package com.nxtlife.efkon.license.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.RenewConfigurationService;
import com.nxtlife.efkon.license.view.RenewConfigurationRequest;
import com.nxtlife.efkon.license.view.RenewConfigurationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Renew configuration", description = "Renew configuration api")
@RequestMapping("/api/")
public class RenewConfigurationController {

	@Autowired
	private RenewConfigurationService renewConfigurationService;

	@GetMapping(value = "renew-configuration", produces = { "application/json" })
	@Operation(summary = "Find renew configuration", description = "return renew configuration details", tags = {
			"RenewConfiguration" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Renew Configuration successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenewConfigurationResponse.class)))),
			@ApiResponse(responseCode = "404", description = "If renew configuration not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RenewConfigurationResponse find() {
		return renewConfigurationService.find();

	}

	@PutMapping(value = "renew-configuration", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Update renew configuration", description = "return renew configuration details", tags = {
			"RenewConfiguration" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Renew Configuration successfully updated", content = @Content(schema = @Schema(implementation = RenewConfigurationResponse.class))),
			@ApiResponse(responseCode = "404", description = "If renew configuration not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RenewConfigurationResponse update(@Valid @RequestBody RenewConfigurationRequest request) {
		return renewConfigurationService.update(request);

	}

}
