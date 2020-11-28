package com.nxtlife.efkon.license.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nxtlife.efkon.license.dao.jpa.LicenseJpaDao;
import com.nxtlife.efkon.license.dao.jpa.LicenseTypeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductDetailJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductCommentJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RenewConfigurationJpaDao;
import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.entity.project.product.ProjectProductComment;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.enums.ExpirationPeriodType;
import com.nxtlife.efkon.license.enums.LicenseStatus;
import com.nxtlife.efkon.license.enums.LicenseTypeEnum;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.FileStorageService;
import com.nxtlife.efkon.license.service.ProjectProductService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.util.DateUtil;
import com.nxtlife.efkon.license.util.ITextPdfUtil;
import com.nxtlife.efkon.license.util.PdfHeaderFooterPageEvent;
import com.nxtlife.efkon.license.util.PdfTableUtil;
import com.nxtlife.efkon.license.util.WorkBookUtil;
import com.nxtlife.efkon.license.view.RenewConfigurationResponse;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.project.ProjectResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductGraphResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

@Service("projectProductServiceImpl")
@Transactional
public class ProjectProductServiceImpl extends BaseService implements ProjectProductService {

	@Autowired
	private ProjectProductJpaDao projectProductDao;

	@Autowired
	private ProjectJpaDao projectDao;

	@Autowired
	private ProductDetailJpaDao productDetailDao;

	@Autowired
	private ProjectProductCommentJpaDao projectProductCommentDao;

	@Autowired
	private LicenseJpaDao licenseDao;

	@Autowired
	private LicenseTypeJpaDao licenseTypeJpaDao;

	@Autowired
	private RenewConfigurationJpaDao renewConfigurationJpaDao;

	@Value("${file.upload-excel-dir}")
	private String excelDirectory;

	@Value("${file.upload-pdf-dir}")
	private String pdfDirectory;

	private static Logger logger = LoggerFactory.getLogger(ProjectProductServiceImpl.class);

	private void createExcel(List<ProjectProductResponse> projectProductResponseList, String fileName, String heading) {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		try {
			Sheet sheet = workbook.createSheet(heading);
			List<String> columnHeaders = ProjectProductResponse.projectProductsColumnHeaders();
			CellStyle headerStyle = WorkBookUtil.getHeaderCellStyle(workbook);
			WorkBookUtil.createRow(headerStyle, sheet, columnHeaders, 0);
			Integer row = 1;

			for (ProjectProductResponse iterate : projectProductResponseList) {
				WorkBookUtil.createRow(sheet, iterate.columnValues(), row++);
			}
			FileOutputStream fout = new FileOutputStream(fileName);
			workbook.write(fout);
			workbook.close();
			workbook.dispose();
			fout.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidationException(String.format("Couldn't create excel because of %s", e.getMessage()));
		}

	}

	private void createPdf(List<ProjectProductResponse> projectProductResponseList, String fileName, String heading) {

		Document document = new Document(PageSize.A4, 10f, 10f, 10f, 30f);
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, fout);
			List<String> columnHeaders = ProjectProductResponse.projectProductsColumnHeaders();
			int length = columnHeaders.size();
			PdfPTable headerPdfTable = new PdfPTable(length);
			PdfTableUtil.addTableHeader(headerPdfTable, columnHeaders);
			String generatedDate = DateUtil.get(new Date(), "dd-MM-yyyy HH:mm:ss");
			pdfWriter.setPageEvent(new PdfHeaderFooterPageEvent(heading, headerPdfTable, generatedDate));
			document.open();
			ITextPdfUtil.setProjectProductDocumentProperties(document);
			PdfPTable pdfTable = new PdfPTable(length);
			for (ProjectProductResponse iterate : projectProductResponseList) {
				PdfTableUtil.addTableRows(pdfTable, iterate.columnValues());
			}
			pdfTable.setWidthPercentage(95f);
			document.add(pdfTable);
			document.close();
			fout.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

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

	private void validate(ProjectProductRequest request, String licenseType) {
		if (!projectDao.existsByIdAndActive(request.getProjectId(), true)) {
			throw new ValidationException(String.format("Project (%s) not exist", mask(request.getProjectId())));
		}
		if (!productDetailDao.existsByIdAndActive(request.getProductDetailId(), true)) {
			throw new ValidationException(
					String.format("Product detail (%s) not exist", mask(request.getProductDetailId())));
		}
		validate(request.getExpirationPeriodType(), licenseType, request.getExpirationMonthCount());
	}

	private ProjectProductResponse getProjectProductResponse(ProjectProduct projectProduct, Long projectId,
			Long productDetailId) {
		ProjectProductResponse response = ProjectProductResponse.get(projectProduct);
		response.setProject(projectDao.findResponseById(projectId));
		ProductDetailResponse productDetailResponse = productDetailDao.findResponseById(productDetailId);
		response.setProductDetail(productDetailResponse);
		return response;
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

	private List<ProjectProductGraphResponse> getDashboardResponseForSubmitter(Integer showBeforeDays) {
		List<Tuple> statusWiseProductCount = null;
		List<ProjectProductGraphResponse> responseList = new ArrayList<>();
		Tuple draftCount = projectProductDao.countProductAndSumLicenseCountByStatusAndCreatedByIdAndActive(
				ProjectProductStatus.DRAFT, getUserId(), true);
		statusWiseProductCount = projectProductDao.countProductAndSumLicenseCountByStatusAndActiveAndNotInStatus(true,
				Arrays.asList(ProjectProductStatus.DRAFT));
		for (ProjectProductStatus status : ProjectProductStatus.values()) {
			if (status.equals(ProjectProductStatus.DRAFT)) {
				responseList.add(new ProjectProductGraphResponse(status.name(), status.getName(),
						draftCount.get("productCount", Long.class), draftCount.get("licenseCount", Long.class)));
			} else {
				Boolean available = false;
				for (Tuple tuple : statusWiseProductCount) {
					if (tuple.get("status", ProjectProductStatus.class).equals(status)) {
						responseList.add(new ProjectProductGraphResponse(status.name(), status.getName(),
								tuple.get("productCount", Long.class), tuple.get("licenseCount", Long.class)));
						available = true;
						break;
					}
				}
				if (!available) {
					responseList.add(new ProjectProductGraphResponse(status.name(), status.getName(), 0l, 0l));
				}
			}
		}
		LocalDate date = LocalDate.now();
		date = date.plusDays(showBeforeDays);
		Tuple renewalCount = projectProductDao
				.countProductAndSumLicenseCountByStatusAndActiveAndBeforeEndDateAndLicenseTypeName(
						ProjectProductStatus.APPROVED, true, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
						LicenseTypeEnum.COMMERCIAL.name());
		responseList.add(new ProjectProductGraphResponse("RENEWALP", "Renewal Pending",
				renewalCount != null ? renewalCount.get("productCount", Long.class) : 0l,
				renewalCount != null ? renewalCount.get("licenseCount", Long.class) : 0l));
		return responseList;
	}

	private List<ProjectProductGraphResponse> getDashboardResponseForProjectManager(Integer showBeforeDays) {
		List<Tuple> statusWiseProductCount = null;
		List<ProjectProductGraphResponse> responseList = new ArrayList<>();
		Tuple rejectedCount = projectProductDao.countProductAndSumLicenseCountByStatusAndModifiedByIdAndActive(
				ProjectProductStatus.REJECTED, getUserId(), true);
		statusWiseProductCount = projectProductDao
				.countProductAndSumLicenseCountByStatusAndProjectProjectManagerIdAndActiveAndNotInStatus(getUserId(),
						true, Arrays.asList(ProjectProductStatus.DRAFT));

		for (ProjectProductStatus status : ProjectProductStatus.values()) {
			if (status.equals(ProjectProductStatus.DRAFT)) {
			} else {
				Boolean available = false;
				String name = null;
				for (Tuple tuple : statusWiseProductCount) {
					if (status.equals(ProjectProductStatus.REJECTED)
							&& tuple.get("status", ProjectProductStatus.class).equals(status)) {
						responseList.add(new ProjectProductGraphResponse("REJECTEDM", "Rejected by me",
								rejectedCount != null ? rejectedCount.get("productCount", Long.class) : 0l,
								rejectedCount != null ? rejectedCount.get("licenseCount", Long.class) : 0l));
						responseList
								.add(new ProjectProductGraphResponse(status.name(), "Rejected by others", tuple.get(
										"productCount", Long.class)
										- (rejectedCount != null ? rejectedCount.get("productCount", Long.class) : 0l),
										tuple.get("licenseCount", Long.class)
												- (rejectedCount != null
														? (rejectedCount.get("licenseCount", Long.class) == null ? 0l
																: rejectedCount.get("licenseCount", Long.class))
														: 0l)));
						available = true;
						break;
					} else if (tuple.get("status", ProjectProductStatus.class).equals(status)) {
						if (status.equals(ProjectProductStatus.SUBMITTED)) {
							name = "Pending for review";
						} else {
							name = status.getName();
						}
						responseList.add(new ProjectProductGraphResponse(status.name(), name,
								tuple.get("productCount", Long.class), tuple.get("licenseCount", Long.class)));
						available = true;
						break;
					}
				}
				if (!available) {
					if (status.equals(ProjectProductStatus.SUBMITTED)) {
						name = "Pending for review";
					} else if (status.equals(ProjectProductStatus.REJECTED)) {
						responseList.add(new ProjectProductGraphResponse("REJECTEDM", "Rejected by me",
								rejectedCount != null ? rejectedCount.get("productCount", Long.class) : 0l,
								rejectedCount != null ? rejectedCount.get("licenseCount", Long.class) : 0l));
						name = "Rejected by others";
					} else {
						name = status.getName();
					}
					responseList.add(new ProjectProductGraphResponse(status.name(), name, 0l, 0l));
				}
			}
		}
		LocalDate date = LocalDate.now();
		date = date.plusDays(showBeforeDays);
		Tuple renewalCount = projectProductDao
				.countProductAndSumLicenseCountByStatusAndProjectProjectManagerIdAndActiveAndBeforeEndDateAndLicenseTypeName(
						ProjectProductStatus.APPROVED, getUserId(), true,
						date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), LicenseTypeEnum.COMMERCIAL.name());
		responseList.add(new ProjectProductGraphResponse("RENEWALP", "Renewal Pending",
				renewalCount != null ? renewalCount.get("productCount", Long.class) : 0l,
				renewalCount != null ? renewalCount.get("licenseCount", Long.class) : 0l));
		return responseList;
	}

	private List<ProjectProductGraphResponse> getDashboardResponseForApprover() {
		List<Tuple> statusWiseProductCount = null;
		List<ProjectProductGraphResponse> responseList = new ArrayList<>();
		Tuple rejectedCount = projectProductDao.countProductAndSumLicenseCountByStatusAndModifiedByIdAndActive(
				ProjectProductStatus.REJECTED, getUserId(), true);
		statusWiseProductCount = projectProductDao.countProductAndSumLicenseCountByStatusAndActiveAndNotInStatus(true,
				Arrays.asList(ProjectProductStatus.DRAFT, ProjectProductStatus.SUBMITTED,
						ProjectProductStatus.REJECTED));
		for (ProjectProductStatus status : ProjectProductStatus.values()) {
			if (status.equals(ProjectProductStatus.DRAFT) || status.equals(ProjectProductStatus.SUBMITTED)) {
			} else if (status.equals(ProjectProductStatus.REJECTED)) {
				responseList.add(new ProjectProductGraphResponse(status.name(), "Rejected by me",
						rejectedCount != null ? rejectedCount.get("productCount", Long.class) : 0l,
						rejectedCount != null ? (rejectedCount.get("licenseCount", Long.class) != null
								? rejectedCount.get("licenseCount", Long.class)
								: 0l) : 0l));

			} else {
				Boolean available = false;
				String name = null;
				for (Tuple tuple : statusWiseProductCount) {
					if (tuple.get("status", ProjectProductStatus.class).equals(status)) {
						if (status.equals(ProjectProductStatus.APPROVED)) {
							name = "Approved";
						} else if (status.equals(ProjectProductStatus.REVIEWED)) {
							name = "Pending for approval";
						} else {
							name = status.getName();
						}
						responseList.add(new ProjectProductGraphResponse(status.name(), name,
								tuple.get("productCount", Long.class),
								tuple.get("licenseCount", Long.class) != null ? tuple.get("licenseCount", Long.class)
										: 0l));
						available = true;
						break;
					}
				}
				if (!available) {
					if (status.equals(ProjectProductStatus.APPROVED)) {
						name = "Approved";
					} else if (status.equals(ProjectProductStatus.REVIEWED)) {
						name = "Pending for approval";
					} else {
						name = status.getName();
					}
					responseList.add(new ProjectProductGraphResponse(status.name(), name, 0l, 0l));
				}
			}
		}
		return responseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_CREATE)
	public ProjectProductResponse save(ProjectProductRequest request) {
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(request.getLicenseTypeId());
		if (!licenseType.isPresent()) {
			throw new ValidationException("License type is not valid");
		}
		validate(request, licenseType.get().getName());
		ProjectProduct projectProduct = request.toEntity();
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
		projectProduct.setStatus(ProjectProductStatus.DRAFT);
		projectProductDao.save(projectProduct);
		return getProjectProductResponse(projectProduct, request.getProjectId(), request.getProductDetailId());
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_UPDATE)
	public ProjectProductResponse update(Long id, ProjectProductRequest request) {
		Long unmaskId = unmask(id);
		ProjectProductResponse projectProduct = projectProductDao.findResponseById(unmaskId);
		if (projectProduct == null) {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		if (!projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)
				&& !projectProduct.getStatus().equals(ProjectProductStatus.REJECTED)) {
			throw new ValidationException("Project product can't be updated");
		}
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(request.getLicenseTypeId());
		if (!licenseType.isPresent()) {
			throw new ValidationException("License type is not valid");
		}
		validate(request.getExpirationPeriodType(), licenseType.get().getName(), request.getExpirationMonthCount());

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
		int rows = projectProductDao.update(unmaskId,
				request.getLicenseCount() == null ? projectProduct.getLicenseCount() : request.getLicenseCount(),
				request.getLicenseTypeId() == null ? projectProduct.getLicenseTypeId() : request.getLicenseTypeId(),
				request.getExpirationPeriodType() == null ? projectProduct.getExpirationPeriodType()
						: ExpirationPeriodType.valueOf(request.getExpirationPeriodType()),
				request.getExpirationMonthCount() == null ? projectProduct.getExpirationMonthCount()
						: request.getExpirationMonthCount(),
				request.getStartDate() == null ? projectProduct.getStartDate() : request.getStartDate(),
				setEndDate(request.getStartDate(), expirationMonth), getUserId(), new Date(),
				ProjectProductStatus.DRAFT);
		if (rows > 0) {
			logger.info("Project product {} updated successfully", unmaskId);
		}
		ProjectProductResponse projectProductResponse = projectProductDao.findResponseById(unmaskId);
		projectProductResponse.setProductDetail(
				productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
		projectProductResponse.setProject(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
		return projectProductResponse;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public List<ProjectProductResponse> findAll() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProductResponseList;
		if (roles.contains("Customer")) {
			projectProductResponseList = projectProductDao
					.findByStatusAndProjectCustomerEmailAndActive(ProjectProductStatus.APPROVED, user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponseList = projectProductDao.findByProjectProjectManagerIdAndActive(user.getUserId(),
						true);
			} else {
				projectProductResponseList = projectProductDao.findByActive(true);
			}
		}
		projectProductResponseList = projectProductResponseList.stream()
				.filter(projectProduct -> !(projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)
						&& !projectProduct.getCreatedById().equals(getUserId())))
				.collect(Collectors.toList());
		projectProductResponseList.forEach(projectProduct -> {
			projectProduct
					.setProductDetail(productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
			projectProduct.setProject(projectDao.findResponseById(unmask(projectProduct.getProjectId())));
			if (!roles.contains("Customer"))
				projectProduct
						.setComments(projectProductCommentDao.findByProjectProductId(unmask(projectProduct.getId())));
			List<LicenseResponse> licenses = licenseDao.findByProjectProductIdAndActive(unmask(projectProduct.getId()),
					true);
			licenses.forEach(license -> {
				if (license.getGeneratedKey() != null) {
					projectProduct.setGeneratedLicenseCount(projectProduct.getGeneratedLicenseCount() + 1);
				}
			});
			projectProduct.setLicenses(licenses);
		});
		return projectProductResponseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public Resource findAllExcel() {
		List<ProjectProductResponse> projectProductResponseList = findAll();
		String fileName = excelDirectory + "Allproducts.xlsx";
		createExcel(projectProductResponseList, fileName, "ProjectProducts");
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public Resource findAllPdf() {
		List<ProjectProductResponse> projectProductResponseList = findAll();
		String fileName = pdfDirectory + "Allproducts.pdf";
		createPdf(projectProductResponseList, fileName, "ProjectProducts");
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public List<ProjectProductResponse> findByProjectId(Long projectId) {
		User user = getUser();
		Long unmaskProjectId = unmask(projectId);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProductResponseList;
		if (roles.contains("Customer")) {
			projectProductResponseList = projectProductDao.findByProjectIdAndProjectCustomerEmailAndActiveAndStatus(
					unmaskProjectId, user.getEmail(), true, ProjectProductStatus.APPROVED);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponseList = projectProductDao
						.findByProjectIdAndProjectProjectManagerIdAndActive(unmaskProjectId, user.getUserId(), true);
			} else {
				projectProductResponseList = projectProductDao.findByProjectIdAndActive(unmaskProjectId, true);
			}
		}
		projectProductResponseList = projectProductResponseList.stream()
				.filter(projectProduct -> !(projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)
						&& !projectProduct.getCreatedById().equals(getUserId())))
				.collect(Collectors.toList());
		projectProductResponseList.forEach(projectProduct -> {
			projectProduct
					.setProductDetail(productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
			projectProduct.setProject(projectDao.findResponseById(unmask(projectProduct.getProjectId())));
			if (!roles.contains("Customer"))
				projectProduct
						.setComments(projectProductCommentDao.findByProjectProductId(unmask(projectProduct.getId())));
		});
		return projectProductResponseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public Resource findByProjectIdExcel(Long projectId) {
		List<ProjectProductResponse> projectProductResponseList = findByProjectId(projectId);
		String fileName = excelDirectory + "productsByProjectId.xlsx";
		createExcel(projectProductResponseList, fileName, "ProjectProducts");
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public Resource findByProjectIdPdf(Long projectId) {
		List<ProjectProductResponse> projectProductResponseList = findByProjectId(projectId);
		String fileName = pdfDirectory + "productsByProjectId.pdf";
		createPdf(projectProductResponseList, fileName, "ProjectProducts");
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public ProjectProductResponse findById(Long id) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductResponse projectProductResponse;
		if (roles.contains("Customer")) {
			projectProductResponse = projectProductDao.findByIdAndProjectCustomerEmailAndActive(unmaskId,
					user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponse = projectProductDao.findByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
			} else {
				projectProductResponse = projectProductDao.findByIdAndActive(unmaskId, true);
			}
		}
		if (projectProductResponse != null) {
			projectProductResponse.setProductDetail(
					productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
			projectProductResponse
					.setProject(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
			projectProductResponse.setComments(projectProductCommentDao.findByProjectProductId(unmaskId));
		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		return projectProductResponse;
	}

	@Override
	public ProjectProductResponse updateStatus(Long id, ProjectProductStatus status, String comment) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductStatus projectProductStatus;
		if (roles.contains("Customer")) {
			projectProductStatus = projectProductDao.findStatusByIdAndProjectCustomerEmailAndActive(unmaskId,
					user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductStatus = projectProductDao.findStatusByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
				if (status.equals(ProjectProductStatus.REJECTED)
						&& projectProductStatus.equals(ProjectProductStatus.REVIEWED)) {
					throw new ValidationException("Project Manager can't reject project product after review");
				}
			} else {
				projectProductStatus = projectProductDao.findStatusByIdAndActive(unmaskId, true);
			}
		}
		if (projectProductStatus != null) {
			if (status.equals(ProjectProductStatus.APPROVED)
					&& !projectProductStatus.equals(ProjectProductStatus.REVIEWED)) {
				throw new ValidationException("You can approve project product after review only");
			}
			if (status.equals(ProjectProductStatus.SUBMITTED)
					&& (!projectProductStatus.equals(ProjectProductStatus.DRAFT)
							&& !projectProductStatus.equals(ProjectProductStatus.REJECTED))) {
				throw new ValidationException("You can submit project product after draft/reject only");
			}
			if (status.equals(ProjectProductStatus.REVIEWED)
					&& !projectProductStatus.equals(ProjectProductStatus.SUBMITTED)) {
				throw new ValidationException("You can review project product after submit only");
			}

			if (status.equals(ProjectProductStatus.REJECTED) && (comment == null || comment.isEmpty())) {
				throw new ValidationException("Comment is required at the reject time");
			}
			if (status.equals(ProjectProductStatus.REJECTED) && (projectProductStatus.equals(ProjectProductStatus.DRAFT)
					|| projectProductStatus.equals(ProjectProductStatus.APPROVED))) {
				throw new ValidationException("You can't reject project product in draft and approved mode");
			}
			int rows;
			if (status.equals(ProjectProductStatus.REJECTED)) {
				rows = projectProductDao.update(unmaskId, ProjectProductStatus.REJECTED, getUserId(), new Date());
			} else {
				rows = projectProductDao.update(unmaskId, status, getUserId(), new Date());
			}
			if (comment != null && !comment.isEmpty()) {
				projectProductCommentDao.save(new ProjectProductComment(comment, getUserId(), status.name(), unmaskId));
			}
			ProjectProductResponse projectProductResponse = projectProductDao.findByIdAndActive(unmaskId, true);
			if (projectProductResponse != null) {
				projectProductResponse.setProductDetail(
						productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
				projectProductResponse
						.setProject(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
				if (status.equals(ProjectProductStatus.APPROVED) && rows > 0) {
					logger.info("Project product {} approved successfully", unmaskId);
					Integer licenseCount = projectProductDao.findLicenseCountById(unmaskId);
					for (int i = 0; i < licenseCount; i++) {
						licenseDao.save(new License(
								String.format("EF-%s-%s-%s-%s-%s-%04d-%04d-%s-%s",
										projectProductResponse.getProject().getCustomerCode(),
										projectProductResponse.getProductDetail().getProductFamilyCode(),
										projectProductResponse.getProductDetail().getProductCodeCode(),
										getUser().getCode(), projectProductResponse.getLicenseTypeCode(), (i + 1),
										projectProductResponse.getExpirationMonthCount() == null ? 0
												: projectProductResponse.getExpirationMonthCount(),
										LocalDate
												.parse(projectProductResponse.getStartDate(),
														DateTimeFormatter.ofPattern("yyyy-MM-dd"))
												.format(DateTimeFormatter.ofPattern("ddMMyyyy")),
										projectProductResponse.getEndDate() == null ? "NA"
												: LocalDate
														.parse(projectProductResponse.getEndDate(),
																DateTimeFormatter.ofPattern("yyyy-MM-dd"))
														.format(DateTimeFormatter.ofPattern("ddMMyyyy"))),
								LicenseStatus.ACTIVE, unmaskId));
					}
				}
				projectProductResponse.setComments(projectProductCommentDao.findByProjectProductId(unmaskId));
			}
			return projectProductResponse;
		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_UNDO)
	public ProjectProductResponse undo(Long id, String comment) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductStatus projectProductStatus;
		if (roles.contains("Customer")) {
			throw new ValidationException(
					String.format("you don't have access to undo the project product having id [%s]", id));
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductStatus = projectProductDao.findStatusByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
				if (projectProductStatus.equals(ProjectProductStatus.SUBMITTED)) {
					throw new ValidationException(
							String.format("Project Manager can't recall the project product whose status is %s",
									projectProductStatus));
				}
			} else {
				projectProductStatus = projectProductDao.findStatusByIdAndActive(unmaskId, true);
				if (roles.contains("Submitter")) {
					Boolean isSUBMITTEDter = true;
					roles.remove("Submitter");
					if (roles.isEmpty() && isSUBMITTEDter) {
						if (projectProductStatus.equals(ProjectProductStatus.REVIEWED)) {
							throw new ValidationException(
									String.format("Submitter can't recall the project product whose status is %s",
											projectProductStatus));
						}
					}
				}

			}
		}
		int rows = 0;

		if (projectProductStatus != null) {
			if (projectProductStatus.equals(ProjectProductStatus.DRAFT)
					|| projectProductStatus.equals(ProjectProductStatus.APPROVED)
					|| projectProductStatus.equals(ProjectProductStatus.REJECTED)) {
				throw new ValidationException(String.format(
						"It is not possibe to undo the project product whose status is (%s) ", projectProductStatus));
			}
			if (projectProductStatus.equals(ProjectProductStatus.SUBMITTED)) {
				rows = projectProductDao.update(unmaskId, ProjectProductStatus.DRAFT, getUserId(), new Date());
			}
			if (projectProductStatus.equals(ProjectProductStatus.REVIEWED)) {
				rows = projectProductDao.update(unmaskId, ProjectProductStatus.SUBMITTED, getUserId(), new Date());
			}
			if (comment != null && !comment.isEmpty()) {
				projectProductCommentDao.save(new ProjectProductComment(comment, getUserId(), "RECALL", unmaskId));
			}
			if (rows > 0) {
				logger.info("Project product {} undo request accepted successfully", unmaskId);
			}
			ProjectProductResponse projectProductResponse = projectProductDao.findByIdAndActive(unmaskId, true);
			if (projectProductResponse != null) {
				projectProductResponse.setProductDetail(
						productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
				projectProductResponse
						.setProject(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
				projectProductResponse.setComments(projectProductCommentDao.findByProjectProductId(unmaskId));
			}
			return projectProductResponse;
		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_RENEW)
	public ProjectProductResponse renew(Long id, ProjectProductRequest request) {
		User user = getUser();
		Long unmaskId = unmask(id);
		List<RenewConfigurationResponse> renewConfigurationResponses = renewConfigurationJpaDao.findByActiveTrue();
		RenewConfigurationResponse renewConfiguration = null;
		if (!renewConfigurationResponses.isEmpty()) {
			renewConfiguration = renewConfigurationResponses.get(0);
		} else {
			throw new ValidationException("Renew configuration details not found");
		}
		if (request.getExpirationMonthCount() == null || (renewConfiguration != null
				&& renewConfiguration.getStartDateChange() && request.getStartDate() == null)) {
			throw new ValidationException("Start date and exipration month count is mandatory for renewal");
		}
		if (request.getExpirationMonthCount() < 1) {
			throw new ValidationException("Expiration month can't be less than 1");
		}
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductResponse projectProduct = null;
		if (roles.contains("Customer")) {
			projectProduct = projectProductDao.findByIdAndProjectCustomerEmailAndActive(unmaskId, user.getEmail(),
					true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProduct = projectProductDao.findByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
			} else {
				projectProduct = projectProductDao.findByIdAndActive(unmaskId, true);
			}
		}
		if (projectProduct != null) {
			if (projectProduct.getLicenseTypeName().equals(LicenseTypeEnum.DEMO.name())) {
				throw new ValidationException("You can't renew demo product");
			}
			if (projectProduct.getStatus().equals(ProjectProductStatus.RENEWED)) {
				throw new ValidationException(
						String.format("This product (%s) already renewed", projectProduct.getId()));
			}
			if (!projectProduct.getStatus().equals(ProjectProductStatus.APPROVED)) {
				throw new ValidationException(
						String.format("You can't renew if project product(%s) not approved", projectProduct.getId()));
			}
			if (request.getStartDate() != null && projectProduct.getEndDate().compareTo(request.getStartDate()) > 1) {
				throw new ValidationException(
						"Start date of renewal license should be greater than end date of previous");
			}
			LocalDate endDate = LocalDate.parse(projectProduct.getEndDate());
			if (renewConfiguration != null && renewConfiguration.getShowBeforeDays() != null) {
				LocalDate date = LocalDate.now().plusDays(renewConfiguration.getShowBeforeDays());
				if (endDate.isAfter(date)) {
					throw new ValidationException(String.format("You can't renew this product now"));
				}
			}

			ProjectProduct renewedProjectProduct = new ProjectProduct(projectProduct.getLicenseCount(),
					unmask(projectProduct.getLicenseTypeId()), projectProduct.getExpirationPeriodType(),
					request.getExpirationMonthCount(),
					request.getStartDate() == null ? projectProduct.getEndDate() : request.getStartDate(),
					setEndDate(request.getStartDate() == null ? projectProduct.getEndDate() : request.getStartDate(),
							request.getExpirationMonthCount()),
					ProjectProductStatus.SUBMITTED);
			renewedProjectProduct.settProductDetailId(unmask(projectProduct.getProductDetailId()));
			renewedProjectProduct.settProjectId(unmask(projectProduct.getProjectId()));
			renewedProjectProduct.settPastProjectProductId(unmaskId);
			renewedProjectProduct = projectProductDao.save(renewedProjectProduct);
			projectProductCommentDao.save(new ProjectProductComment("Project Renewed", getUserId(),
					ProjectProductStatus.RENEWED.name(), renewedProjectProduct.getId()));
			projectProductDao.update(unmaskId, ProjectProductStatus.RENEWED, getUserId(), new Date());
			ProjectProductResponse projectProductResponse = projectProductDao
					.findResponseById(renewedProjectProduct.getId());
			projectProductResponse.setProductDetail(
					productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
			projectProductResponse
					.setProject(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
			logger.info("Project product {} renewed successfully", unmaskId);
			projectProductResponse
					.setComments(projectProductCommentDao.findByProjectProductId(renewedProjectProduct.getId()));
			return projectProductResponse;

		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}

	}

	@Override
	@Secured(AuthorityUtils.DASHBOARD_FETCH)
	public List<ProjectProductGraphResponse> findCountByStatus() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		if (roles.contains("Customer")) {
			Long approvedProjectProductCount = projectProductDao.countProductByStatusAndProjectCustomerEmailAndActive(
					ProjectProductStatus.APPROVED, user.getEmail(), true);
			return Arrays.asList(new ProjectProductGraphResponse(ProjectProductStatus.APPROVED.name(),
					ProjectProductStatus.APPROVED.name(), approvedProjectProductCount, null));
		} else {
			List<RenewConfigurationResponse> renewConfigurationResponses = renewConfigurationJpaDao.findByActiveTrue();
			RenewConfigurationResponse renewConfiguration = null;
			if (!renewConfigurationResponses.isEmpty()) {
				renewConfiguration = renewConfigurationResponses.get(0);
			} else {
				throw new ValidationException("Renew configuration details not found");
			}
			if (roles.contains("SuperAdmin")) {
				return new ArrayList<>();
			} else if (roles.contains("Approver")) {
				return this.getDashboardResponseForApprover();
			} else if (roles.contains("Project Manager")) {
				return this.getDashboardResponseForProjectManager(renewConfiguration.getShowBeforeDays());
			} else {
				return this.getDashboardResponseForSubmitter(renewConfiguration.getShowBeforeDays());
			}
		}
	}

	@Override
	@Secured(AuthorityUtils.DASHBOARD_FETCH)
	public List<ProjectProductResponse> findByStatus(String name) {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProducts = null;
		if (roles.contains("Customer")) {
			if (name.equalsIgnoreCase(ProjectProductStatus.APPROVED.name())) {
				projectProducts = projectProductDao.findByProjectCustomerEmailAndActiveAndStatusIn(user.getEmail(),
						true, Arrays.asList(ProjectProductStatus.APPROVED));
			} else {
				return new ArrayList<>();
			}
		} else {
			List<RenewConfigurationResponse> renewConfigurationResponses = renewConfigurationJpaDao.findByActiveTrue();
			RenewConfigurationResponse renewConfiguration = null;
			if (!renewConfigurationResponses.isEmpty()) {
				renewConfiguration = renewConfigurationResponses.get(0);
			} else {
				throw new ValidationException("Renew configuration details not found");
			}
			if (roles.contains("SuperAdmin")) {
				return new ArrayList<>();
			} else if (roles.contains("Approver")) {
				if (name.equalsIgnoreCase(ProjectProductStatus.REJECTED.name())) {
					projectProducts = projectProductDao.findByActiveAndStatusAndModifiedById(true,
							ProjectProductStatus.REJECTED, getUserId());
				} else {
					projectProducts = projectProductDao.findByActiveAndStatus(true, ProjectProductStatus.valueOf(name));
				}
			} else if (roles.contains("Project Manager")) {
				if (name.equalsIgnoreCase(ProjectProductStatus.DRAFT.name())) {
					projectProducts = new ArrayList<>();
				} else if (name.equalsIgnoreCase(ProjectProductStatus.REJECTED.name())) {
					projectProducts = projectProductDao
							.findByActiveAndStatusAndProjectProjectManagerIdAndModifiedByIdNot(true,
									ProjectProductStatus.REJECTED, getUserId(), getUserId());
				} else if (name.equalsIgnoreCase("REJECTEDM")) {
					projectProducts = projectProductDao.findByActiveAndStatusAndModifiedById(true,
							ProjectProductStatus.REJECTED, getUserId());
				} else if (name.equalsIgnoreCase("RENEWALP")) {
					LocalDate date = LocalDate.now();
					date = date.plusDays(renewConfiguration.getShowBeforeDays());
					projectProducts = projectProductDao
							.findByActiveAndStatusAndProjectProjectManagerIdAndEndDateLessThanEqual(true,
									ProjectProductStatus.APPROVED, getUserId(),
									date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					projectProducts = projectProducts.stream().filter(projectProduct -> projectProduct
							.getLicenseTypeName().equalsIgnoreCase(LicenseTypeEnum.COMMERCIAL.name()))
							.collect(Collectors.toList());
				} else {
					projectProducts = projectProductDao.findByActiveAndStatusAndProjectProjectManagerId(true,
							ProjectProductStatus.valueOf(name), getUserId());
				}
			} else {
				if (name.equalsIgnoreCase(ProjectProductStatus.DRAFT.name())) {
					projectProducts = projectProductDao.findByActiveAndStatusAndCreatedById(true,
							ProjectProductStatus.DRAFT, getUserId());
				} else if (name.equalsIgnoreCase("RENEWALP")) {
					LocalDate date = LocalDate.now();
					date = date.plusDays(renewConfiguration.getShowBeforeDays());
					projectProducts = projectProductDao.findByActiveAndStatusAndEndDateLessThanEqual(true,
							ProjectProductStatus.APPROVED, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					projectProducts = projectProducts.stream().filter(projectProduct -> projectProduct
							.getLicenseTypeName().equalsIgnoreCase(LicenseTypeEnum.COMMERCIAL.name()))
							.collect(Collectors.toList());
				} else {
					projectProducts = projectProductDao.findByActiveAndStatus(true, ProjectProductStatus.valueOf(name));
				}
			}
		}
		projectProducts.forEach(projectProduct -> {
			projectProduct
					.setProductDetail(productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
			projectProduct.setProject(projectDao.findResponseById(unmask(projectProduct.getProjectId())));
			if (!roles.contains("Customer"))
				projectProduct
						.setComments(projectProductCommentDao.findByProjectProductId(unmask(projectProduct.getId())));
			List<LicenseResponse> licenses = licenseDao.findByProjectProductIdAndActive(unmask(projectProduct.getId()),
					true);
			licenses.forEach(license -> {
				if (license.getGeneratedKey() != null) {
					projectProduct.setGeneratedLicenseCount(projectProduct.getGeneratedLicenseCount() + 1);
				}
			});
			projectProduct.setLicenses(licenses);
		});
		return projectProducts;
	}

	@Override
	@Secured(AuthorityUtils.REPORT_FETCH)
	public List<ProjectProductGraphResponse> findCountByApprovedStatusAndGroupByCustomerEmail() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<Tuple> tuples;
		if (getUserId() == null) {
			throw new ValidationException("User id not found");
		}
		if (roles.contains("Project Manager")) {
			tuples = projectProductDao
					.countProductAndSumLicenseCountByStatusesAndProjectProjectManagerIdAndActiveAndGroupByCustomerEmail(
							getUserId(), Arrays.asList(ProjectProductStatus.APPROVED, ProjectProductStatus.RENEWED),
							true);
		} else {
			tuples = projectProductDao.countProductAndSumLicenseCountByStatusesAndActiveAndGroupByCustomerEmail(
					Arrays.asList(ProjectProductStatus.APPROVED, ProjectProductStatus.RENEWED), true);
		}
		List<ProjectProductGraphResponse> responseList = new ArrayList<>();
		tuples.forEach(tuple -> {
			responseList.add(new ProjectProductGraphResponse(null,
					tuple.get("customerName", String.class)
							.concat(String.format("(%s)", tuple.get("customerEmail", String.class))),
					tuple.get("productCount", Long.class), tuple.get("licenseCount", Long.class)));
		});
		return responseList;
	}

	@Override
	@Secured(AuthorityUtils.REPORT_FETCH)
	public List<ProjectProductResponse> findByApprovedStatusAndCustomerEmail(String email) {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProducts;
		if (getUserId() == null) {
			throw new ValidationException("User id not found");
		}
		if (roles.contains("Customer") && !user.getEmail().equalsIgnoreCase(email)) {
			return new ArrayList<>();
		}
		if (roles.contains("Project Manager")) {
			projectProducts = projectProductDao
					.findByProjectProjectManagerIdAndProjectCustomerEmailAndActiveAndStatusIn(getUserId(), email, true,
							Arrays.asList(ProjectProductStatus.APPROVED, ProjectProductStatus.RENEWED));
		} else {
			projectProducts = projectProductDao.findByProjectCustomerEmailAndActiveAndStatusIn(email, true,
					Arrays.asList(ProjectProductStatus.APPROVED, ProjectProductStatus.RENEWED));
		}
		List<ProjectResponse> projects = projectDao.findByCustomerEmailAndActive(email, true);
		Map<Long, ProjectResponse> projectLookup = new HashMap<>();
		projects.forEach(project -> {
			projectLookup.putIfAbsent(project.getId(), project);
		});
		projectProducts.forEach(projectProduct -> {
			projectProduct.setProject(projectLookup.get(projectProduct.getProjectId()));
			projectProduct
					.setProductDetail(productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
		});
		return projectProducts;
	}

	@Override
	@Secured(AuthorityUtils.REPORT_FETCH)
	public Resource findExcelByApprovedStatusAndCustomerEmail(String email) {
		List<ProjectProductResponse> projectProducts = findByApprovedStatusAndCustomerEmail(email);
		String fileName = excelDirectory + String.format("Report(%s).xlsx", email);
		createExcel(projectProducts, fileName, "Product Report For " + email);
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.REPORT_FETCH)
	public Resource findPdfByApprovedStatusAndCustomerEmail(String email) {
		List<ProjectProductResponse> projectProducts = findByApprovedStatusAndCustomerEmail(email);
		String fileName = pdfDirectory + String.format("Report(%s).pdf", email);
		createPdf(projectProducts, fileName, "Product report for " + email);
		return FileStorageService.fetchFile(fileName);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!projectProductDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		if (licenseDao.existsByProjectProductIdAndActive(unmaskId, true)) {
			throw new ValidationException(String.format(
					"Project product (%s) can't be delete as some of the project product have got the licenses ", id));
		}
		List<Long> projectProductCommentIds = projectProductCommentDao.findAllIdsByProjectProductIdAndActive(unmaskId,
				true);
		if (!projectProductCommentIds.isEmpty()) {
			projectProductCommentDao.delete(projectProductCommentIds, getUserId(), new Date());
		}
		int rows = projectProductDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("project product {} successfully deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Project product successfully deleted");

	}

}
