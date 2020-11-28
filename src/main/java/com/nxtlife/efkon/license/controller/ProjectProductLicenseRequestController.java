package com.nxtlife.efkon.license.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.enums.LicenseRequestStatus;
import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProjectProductLicenseRequestService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductCommentRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Project Product License Request", description = "Project Product License Request api's for fetch,save,update and delete the license requests")
@RequestMapping("/api/")
public class ProjectProductLicenseRequestController {

	@Autowired
	private ProjectProductLicenseRequestService projectProductLicenseRequestService;

	@PostMapping(value = "project/product/{projectProductId}/request", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "Save product license request in project ", description = "return project product license request response after saving the license in project product license request", tags = {
			"Project Product", "Project Product License Request" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response after successfully saved license request in Project Product License Request", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to request Project Product License Request", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductLicenseRequestResponse save(@PathVariable Long projectProductId,
			@Valid @RequestBody ProjectProductLicenseRequestRequest request) {
		return projectProductLicenseRequestService.save(projectProductId, request);
	}

	@PutMapping(value = "project-request/{id}/accept", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product license request status to submit ", description = "return project product license request response after updating details", tags = {
			"Project Product License Request", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response after successfully update the product license request details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product license Request details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product license Request id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or v not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductLicenseRequestResponse accept(@PathVariable Long id,
			@RequestBody ProjectProductRequest request) {
		return projectProductLicenseRequestService.accept(id, LicenseRequestStatus.ACCEPTED, request);
	}

	@PutMapping(value = "project-request/{id}/reject", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product license request status to reject ", description = "return project product license request response after rejecting details", tags = {
			"Project Product License Request", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response after successfully rejecting the product license request details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to reject product license Request details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product license Request id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or projectProduct/productProductLicenseRequest not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductLicenseRequestResponse reject(@PathVariable Long id,
			@RequestBody ProjectProductCommentRequest request) {
		return projectProductLicenseRequestService.reject(id, LicenseRequestStatus.REJECTED, request.getComment());
	}

	@PutMapping(value = "project-request/{id}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "update project product license request in project ", description = "return project product license request response after updating the license in project product license request", tags = {
			"Project Product", "Project Product License Request" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response after successfully updating license request in Project Product License Request", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update Project Product License Request", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductLicenseRequestResponse update(@PathVariable Long id,
			@Valid @RequestBody ProjectProductLicenseRequestRequest request) {
		return projectProductLicenseRequestService.update(id, request);
	}

	@GetMapping(value = "project-request/{id}", produces = { "application/json" })
	@Operation(summary = "fetch project product license request detail ", description = "return product product license response", tags = {
			"Project Product License Request", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch Project Product License Request details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If Project Product License Request id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or projectProductLicenseRequest/Projectproduct not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductLicenseRequestResponse findById(@PathVariable Long id) {
		return projectProductLicenseRequestService.findById(id);
	}

	@GetMapping(value = "project-requests", produces = { "application/json" })
	@Operation(summary = "fetch pending project product license request detail ", description = "return product product license response", tags = {
			"Project Product License Request", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product License Request response", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch Project Product License Request details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If Project Product License Request id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or projectProductLicenseRequest/Projectproduct not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectProductLicenseRequestResponse> findByStatus(@RequestParam LicenseRequestStatus status) {
		return projectProductLicenseRequestService.findByStatus(status);
	}

	@DeleteMapping(value = "project-request/{id}", produces = { "application/json" })
	@Operation(summary = "Delete project product license request detail ", description = "return success response after successfully deleting the product product license request", tags = {
			"Project Product License Request", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "product license request successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete product license", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product license request id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@PathVariable Long id) {
		return projectProductLicenseRequestService.delete(id);
	}
}
