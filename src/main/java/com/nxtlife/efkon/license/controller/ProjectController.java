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
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProjectService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectRequest;
import com.nxtlife.efkon.license.view.project.ProjectResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Project", description = "Project api's for fetch, save ,update and delete the project")
@RequestMapping("/api/")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PostMapping(value = "project", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save project detail ", description = "return project response after saved the project details", tags = {
			"Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project response after successfully saved the product detail", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectResponse save(
			@Parameter(description = "Project details which need to be saved") @Valid @RequestBody ProjectRequest request) {
		return projectService.save(request);

	}

	@PutMapping(value = "project/{id}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "update project detail ", description = "return project response after updating the project details", tags = {
			"Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project response after successfully updating the product detail", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectResponse update(@PathVariable Long id, @RequestBody ProjectRequest request) {
		return projectService.update(id, request);
	}

	@GetMapping(value = "projects", produces = { "application/json" })
	@Operation(summary = "Find all project", description = "return a list of projects", tags = { "Project" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch projects ", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectResponse> findAll() {
		return projectService.findAll();
	}

	@GetMapping(value = "project/{id}", produces = { "application/json" })
	@Operation(summary = "fetch project detail", description = "return project response", tags = { "Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project response", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch Project details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If Project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project doesn't exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectResponse findById(@PathVariable Long id) {
		return projectService.findById(id);
	}

	@DeleteMapping(value = "project/{id}", produces = { "application/json" })
	@Operation(summary = "Delete project detail", description = "return success response after successfully deleting the project", tags = {
			"Project" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductLicenseRequestResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete Project ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If Project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@PathVariable Long id) {
		return projectService.delete(id);
	}

}
