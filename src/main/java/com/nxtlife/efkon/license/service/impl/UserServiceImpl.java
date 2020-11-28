package com.nxtlife.efkon.license.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nxtlife.efkon.license.dao.jpa.AuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleAuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserRoleJpaDao;
import com.nxtlife.efkon.license.entity.common.RoleAuthorityKey;
import com.nxtlife.efkon.license.entity.common.UserRoleKey;
import com.nxtlife.efkon.license.entity.user.Authority;
import com.nxtlife.efkon.license.entity.user.Role;
import com.nxtlife.efkon.license.entity.user.RoleAuthority;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.entity.user.UserRole;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.UserService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.user.UserRequest;
import com.nxtlife.efkon.license.view.user.UserResponse;
import com.nxtlife.efkon.license.view.user.security.AuthorityResponse;
import com.nxtlife.efkon.license.view.user.security.PasswordRequest;
import com.nxtlife.efkon.license.view.user.security.RoleResponse;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserDetailsService, UserService {

	@Autowired
	private UserJpaDao userJpaDao;

	@Autowired
	private RoleJpaDao roleDao;

	@Autowired
	private UserRoleJpaDao userRoleJpaDao;

	@Autowired
	private AuthorityJpaDao authorityDao;

	@Autowired
	private RoleAuthorityJpaDao roleAuthorityDao;

	private static PasswordEncoder userPasswordEncoder = new BCryptPasswordEncoder();

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

	@PostConstruct
	public void init() {

		Role role;
		if ((role = roleDao.findByName("SuperAdmin")) == null) {
			role = new Role("SuperAdmin");
		}
		if (userJpaDao.findByUsername("ajay") == null) {
			User user = new User("ajay", userPasswordEncoder.encode("12345"), null);
			user.setName("Ajay");
			user.setUserRoles(Arrays.asList(new UserRole(new UserRoleKey(role.getId(), user.getId()), role, user)));
			userJpaDao.saveAll(Arrays.asList(user));
			List<Authority> authorities = authorityDao.findAll();
			for (Authority authority : authorities) {
				roleAuthorityDao.save(
						new RoleAuthority(new RoleAuthorityKey(role.getId(), authority.getId()), role, authority));
			}
		}

	}

	/**
	 * this method used to validate role ids that these role ids exist in database
	 *
	 * @param requestRoleIds
	 */
	private void validateRoleIds(Set<Long> requestRoleIds) {
		List<Long> roleIds = roleDao.getAllIdsByActive(true);
		if (!roleIds.containsAll(requestRoleIds)) {
			requestRoleIds.removeAll(roleIds);
			throw new ValidationException(String.format("Some of the roles (%s) are not valid or not active",
					StringUtils.arrayToCommaDelimitedString(requestRoleIds.toArray())));

		}
	}

	/**
	 * this method used to validate user request like username already exist or not
	 *
	 * @param request
	 */
	private void validate(UserRequest request) {
		if (userJpaDao.existsByUsername(request.getUsername())) {
			throw new ValidationException(String.format("This user (%s) already exist", request.getUsername()));

		} else if (userJpaDao.findIdByEmailAndActive(request.getEmail(), true) != null) {
			throw new ValidationException(String.format("This user's email (%s) already exists", request.getEmail()));
		}
		validateRoleIds(request.getRoleIds());
	}

	/**
	 * this method used to fetch user response using user entity and roleIds
	 *
	 * @param user
	 * @param roleIds
	 * @return {@link UserResponse}
	 */
	private UserResponse fetch(User user, Set<Long> roleIds) {
		UserResponse response = UserResponse.get(user);
		response.setRoles(new HashSet<>());
		RoleResponse roleResponse;
		for (Long roleId : roleIds) {
			roleResponse = roleDao.findResponseById(roleId);
			roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(roleId));
			response.getRoles().add(roleResponse);
		}
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_CREATE)
	public UserResponse save(UserRequest request) {
		validate(request);
		User user = request.toEntity();
		if (user.getIsEmailSend() && request.getEmail() == null) {
			throw new ValidationException("Email can't be empty");
		}
		user.setPassword(userPasswordEncoder.encode("12345"));
		user.setCode(String.format("%04d", sequenceGenerator("User")));
		user = userJpaDao.save(user);
		Long customerRoleId = roleDao.findIdByName("Customer");
		if (customerRoleId != null && request.getRoleIds().contains(customerRoleId)) {
			throw new ValidationException("You can't create customer");
		}
		for (Long roleId : request.getRoleIds()) {
			userRoleJpaDao.save(user.getId(), roleId);
		}
		return fetch(user, request.getRoleIds());
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public List<UserResponse> findAll() {
		List<UserResponse> userResponseList = new ArrayList<>();
		userJpaDao.findAll().stream()
				.forEach(user -> userResponseList.add(new UserResponse(user.getId(), user.getName(), user.getCode(),
						user.getActive(), user.getUsername(), user.getEmail(), user.getContactNo())));
		List<Long> roleIds = roleDao.getAllIdsByActive(true);
		Map<Long, List<AuthorityResponse>> roleAuthoritiesMap = new HashMap<>();
		for (Long roleId : roleIds) {
			roleAuthoritiesMap.put(roleId, authorityDao.findByAuthorityRolesRoleId(roleId));
		}
		for (UserResponse user : userResponseList) {
			user.setRoles(roleDao.findByRoleUsersUserId(unmask(user.getId())));
			for (RoleResponse role : user.getRoles()) {
				role.setAuthorities(roleAuthoritiesMap.get(unmask(role.getId())));
			}
		}
		return userResponseList;
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public UserResponse findById(Long userId) {
		Long unmaskId = unmask(userId);
		UserResponse user = userJpaDao.findResponseById(unmaskId);
		user.setRoles(roleDao.findByRoleUsersUserId(unmaskId));
		for (RoleResponse role : user.getRoles()) {
			role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(unmask(role.getId())));
		}
		return user;
	}

	/**
	 * this method used to fetch user response using roleId this method is used when
	 * we want to get al the names and email ids of the user using particular role
	 *
	 * @param roleId
	 * @return {@link <tt>UserResponse</tt>}
	 */

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public List<UserResponse> findAll(Long roleId) {
		Long unmaskRoleId = unmask(roleId);
		if (!roleDao.existsById(unmaskRoleId)) {
			throw new NotFoundException(String.format("Role (%s) not found", roleId));
		}
		List<UserResponse> userResponseList = userJpaDao.findByUserRolesRoleId(roleId);
		return userResponseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_MANAGER_FETCH)
	public Set<UserResponse> findAllProjectManagers() {
		Set<UserResponse> userResponseList = new HashSet<>(userJpaDao.findByUserRolesRoleName("Project Manager"));
		return userResponseList;
	}

	@Override
	@Secured(AuthorityUtils.CUSTOMER_FETCH)
	public Set<UserResponse> findAllCustomers() {
		Set<UserResponse> userResponseList = new HashSet<>(userJpaDao.findByUserRolesRoleName("Customer"));
		return userResponseList;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userJpaDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("username(%s) not found", username));
		}
		user.setUserId(user.getId());
		Set<Authority> authorities = new HashSet<>();
		Set<Role> roles = new HashSet<>();
		for (UserRole userRole : user.getUserRoles()) {
			for (RoleAuthority roleAuthority : userRole.getRole().getRoleAuthorities()) {
				authorities.add(roleAuthority.getAuthority());
				roles.add(roleAuthority.getRole());
			}
		}
		user.setAuthorities(authorities);
		user.setRoles(roles);
		return user;
	}

	@Override
	public UserResponse findByAuthentication() {
		User user = getUser();
		if (user == null) {
			throw new NotFoundException("User not found");
		}
		UserResponse response = UserResponse.get(user);
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public UserResponse update(Long userId, UserRequest request) {
		Long unmaskUserId = unmask(userId);
		Boolean requestBody = false;
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			validateRoleIds(request.getRoleIds());
			requestBody = true;
		}
		if (request.getContactNo() == null && request.getEmail() == null && request.getName() == null && !requestBody) {
			throw new ValidationException("Request body is not valid");
		}
		Long id;
		Optional<User> user = userJpaDao.findById(unmaskUserId);
		if (!user.isPresent()) {
			throw new NotFoundException(String.format("User (%s) not found", userId));
		}
		if (request.getContactNo() != null) {
			if (request.getContactNo().length() != 10 || !Pattern.matches("^[0-9]*$", request.getContactNo())) {
				throw new ValidationException("Contact number value is not correct");
			}

			user.get().setContactNo(request.getContactNo());
		}
		if (request.getEmail() != null) {
			id = userJpaDao.findIdByEmailAndActive(request.getEmail(), true);
			if (id != null && !id.equals(unmaskUserId)) {
				throw new ValidationException(String.format("This email (%s) already exists", request.getEmail()));
			}
			user.get().setEmail(request.getEmail());
		}
		if (request.getName() != null) {
			user.get().setName(request.getName());
		}
		userJpaDao.save(user.get());
		Set<Long> roleIds = userRoleJpaDao.findRoleIdsByUserId(unmaskUserId);
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			Set<Long> requestedRoleIds = new HashSet<>(request.getRoleIds());
			Set<Long> newRoles = request.getRoleIds();
			newRoles.removeAll(roleIds);
			for (Long roleId : newRoles) {
				userRoleJpaDao.save(unmaskUserId, roleId);
			}
			roleIds.removeAll(requestedRoleIds);
			for (Long roleId : roleIds) {
				userRoleJpaDao.delete(unmaskUserId, roleId);
			}
		}
		return fetch(user.get(), request.getRoleIds() == null ? roleIds : request.getRoleIds());
	}

	@Override
	public SuccessResponse forgotPassword(String username) {
		if (!username.matches("^[@A-Za-z0-9_]{3,20}$")) {
			throw new ValidationException(String.format("Incorrect username [%s]", username));
		}
		Map<String, String> response = userJpaDao.findEmailAndContactByUsername(username);
		if (response == null) {
			throw new NotFoundException(String.format("User[username-%s] not found", username));
		}
		if (response.get("email") == null && response.get("contactNo") == null) {
			throw new ValidationException("User email/contact not register with us");
		}
		String generatedPassword = UUID.randomUUID().toString().substring(0, 6);
		logger.info("Password {} has been sent to email {}/contact {}", generatedPassword, response.get("email"),
				response.get("contactNo"));
		userJpaDao.setGeneratedPassword(username, userPasswordEncoder.encode(generatedPassword));
		return new SuccessResponse(HttpStatus.OK.value(),
				"New generated password has been sent to your email and contact number");
	}

	@Override
	public SuccessResponse matchGeneratedPassword(String username, String generatedPassword) {
		String encodedGeneratedPassword = userJpaDao.findGeneratedPasswordByUsername(username);
		if (encodedGeneratedPassword == null) {
			throw new NotFoundException(String.format("User[username-%s] not found or password already set", username));
		}
		if (!userPasswordEncoder.matches(generatedPassword, encodedGeneratedPassword)) {
			throw new ValidationException(String.format("Sent generated password[%s] incorrect", generatedPassword));
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Generated password matched");
	}

	@Override
	public SuccessResponse changePasswordByGeneratedPassword(PasswordRequest request) {
		request.checkGeneratedPassword();
		SuccessResponse response = matchGeneratedPassword(request.getUsername(), request.getGeneratedPassword());
		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new ValidationException("Generated password didn't match");
		}
		int rows = userJpaDao.setPassword(request.getUsername(), userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	public SuccessResponse changePassword(PasswordRequest request) {
		request.checkPassword();
		String encodedPassword = userJpaDao.findPasswordById(getUserId());
		if (encodedPassword == null) {
			throw new NotFoundException(
					String.format("User[id-%s] not found or password already set", mask(getUserId())));
		}
		if (!userPasswordEncoder.matches(request.getOldPassword(), encodedPassword)) {
			throw new ValidationException(String.format("Old password[%s] incorrect", request.getOldPassword()));
		}
		int rows = userJpaDao.setPassword(getUserId(), userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public SuccessResponse activate(Long id) {
		Long unmaskId = unmask(id);
		if (!userJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		if (userRoleJpaDao.findRoleIdsByUserIdAndRoleActive(unmaskId, false).size() > 0) {
			throw new ValidationException("Some of the roles are not active for this user");
		}
		int rows = userJpaDao.activate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} activated successfully", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User activated successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!userJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		int rows = userJpaDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} deleted successfully", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User deleted successfully");
	}

}
