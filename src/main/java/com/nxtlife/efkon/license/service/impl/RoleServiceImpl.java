package com.nxtlife.efkon.license.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.AuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleAuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserRoleJpaDao;
import com.nxtlife.efkon.license.entity.user.Role;
import com.nxtlife.efkon.license.entity.user.RoleAuthority;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.RoleService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.user.security.RoleRequest;
import com.nxtlife.efkon.license.view.user.security.RoleResponse;

@Service("roleServiceImpl")
@Transactional
public class RoleServiceImpl extends BaseService implements RoleService {

	private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleJpaDao roleDao;

	@Autowired
	private RoleAuthorityJpaDao roleAuthorityJpaDao;

	@Autowired
	private AuthorityJpaDao authorityDao;

	@Autowired
	private UserRoleJpaDao userRoleJpaDao;

	private void validateAuthorityIds(Set<Long> requestAuthorityIds) {
		List<Long> authorityIds = authorityDao.findAllIds();
		requestAuthorityIds.removeAll(authorityIds);
		if (!requestAuthorityIds.isEmpty()) {
			throw new ValidationException(String.format("Some of the authorities (%s) are not valid",
					requestAuthorityIds.stream().map(id -> mask(id)).collect(Collectors.toSet())));
		}
	}

	/**
	 * this method used to validate request
	 *
	 * @param request
	 */
	private void validateRequest(RoleRequest request) {
		validateAuthorityIds(request.getAuthorityIds());
		if (roleDao.existsByName(request.getName())) {
			throw new ValidationException(String.format(" Role (%s) is already exists", request.getName()));
		}
	}

	@Secured(AuthorityUtils.ROLE_FETCH)
	@Override
	public List<RoleResponse> getAllRoles() {
		List<RoleResponse> roles = new ArrayList<>();
		roleDao.findAll().stream()
				.forEach(role -> roles.add(new RoleResponse(role.getId(), role.getName(), role.getActive())));
		roles.stream().map(role -> {
			role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(unmask(role.getId())));
			return role;
		}).collect(Collectors.toList());
		return roles;
	}

	/**
	 * save the Role <tt>size()</tt>tells no of elements in the list
	 *
	 * <tt>toEntity()</tt> tranform the request into entity
	 * <p>
	 * throw a validation exception when size is less than 1.
	 *
	 * <tt>getUser()</tt>return the user that wil login at that instance
	 *
	 * <findById>return an object by particular id</findById>
	 *
	 *
	 * <saveAll>mehtod save the list of objects in the database</saveAll>
	 *
	 * @return <tt>RoleResponse</tt>
	 * @Param request call save method of jpa
	 */
	@Secured(AuthorityUtils.ROLE_CREATE)
	@Override
	public RoleResponse save(RoleRequest request) {
		validateRequest(request);
		Role role = request.toEntity();
		roleDao.save(role);
		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		for (Long authorityId : request.getAuthorityIds()) {
			if (authorityDao.findResponseById(authorityId) != null) {
				roleAuthorities.add(new RoleAuthority(role.getId(), authorityId));
			}
		}
		roleAuthorityJpaDao.saveAll(roleAuthorities);
		RoleResponse roleResponse = roleDao.findResponseById(role.getId());
		roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(role.getId()));
		return roleResponse;
	}

	@Secured(AuthorityUtils.ROLE_FETCH)
	@Override
	public RoleResponse findById(Long id) {
		Long unmaskId = unmask(id);
		RoleResponse role = roleDao.findResponseById(unmaskId);
		if (role == null) {
			throw new NotFoundException(String.format("Role(%s) not found", id));
		}
		role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(unmask(role.getId())));
		return role;
	}

	@Override
	@Secured(AuthorityUtils.ROLE_UPDATE)
	public RoleResponse update(Long id, RoleRequest request) {
		Long unmaskId = unmask(id);
		RoleResponse role = roleDao.findResponseById(unmaskId);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		if (role.getName().equalsIgnoreCase("SuperAdmin")) {
			throw new ValidationException("SuperAdmin role can't be update");
		}
		validateAuthorityIds(request.getAuthorityIds());
		Long existRoleId = roleDao.findIdByName(request.getName());
		if (existRoleId != null && !existRoleId.equals(unmaskId)) {
			throw new ValidationException("This role is already exists");
		}
		List<Long> requestAuthorityIds = new ArrayList<>(request.getAuthorityIds());
		List<Long> roleAuthorityIds = roleAuthorityJpaDao.getAllAuthorityIdsByRoleId(unmaskId);
		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		if (request.getName() != null && !role.getName().equals(request.getName())) {
			roleDao.updateName(request.getName(), unmaskId, getUserId(), new Date());
		}
		requestAuthorityIds.removeAll(roleAuthorityIds);
		for (Long authorityId : requestAuthorityIds) {
			roleAuthorities.add(new RoleAuthority(unmaskId, authorityId));
		}
		roleAuthorityIds.removeAll(request.getAuthorityIds());
		if (!roleAuthorities.isEmpty())
			roleAuthorityJpaDao.saveAll(roleAuthorities);
		if (!roleAuthorityIds.isEmpty()) {
			roleAuthorityJpaDao.deleteByRoleIdAndAuthorityIds(unmaskId, roleAuthorityIds);
		}
		logger.info("Role {} updated successfully", request.getName());
		RoleResponse roleResponse = roleDao.findResponseById(unmaskId);
		roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(unmaskId));
		return roleResponse;

	}

	@Override
	@Secured(AuthorityUtils.ROLE_UPDATE)
	public SuccessResponse activate(Long id) {
		Long unmaskId = unmask(id);
		RoleResponse role = roleDao.findResponseById(unmaskId);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		int rows = roleDao.activate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Role {} activated successfully", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Role activated successfully");
	}

	@Override
	@Secured(AuthorityUtils.ROLE_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		RoleResponse role = roleDao.findResponseById(unmaskId);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		if (role.getName().equalsIgnoreCase("SuperAdmin")) {
			throw new ValidationException("SuperAdmin role can't be delete");
		}
		Set<Long> userIds = userRoleJpaDao.findUserIdsByRoleIdAndActive(unmaskId, true);
		if (userIds.isEmpty()) {
			int rows = roleDao.delete(unmaskId, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Role {} deleted successfully", unmaskId);
			}
		} else {
			throw new ValidationException("Some of the users are assigned with this role.");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Role deleted successfully");
	}

}
