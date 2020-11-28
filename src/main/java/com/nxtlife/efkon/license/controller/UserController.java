package com.nxtlife.efkon.license.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.UserService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.user.UserRequest;
import com.nxtlife.efkon.license.view.user.UserResponse;
import com.nxtlife.efkon.license.view.user.security.PasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User", description = "User api's for fetch, create, delete user, password change and logout")
@RequestMapping("/api/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenStore tokenStore;

	@GetMapping(produces = { "application/json" }, value = "me")
	@Operation(summary = "Find login user info", description = "return user info using access token detail", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
			@ApiResponse(description = "If token is not valid", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse find() {
		return userService.findByAuthentication();
	}

	@PostMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "user")
	@Operation(summary = "Save user details", description = "return user info after saving user details", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info after saving user details", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or role ids not valid or username already exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse save(
			@Parameter(description = "User details", required = true) @Valid @RequestBody UserRequest request) {
		return userService.save(request);
	}

	@GetMapping(produces = { "application/json" }, value = "users")
	@Operation(summary = "Find all user info", description = "return list of user details", tags = { "User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<UserResponse> findAll() {
		return userService.findAll();
	}

	@GetMapping(produces = { "application/json" }, value = "user/{userId}")
	@Operation(summary = "Find user info", description = "return user details", tags = { "User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch list of user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse findById(@PathVariable Long userId) {
		return userService.findById(userId);
	}

	@GetMapping(produces = { "application/json" }, value = "users/{roleId}")
	@Operation(summary = "Find all user by role Id", description = "return list of user details from the given role id", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If role id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<UserResponse> findAllUsersByRoleId(
			@Parameter(description = "Role id", required = true) @PathVariable Long roleId) {
		return userService.findAll(roleId);
	}

	@GetMapping(produces = { "application/json" }, value = "users/project-manager")
	@Operation(summary = "Find all project managers", description = "return list of project manager details", tags = {
			"User", "Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project managers info successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch project managers", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public Set<UserResponse> findAllProjectManagers() {
		return userService.findAllProjectManagers();
	}

	@GetMapping(produces = { "application/json" }, value = "users/customer")
	@Operation(summary = "Find all project customers", description = "return list of customer details", tags = { "User",
			"Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Customers info successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch customers", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public Set<UserResponse> findAllCustomers() {
		return userService.findAllCustomers();
	}

	@PutMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "user/{id}")
	@Operation(summary = "Update user details", description = "return user info after updating user details", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User info after updating user details", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or role ids not valid", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse update(@Parameter(description = "User id", required = true) @PathVariable Long id,
			@Parameter(description = "User details", required = true) @RequestBody UserRequest request) {
		return userService.update(id, request);
	}

	@PutMapping(value = "/me/password", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Change user password using old password", description = "This api used to change password using old password", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "Success message", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user not found with password", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If old password is not correct", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse changePassword(@Valid @RequestBody PasswordRequest request) {
		return userService.changePassword(request);
	}

	@GetMapping(produces = { "application/json" }, value = "/me/logout")
	@Operation(summary = "Logout", description = "This api used to logout", tags = { "User", "Logout" })
	@ApiResponses(value = {
			@ApiResponse(description = "Success message", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))) })
	public SuccessResponse logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Logout successfully");
	}

	@PutMapping(produces = { "application/json" }, value = "user/{userId}/activate")
	@Operation(summary = "Activate User", description = "return success message if user successfully activated", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User successfully activated", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to activate user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If user id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse activate(@Parameter(description = "User id", required = true) @PathVariable Long userId) {
		return userService.activate(userId);
	}

	@DeleteMapping(produces = { "application/json" }, value = "user/{userId}")
	@Operation(summary = "Delete User", description = "return success message if user successfully deleted", tags = {
			"User" })
	@ApiResponses(value = {
			@ApiResponse(description = "User successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If user id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@Parameter(description = "User id", required = true) @PathVariable Long userId) {
		return userService.delete(userId);
	}

}
