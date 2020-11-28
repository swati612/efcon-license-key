package com.nxtlife.efkon.license.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.UserService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.user.security.PasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Forgot password", description = "Forgot password api for user")
public class BasicController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/forgot-password/{username}", produces = { "application/json" })
	@Operation(summary = "Forgot password", description = "This api used to sent generated password if you user forgot password", tags = {
			"Forgot password" })
	@ApiResponses(value = {
			@ApiResponse(description = "Generated password successfully sent", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "Username not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "Username pattern didn't match or email or contact not registered with us", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse forgotPassword(@PathVariable String username) {
		return userService.forgotPassword(username);
	}

	@PostMapping(value="/forgot-password", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Forgot password", description = "This api used to sent generated password if you user forgot password", tags = {
			"Forgot password" })
	@ApiResponses(value = {
			@ApiResponse(description = "Password changed successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "Generated password didn't match", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse forgotPassword(@Valid @RequestBody PasswordRequest request) {
		return userService.changePasswordByGeneratedPassword(request);
	}

}
