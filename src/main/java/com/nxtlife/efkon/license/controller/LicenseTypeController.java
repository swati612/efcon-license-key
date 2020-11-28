package com.nxtlife.efkon.license.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.LicenseTypeService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseTypeRequest;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "LicenseType", description = "License type apis")
@RequestMapping("/api/")
public class LicenseTypeController {

	@Autowired
	private LicenseTypeService licenseTypeService;

	@GetMapping(value = "license/types/all", produces = { "application/json" })
	@Operation(summary = "Find all license types", description = "return a list of license types", tags = {
			"LicenseType", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License types successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseTypeResponse> findAll() {
		return licenseTypeService.findAll();

	}

	@GetMapping(value = "license/types", produces = { "application/json" })
	@Operation(summary = "Find all activated license types", description = "return a list of activated license types", tags = {
			"LicenseType", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License types successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseTypeResponse> findAllActivated() {
		return licenseTypeService.findAllActivated();

	}

	@PutMapping(value = "license/type/{id}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Update license type", description = "success message after updating license type", tags = {
			"LicenseType" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License types updated successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to update project type", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse update(@PathVariable("id") Long id, @Valid @RequestBody LicenseTypeRequest request) {
		return licenseTypeService.update(id, request.getMonthCount());

	}

}
