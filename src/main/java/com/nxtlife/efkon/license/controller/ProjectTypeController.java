package com.nxtlife.efkon.license.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProjectTypeService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Project Type", description = "Project type api's for fetch the project type")
@RequestMapping("/api/")
public class ProjectTypeController {

	@Autowired
	private ProjectTypeService projectTypeService;

	@PostMapping(value = "project/type", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save project type", description = "return project type after successfully saved", tags = {
			"Project Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project types successfully saved", content = @Content(schema = @Schema(implementation = ProjectTypeResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectTypeResponse save(
			@Parameter(description = "name of project type which you want to add", required = true) @RequestParam(required = true, value = "name") String projectType) {
		return projectTypeService.save(projectType);
	}

	@GetMapping(value = "project/types", produces = { "application/json" })
	@Operation(summary = "Find all project types", description = "return a list of project types", tags = { "Project",
			"Project Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project types successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectTypeResponse> findAll() {
		return projectTypeService.findAll();
	}

	@PutMapping(value = "project/type/reactivate/{id}", consumes = { "application/json" }, produces = {
			"application/json" })
	@Operation(summary = "reactivate project type ", description = "return project type info after reactivating project type", tags = {
			"project type" })
	@ApiResponses(value = {
			@ApiResponse(description = "project type info after reactivating project type", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectTypeResponse.class))),
			@ApiResponse(description = "If user doesn't have access to reactivate project type", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectTypeResponse reactivate(@PathVariable Long id) {
		return projectTypeService.reactivate(id);
	}

	@DeleteMapping(value = "project/type/{id}", produces = { "application/json" })
	@Operation(summary = "Save project type", description = "return project type after successfully deleted", tags = {
			"Project Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project types successfully deleted", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(
			@Parameter(description = "project type id which you want to delete", required = true) @PathVariable Long id) {
		return projectTypeService.delete(id);
	}

}
