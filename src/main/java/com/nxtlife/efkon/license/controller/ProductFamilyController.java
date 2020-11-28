package com.nxtlife.efkon.license.controller;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProductFamilyService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductFamilyRequest;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Product Family", description = "product family api's for save,fetch,update and delete the product family")
@RequestMapping("/api/")
public class ProductFamilyController {

	@Autowired
	public ProductFamilyService productFamilyService;

	@GetMapping(value = "product/families", produces = { "application/json" })
	@Operation(summary = "Find activated product families", description = "return list of product families ", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "product families successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductFamilyResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of product families", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProductFamilyResponse> findAllActivated() {
		return productFamilyService.findAllActivated();
	}

	@GetMapping(value = "product/families/all", produces = { "application/json" })
	@Operation(summary = "Find all product families", description = "return list of product families ", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "product families successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductFamilyResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of product families", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProductFamilyResponse> findAll() {
		return productFamilyService.findAll();
	}

	@PostMapping(value = "product/family", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Save product family ", description = "return product family info after saving product family details", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product family response after successfully saved the product family", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductFamilyResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save product family", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or product code name already exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProductFamilyResponse saveProductFamily(@Valid @RequestBody ProductFamilyRequest productFamilyRequest) {
		return productFamilyService.save(productFamilyRequest);
	}

	@PutMapping(value = "product/family/{id}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Update product family ", description = "return product family info after updating product family details", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product Family info after updating product family details", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductFamilyResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product family", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or name already exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProductFamilyResponse update(@PathVariable Long id, @RequestBody ProductFamilyRequest request) {
		return productFamilyService.update(id, request);
	}

	@PutMapping(value = "product/family/reactivate/{id}", consumes = { "application/json" }, produces = {
			"application/json" })
	@Operation(summary = "reactivate product family ", description = "return product family info after reactivating product family", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product Family info after reactivating product family", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductFamilyResponse.class))),
			@ApiResponse(description = "If user doesn't have access to reactivate product family", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProductFamilyResponse reactivate(@PathVariable Long id) {
		return productFamilyService.reactivate(id);
	}

	@DeleteMapping(value = "product/family/{id}", produces = { "application/json" })
	@Operation(summary = "Delete product family ", description = "return success response after successfully deleting the project family", tags = {
			"Product Family" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product family successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete product faamily", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If product family id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@PathVariable Long id) {
		return productFamilyService.delete(id);
	}
}
