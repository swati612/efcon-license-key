package com.nxtlife.efkon.license.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.nxtlife.efkon.license.dao.jpa.LicenseTypeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductDetailJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductCommentJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductLicenseRequestJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductRequestCommentJpaDao;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.entity.project.product.ProjectProductComment;
import com.nxtlife.efkon.license.entity.project.product.ProjectProductLicenseRequest;
import com.nxtlife.efkon.license.entity.project.product.ProjectProductRequestComment;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.enums.ExpirationPeriodType;
import com.nxtlife.efkon.license.enums.LicenseRequestStatus;
import com.nxtlife.efkon.license.enums.LicenseTypeEnum;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProjectProductLicenseRequestService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

@Service("ProjectProductLicenseRequestServiceImpl")
@Transactional
public class ProjectProductLicenseRequestServiceImpl extends BaseService
		implements ProjectProductLicenseRequestService {

	@Autowired
	private ProjectProductJpaDao projectProductDao;

	@Autowired
	private ProductDetailJpaDao productDetailJpaDao;

	@Autowired
	private ProjectJpaDao projectDao;

	@Autowired
	private ProjectProductCommentJpaDao projectProductCommentJpaDao;

	@Autowired
	private ProjectProductLicenseRequestJpaDao projectProductLicenseRequestDao;

	@Autowired
	private LicenseTypeJpaDao licenseTypeJpaDao;

	@Autowired
	private ProjectProductRequestCommentJpaDao projectProductRequestCommentDao;

	private static Logger logger = LoggerFactory.getLogger(ProjectProductLicenseRequestServiceImpl.class);

	/**
	 * validation of ProjectProductId in ProjectProductLicenseRequestRequest
	 * 
	 * @param request
	 */
	private void validate(Long projectProductId) {
		ProjectProductResponse projectProductResponse = projectProductDao
				.findByIdAndProjectCustomerEmailAndActive(unmask(projectProductId), getUser().getEmail(), true);
		if (projectProductResponse == null) {
			throw new NotFoundException(String.format(
					"Project product having id [%s] not found. product is required for license requesting",
					projectProductId));

		}
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_CREATE)
	public ProjectProductLicenseRequestResponse save(Long projectProductId,
			ProjectProductLicenseRequestRequest request) {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		if (!roles.contains("Customer")) {
			throw new ValidationException("Only customer can request for more product");
		}
		validate(projectProductId);
		ProjectProductLicenseRequest pplRequest = request.toEntity();
		pplRequest.settProjectProductId(unmask(projectProductId));
		pplRequest.setStatus(LicenseRequestStatus.PENDING);
		pplRequest.setCustomerEmail(user.getEmail());
		Long projectManagerId = projectProductDao.findProjectManagerIdByProjectProductId(unmask(projectProductId));
		if (projectManagerId != null) {
			pplRequest.settProjectManagerId(projectManagerId);
		} else {
			throw new ValidationException(String.format(
					"Project manager not found of the project for which you're requesting the product having id (%s) ",
					projectProductId));
		}
		projectProductLicenseRequestDao.save(pplRequest);
		if (request.getComment() != null && !request.getComment().isEmpty()) {
			projectProductRequestCommentDao.save(new ProjectProductRequestComment(request.getComment(), getUserId(),
					LicenseRequestStatus.PENDING.toString(), pplRequest.getId()));
		}
		ProjectProductLicenseRequestResponse response = ProjectProductLicenseRequestResponse.get(pplRequest);
		response.setProjectProductResponse(projectProductDao.findByIdAndActive(unmask(projectProductId), true));

		if (response.getProjectProductResponse() != null) {
			response.getProjectProductResponse().setProductDetail(productDetailJpaDao
					.findResponseById(unmask(response.getProjectProductResponse().getProductDetailId())));
			response.getProjectProductResponse().setProject(
					projectDao.findResponseById(unmask(response.getProjectProductResponse().getProjectId())));
		}

		response.setComments(
				projectProductRequestCommentDao.findByProjectProductLicenseRequestId(unmask(response.getId())));
		return response;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_UPDATE)
	public ProjectProductLicenseRequestResponse update(Long id, ProjectProductLicenseRequestRequest request) {
		Long unmaskId = unmask(id);
		ProjectProductLicenseRequestResponse pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId,
				true);
		if (pplrResponse == null) {
			throw new NotFoundException(String.format("Project Product License Request (%s) not found", id));
		}

		if (!pplrResponse.getStatus().equals(LicenseRequestStatus.PENDING)) {
			throw new ValidationException("Project product license request can be updated until the status is pending");
		}

		int rows = projectProductLicenseRequestDao.update(unmaskId,
				request.getLicenseCount() == null ? pplrResponse.getLicenseCount() : request.getLicenseCount(),
				getUserId(), new Date());
		if (rows > 0) {
			logger.info("Project product license request {} updated successfully", unmaskId);
		}
		ProjectProductLicenseRequestResponse response = projectProductLicenseRequestDao.findResponseById(unmaskId);
		response.setProjectProductResponse(
				projectProductDao.findByIdAndActive(unmask(response.getProjectProductId()), true));

		if (response.getProjectProductResponse() != null) {
			response.getProjectProductResponse().setProductDetail(productDetailJpaDao
					.findResponseById(unmask(response.getProjectProductResponse().getProductDetailId())));
			response.getProjectProductResponse().setProject(
					projectDao.findResponseById(unmask(response.getProjectProductResponse().getProjectId())));
		}

		return response;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_FETCH)
	public ProjectProductLicenseRequestResponse findById(Long id) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductLicenseRequestResponse pplrResponse;
		if (roles.contains("Customer")) {
			pplrResponse = projectProductLicenseRequestDao
					.findByIdAndProjectProductProjectCustomerEmailAndActive(unmaskId, user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				pplrResponse = projectProductLicenseRequestDao
						.findByIdAndProjectProductProjectProjectManagerIdAndActive(unmaskId, user.getUserId(), true);
			} else {
				pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId, true);
			}
		}
		if (pplrResponse != null) {
			pplrResponse.setProjectProductResponse(
					projectProductDao.findByIdAndActive(unmask(pplrResponse.getProjectProductId()), true));

			if (pplrResponse.getProjectProductResponse() != null) {
				pplrResponse.getProjectProductResponse().setProductDetail(productDetailJpaDao
						.findResponseById(unmask(pplrResponse.getProjectProductResponse().getProductDetailId())));
				pplrResponse.getProjectProductResponse().setProject(
						projectDao.findResponseById(unmask(pplrResponse.getProjectProductResponse().getProjectId())));
			}

			pplrResponse.setComments(
					projectProductRequestCommentDao.findByProjectProductLicenseRequestId(unmask(pplrResponse.getId())));
		} else {
			throw new NotFoundException(String.format("Project product license request (%s) not found", id));
		}

		return pplrResponse;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_FETCH)
	public List<ProjectProductLicenseRequestResponse> findByStatus(LicenseRequestStatus status) {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductLicenseRequestResponse> pplrResponse;
		if (roles.contains("Customer")) {
			pplrResponse = projectProductLicenseRequestDao
					.findByStatusAndProjectProductProjectCustomerEmailAndActive(status, user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				pplrResponse = projectProductLicenseRequestDao
						.findByStatusAndProjectProductProjectProjectManagerIdAndActive(status, user.getUserId(), true);
			} else {
				pplrResponse = projectProductLicenseRequestDao.findByStatusAndActive(status, true);
			}
		}

		if (pplrResponse != null) {
			for (ProjectProductLicenseRequestResponse iterate : pplrResponse) {
				iterate.setProjectProductResponse(
						projectProductDao.findByIdAndActive(unmask(iterate.getProjectProductId()), true));
				if (iterate.getProjectProductResponse() != null) {
					iterate.getProjectProductResponse().setProductDetail(productDetailJpaDao
							.findResponseById(unmask(iterate.getProjectProductResponse().getProductDetailId())));
					iterate.getProjectProductResponse().setProject(
							projectDao.findResponseById(unmask(iterate.getProjectProductResponse().getProjectId())));
				}
				iterate.setComments(
						projectProductRequestCommentDao.findByProjectProductLicenseRequestId(unmask(iterate.getId())));
			}
		}

		return pplrResponse;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		ProjectProductLicenseRequestResponse pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId,
				true);
		if (pplrResponse == null) {
			throw new NotFoundException(String.format("Project product license request (%s) not found", id));
		}
		if (pplrResponse.getStatus().equals(LicenseRequestStatus.ACCEPTED)) {
			throw new ValidationException("You can't delete accepted request");
		}
		int rows = projectProductLicenseRequestDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("project product license request {} successfully deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Project product license request successfully deleted");

	}

	private void validate(String expirationPeriodType, String licenseType, Integer expirationMonthCount) {
		if (expirationPeriodType != null && !ExpirationPeriodType.matches(expirationPeriodType)) {
			throw new ValidationException(
					String.format("Expiration period type (%s) is not valid", expirationPeriodType));
		}
		if (licenseType != null && !LicenseTypeEnum.matches(licenseType)) {
			throw new ValidationException(String.format("License type (%s) is not valid", licenseType));
		}
		if (licenseType != null && licenseType.equals(LicenseTypeEnum.DEMO.name()) && expirationPeriodType != null
				&& expirationPeriodType.equals(ExpirationPeriodType.LIFETIME.name())) {
			throw new ValidationException("Demo license can't be for lifetime");
		}
		if (expirationPeriodType != null && expirationPeriodType.equals(ExpirationPeriodType.LIMITED.name())
				&& expirationMonthCount == null) {
			throw new ValidationException("Expiration month count can't be null for limited expiration period");
		}
	}

	/**
	 * this method used to set end date according to start date and expiration month
	 * count
	 * <p>
	 * if addition of month of start date and expiration month count greater than 12
	 * then year will be incremented and month will be add result minus 12.
	 *
	 * @return String
	 */
	private String setEndDate(String startDate, Integer expirationMonthCount) {
		if (startDate == null || expirationMonthCount == null) {
			return null;
		}
		try {
			LocalDate endDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			if (endDate.isBefore(LocalDate.now())) {
				throw new ValidationException(String.format("Start date of license (%s) can't be in past", startDate));
			}
			endDate = endDate.plusMonths(expirationMonthCount);
			endDate = endDate.minusDays(1);
			return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException ex) {
			throw new ValidationException(String.format("Start date (%s) isn't valid", startDate));
		}

	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_ACCEPT)
	public ProjectProductLicenseRequestResponse accept(Long id, LicenseRequestStatus status,
			ProjectProductRequest request) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductLicenseRequestResponse pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId,
				true);
		if (pplrResponse == null) {
			throw new NotFoundException(
					String.format("Project Product License Request having id (%s) didn't exist", id));
		}
		LicenseRequestStatus licenseRequestStatus;
		if (roles.contains("Customer") && pplrResponse.getCustomerEmail().equalsIgnoreCase(user.getEmail())) {
			licenseRequestStatus = pplrResponse.getStatus();
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager
					&& pplrResponse.getProjectManagerId().equals(mask(user.getUserId()))) {
				licenseRequestStatus = pplrResponse.getStatus();
			} else if (roles.isEmpty() && isProjectManager
					&& !pplrResponse.getProjectManagerId().equals(mask(user.getUserId()))) {
				licenseRequestStatus = null;
			} else {
				licenseRequestStatus = pplrResponse.getStatus();
			}
		}

		if (licenseRequestStatus != null) {
			if (status.equals(LicenseRequestStatus.ACCEPTED)
					&& licenseRequestStatus.equals(LicenseRequestStatus.REJECTED)) {
				throw new ValidationException(
						"You can't accept project product license request who is already rejected");
			}
			if (status.equals(LicenseRequestStatus.ACCEPTED)
					&& !licenseRequestStatus.equals(LicenseRequestStatus.PENDING)) {
				throw new ValidationException(
						"You can accept project product license request only when status is pending");
			}

			// saving project product after accepting the license request

			Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(request.getLicenseTypeId());
			if (!licenseType.isPresent()) {
				throw new ValidationException("License type is not valid");
			}
			validate(request.getExpirationPeriodType(), licenseType.get().getName(), request.getExpirationMonthCount());
			ProjectProduct projectProduct = request.toEntity();

			ProjectProductResponse ppResponse = projectProductDao
					.findResponseById(unmask(pplrResponse.getProjectProductId()));
			projectProduct.settProductDetailId(unmask(ppResponse.getProductDetailId()));
			projectProduct.settProjectId(unmask(ppResponse.getProjectId()));

			String endDate;
			Integer expirationMonth = request.getExpirationMonthCount();
			if (request.getLicenseTypeId() != null) {
				if (request.getExpirationMonthCount() != null
						&& request.getExpirationMonthCount() > licenseType.get().getMaxMonthCount()) {
					throw new ValidationException(
							String.format("License month (%d) limit exceed", licenseType.get().getMaxMonthCount()));
				}
				if (request.getExpirationMonthCount() == null) {
					expirationMonth = licenseType.get().getMaxMonthCount();
				}
			}

			endDate = setEndDate(request.getStartDate(), expirationMonth);
			projectProduct.setEndDate(endDate);
			projectProduct.setStatus(ProjectProductStatus.SUBMITTED);
			projectProductDao.save(projectProduct);

			projectProductCommentJpaDao
					.save(new ProjectProductComment("This product was created using customer request", getUserId(),
							ProjectProductStatus.SUBMITTED.name(), projectProduct.getId()));

			int rows;
			rows = projectProductLicenseRequestDao.update(unmaskId, status, getUserId(), new Date());

			if (rows > 0) {
				logger.info("Project product license request {} updated successfully", unmaskId);
			}

			pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId, true);
			pplrResponse.setProjectProductResponse(
					projectProductDao.findByIdAndActive(unmask(pplrResponse.getProjectProductId()), true));
			pplrResponse.getProjectProductResponse().setProject(
					projectDao.findResponseById(unmask(pplrResponse.getProjectProductResponse().getProjectId())));

			return pplrResponse;
		} else {
			throw new NotFoundException(String.format("Project product license Request (%s) not found", id));
		}

	}

	@Override
	@Secured(AuthorityUtils.LICENSE_REQUEST_REJECT)
	public ProjectProductLicenseRequestResponse reject(Long id, LicenseRequestStatus status, String comment) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductLicenseRequestResponse pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId,
				true);
		if (pplrResponse == null) {
			throw new NotFoundException(
					String.format("Project Product License Request having id (%s) didn't exist", id));
		}

		LicenseRequestStatus licenseRequestStatus;
		if (roles.contains("Customer") && pplrResponse.getCustomerEmail().equalsIgnoreCase(user.getEmail())) {
			licenseRequestStatus = pplrResponse.getStatus();
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager
					&& pplrResponse.getProjectManagerId().equals(mask(user.getUserId()))) {
				licenseRequestStatus = pplrResponse.getStatus();
			} else if (roles.isEmpty() && isProjectManager
					&& !pplrResponse.getProjectManagerId().equals(mask(user.getUserId()))) {
				licenseRequestStatus = null;
			} else {
				licenseRequestStatus = pplrResponse.getStatus();
			}
		}
		if (licenseRequestStatus != null) {
			if (status.equals(LicenseRequestStatus.REJECTED)
					&& licenseRequestStatus.equals(LicenseRequestStatus.ACCEPTED)) {
				throw new ValidationException(
						"You can't reject project product license request who is already accepted");
			}
			if (status.equals(LicenseRequestStatus.REJECTED)
					&& licenseRequestStatus.equals(LicenseRequestStatus.REJECTED)) {
				throw new ValidationException("Project product license request is already rejected");
			}
			if (comment == null || comment.isEmpty()) {
				throw new ValidationException("Comment is required at the reject time");
			}
			int rows;
			rows = projectProductLicenseRequestDao.update(unmaskId, LicenseRequestStatus.REJECTED, getUserId(),
					new Date());
			if (rows > 0) {
				logger.info("Project product license request {} successfully updated", unmaskId);
			}
			projectProductRequestCommentDao
					.save(new ProjectProductRequestComment(comment, getUserId(), status.name(), unmaskId));
			pplrResponse = projectProductLicenseRequestDao.findByIdAndActive(unmaskId, true);
			if (pplrResponse != null) {
				pplrResponse.setProjectProductResponse(
						projectProductDao.findByIdAndActive(unmask(pplrResponse.getProjectProductId()), true));
				pplrResponse.getProjectProductResponse().setProject(
						projectDao.findResponseById(unmask(pplrResponse.getProjectProductResponse().getProjectId())));
				pplrResponse
						.setComments(projectProductRequestCommentDao.findByProjectProductLicenseRequestId(unmaskId));
			}
			return pplrResponse;
		} else {
			throw new NotFoundException(String.format("Project product license Request (%s) not found", id));
		}

	}

}
