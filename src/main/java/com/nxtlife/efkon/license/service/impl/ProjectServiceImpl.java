package com.nxtlife.efkon.license.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.LicenseJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectTypeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserRoleJpaDao;
import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.entity.user.UserRole;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProjectService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectRequest;
import com.nxtlife.efkon.license.view.project.ProjectResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;
import com.nxtlife.efkon.license.view.user.UserResponse;

@Service("projectServiceImpl")
@Transactional
public class ProjectServiceImpl extends BaseService implements ProjectService {

	private static PasswordEncoder userPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private ProjectJpaDao projectDao;

	@Autowired
	private ProjectTypeJpaDao projectTypeDao;

	@Autowired
	private ProjectProductJpaDao projectProductDao;

	@Autowired
	private LicenseJpaDao licenseDao;

	@Autowired
	private RoleJpaDao roleDao;

	@Autowired
	private UserJpaDao userDao;

	@Autowired
	private UserRoleJpaDao userRoleDao;

	private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	/**
	 * this method used to validate request. In this we are validating that project
	 * type and project manager are valid
	 * 
	 * @param request
	 */
	private void validate(ProjectRequest request) {
		if (request.getProjectTypeId() != null) {
			if (!projectTypeDao.existsById(request.getProjectTypeId())) {
				throw new ValidationException(
						String.format("Project type (%s) not found", mask(request.getProjectTypeId())));
			}
		}

		if (request.getProjectManagerId() != null) {
			if (!userRoleDao.existsByUserIdAndRoleName(request.getProjectManagerId(), "Project Manager")) {
				throw new ValidationException(
						String.format("Project manager doesn't exists", mask(request.getProjectManagerId())));
			}
		}

	}

	@Override
	@Secured(AuthorityUtils.PROJECT_CREATE)
	public ProjectResponse save(ProjectRequest request) {
		validate(request);
		UserResponse existUser = userDao.findByEmailAndActive(request.getCustomerEmail(), true);
		Long customerRoleId = roleDao.findIdByName("Customer");
		if (customerRoleId == null) {
			throw new ValidationException("Customer role not created please ask admin to generate role for customer");
		}
		if (existUser != null) {
			Set<Long> roleIds = userRoleDao.findRoleIdsByUserId(unmask(existUser.getId()));
			roleIds.remove(customerRoleId);
			if (!roleIds.isEmpty()) {
				throw new ValidationException(
						"This email id already used for management that's why you can't use this as a customer");
			}
		}
		User user = null;
		Project project = request.toEntity();
		if (existUser == null) {
			String code = String.format("%04d", sequenceGenerator("User"));
			user = userDao.save(new User(request.getCustomerName(), code, request.getCustomerEmail(),
					userPasswordEncoder.encode("12345"), request.getCustomerEmail(), project.getIsEmailSend(),
					request.getCustomerContactNo()));
			userRoleDao.save(new UserRole(user.getId(), customerRoleId));
		}
		project.setCustomerCode(user == null ? existUser.getCode() : user.getCode());
		projectDao.save(project);
		ProjectResponse response = projectDao.findResponseById(project.getId());
		return response;

	}

	@Override
	@Secured(AuthorityUtils.PROJECT_UPDATE)
	public ProjectResponse update(Long id, ProjectRequest request) {
		Long unmaskId = unmask(id);
		ProjectResponse response = projectDao.findByIdAndActive(unmaskId, true);
		if (response == null) {
			throw new NotFoundException(String.format("project having id (%s) didn't exist", id));
		}
		if (request.getProjectManagerId() != null) {
			if (!userRoleDao.existsByUserIdAndRoleName(request.getProjectManagerId(), "Project Manager")) {
				throw new ValidationException(
						String.format("Project manager doesn't exists", mask(request.getProjectManagerId())));
			}
		}
		int rows = projectDao.update(unmaskId,
				request.getCustomerContactNo() == null ? response.getCustomerContactNo()
						: request.getCustomerContactNo(),
				request.getIsEmailSend() == null ? response.getIsEmailSend() : request.getIsEmailSend(),
				request.getCustomerName() == null ? response.getCustomerName() : request.getCustomerName(),
				request.getProjectManagerId() == null ? unmask(response.getProjectManagerId())
						: request.getProjectManagerId(),
				getUserId(), new Date());

		if (rows > 0) {
			logger.info("Project updated successfully", unmaskId);
		}
		response = projectDao.findByIdAndActive(unmaskId, true);
		return response;
	}

	/**
	 * return a list of projects which is active
	 *
	 * @return List of <tt>ProjectResponse</tt>
	 */
	@Override
	@Secured(AuthorityUtils.PROJECT_FETCH)
	public List<ProjectResponse> findAll() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectResponse> projects;
		List<Tuple> projectProductCounts;
		if (roles.contains("Customer")) {
			projects = projectDao.findByCustomerEmailAndActive(user.getEmail(), true);
			projectProductCounts = projectProductDao
					.findProjectIdAndProductCountByProjectCustomerEmailAndActiveAndProjectProductStatus(user.getEmail(),
							true, ProjectProductStatus.APPROVED);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projects = projectDao.findByProjectManagerIdAndActive(user.getUserId(), true);
				projectProductCounts = projectProductDao
						.findProjectIdAndProductCountByProjectProjectManagerIdAndActiveAndProjectProductStatusNotEq(
								user.getUserId(), true, ProjectProductStatus.DRAFT);
				projectProductCounts.addAll(projectProductDao
						.findProjectIdAndProductCountByProjectProjectManagerIdAndActiveAndProjectProductStatus(
								user.getUserId(), true, ProjectProductStatus.DRAFT));
			} else {
				projects = projectDao.findByActive(true);
				projectProductCounts = projectProductDao
						.findProjectIdAndProductCountByActiveAndNotEqProjectProductStatus(true,
								ProjectProductStatus.DRAFT);
				projectProductCounts.addAll(
						projectProductDao.findProjectIdAndProductCountByActiveAndProjectProductStatus(user.getUserId(),
								true, ProjectProductStatus.DRAFT));
			}
		}
		Map<Long, Long> projectProductCountLookup = new HashMap<>();
		Map<Long, Long> approvedprojectProductCountLookup = new HashMap<>();
		projectProductCounts.forEach(projectProductCount -> {
			System.out.println("Project : " + projectProductCount.get("projectId", Long.class) + " Product : "
					+ projectProductCount.get("productCount", Long.class));
			if (projectProductCountLookup.containsKey(projectProductCount.get("projectId", Long.class))) {
				projectProductCountLookup.put(projectProductCount.get("projectId", Long.class),
						projectProductCountLookup.get(projectProductCount.get("projectId", Long.class))
								+ (projectProductCount.get("productCount", Long.class) != null
										? projectProductCount.get("productCount", Long.class)
										: 0l));
			} else {
				projectProductCountLookup.put(projectProductCount.get("projectId", Long.class),
						projectProductCount.get("productCount", Long.class) != null
								? projectProductCount.get("productCount", Long.class)
								: 0l);
			}
			if (approvedprojectProductCountLookup.containsKey(projectProductCount.get("projectId", Long.class))) {
				approvedprojectProductCountLookup.put(projectProductCount.get("projectId", Long.class),
						approvedprojectProductCountLookup.get(projectProductCount.get("projectId", Long.class))
								+ (projectProductCount.get("approvedProductCount", Long.class) != null
										? projectProductCount.get("approvedProductCount", Long.class)
										: 0l));
			} else {
				approvedprojectProductCountLookup.put(projectProductCount.get("projectId", Long.class),
						projectProductCount.get("approvedProductCount", Long.class) != null
								? projectProductCount.get("approvedProductCount", Long.class)
								: 0l);
			}
		});
		projects.forEach(project -> {
			project.setProductsCount(projectProductCountLookup.get(unmask(project.getId())));
			project.setApprovedProductsCount(approvedprojectProductCountLookup.get(unmask(project.getId())));
		});
		return projects;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_FETCH)
	public ProjectResponse findById(Long id) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectResponse response;
		if (roles.contains("Customer")) {
			response = projectDao.findByIdAndCustomerEmailAndActive(unmaskId, user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				response = projectDao.findByIdAndProjectManagerIdAndActive(unmaskId, user.getUserId(), true);
			} else {
				response = projectDao.findByIdAndActive(unmaskId, true);
			}
		}

		if (response == null) {
			throw new NotFoundException(String.format("project having id (%s) didn't exist", id));
		}

		return response;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!projectDao.existsByIdAndActive(unmaskId, true)) {
			throw new NotFoundException(String.format("Project having id (%s) not found", id));
		}
		List<ProjectProductResponse> ppResponse = projectProductDao.findByProjectIdAndActive(unmaskId, true);
		if (!ppResponse.isEmpty() || ppResponse != null) {
			for (ProjectProductResponse response : ppResponse) {
				licenseDao.deleteByProjectProductId(unmask(response.getId()), getUserId(), new Date());
			}
		}
		projectProductDao.deleteByProjectId(unmaskId, getUserId(), new Date());
		int rows = projectDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Project {} successfuly deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Project  deleted successfully");
	}

}
