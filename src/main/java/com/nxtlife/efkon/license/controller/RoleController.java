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
import com.nxtlife.efkon.license.service.RoleService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.user.security.RoleRequest;
import com.nxtlife.efkon.license.view.user.security.RoleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Role", description = "Role api's for fetch ,create and delete the role")
@RequestMapping("/api/")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * return a list of roles available in that organization or that area. if
	 * organization not exist ,it will throw validation exception
	 *
	 * @Param Id
	 * @return List of <tt>RoleResponse</tt>
	 */

	@GetMapping("roles")
	@Operation(summary = "Find all the roles", description = "return a list of roles", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class)))),
			@ApiResponse(responseCode = "404", description = "Page not found"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public List<RoleResponse> getAllRoles() {
		return roleService.getAllRoles();
	}

	@GetMapping(value = "role/{roleId}", produces = { "application/json" })
	@Operation(summary = "Find roles by organization id", description = "return a list of roles by organization id which is taken by currently login user", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
			@ApiResponse(responseCode = "404", description = "role not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "403", description = "don't have access to fetch roles", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RoleResponse findById(@Parameter(description = "Id of the role", required = true) @PathVariable Long roleId) {
		return roleService.findById(roleId);
	}

	
	/**
	 * save the role and return the same if authority not exist ,it will return
	 * empty list
	 *
	 * @param request
	 *            [Key value pair which we want to save that role]
	 * @return RoleResponse
	 */

	@PostMapping(value = "role", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "save the role", description = "Save the role and return the same role as role Response", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
			@ApiResponse(responseCode = "400", description = "If role already exist or authority ids are not valid", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RoleResponse save(@Valid @RequestBody RoleRequest request) {
		return roleService.save(request);
	}

	/**
	 * update the role and return the same role as role response if role not
	 * exist ,it will throw validation exception
	 *
	 * @Param id
	 * @Param request
	 *
	 * @return RoleResponse
	 */
	@PutMapping(value="role/{roleId}", consumes={"application/json"}, produces = "application/json")
	@Operation(summary = "update the role", description = "Update the role and return the same role as role Response", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class)))),
			@ApiResponse(responseCode = "404", description = "Page not found"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public RoleResponse update(@PathVariable Long roleId, @RequestBody RoleRequest request) {
		return roleService.update(roleId, request);
	}
	
	@PutMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "role/{roleId}/activate")
	@Operation(summary = "Activate Role", description = "return success message if role successfully activated", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(description = "Role successfully activated", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to activate role", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If role id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse activate(@Parameter(description = "Role id", required = true) @PathVariable Long roleId) {
		return roleService.activate(roleId);
	}

	@DeleteMapping(produces = { "application/json" }, value = "role/{roleId}")
	@Operation(summary = "Delete Role", description = "return success message if role successfully deleted", tags = {
			"Role" })
	@ApiResponses(value = {
			@ApiResponse(description = "Role successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete role", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If role id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If this role assigned to some of the users or role name is superadmin", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@Parameter(description = "Role id", required = true) @PathVariable Long roleId) {
		return roleService.delete(roleId);
	}


}
