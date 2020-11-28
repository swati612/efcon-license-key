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
import com.nxtlife.efkon.license.service.ProductDetailService;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailRequest;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Product Detail", description = "product detail api's for save,fetch,update and delete the product detail")
@RequestMapping("/api/")
public class ProductDetailController {

	@Autowired
	public ProductDetailService productDetailService;

	@GetMapping(value = "product/details/all", produces = { "application/json" })
	@Operation(summary = "Find all product details", description = "return list of product details ", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "product details successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductFamilyResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of product details", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProductFamilyResponse> findAll() {
		return productDetailService.findAll();
	}

	@GetMapping(value = "product/details", produces = { "application/json" })
	@Operation(summary = "Find all product details", description = "return list of product details ", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "product details successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductFamilyResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of product details", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProductFamilyResponse> findByActiveTrue() {
		return productDetailService.findByActiveTrue();
	}

	@PostMapping(value = "product/detail", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Save product detail ", description = "return product detail info after saving product detail details", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product detail response after successfully saved the product detail", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save product detail", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or same detail already exist with active status", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProductDetailResponse saveProductDetail(@Valid @RequestBody ProductDetailRequest request) {
		return productDetailService.save(request);
	}

	@PutMapping(value = "product/detail/{id}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Update product detail ", description = "return product detail info after updating product detail details", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product Detail info after updating product  details", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product detail", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled or detail already exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProductDetailResponse update(@PathVariable Long id, @Valid @RequestBody ProductDetailRequest request) {
		return productDetailService.update(id, request);
	}

	@PutMapping(value = "product/detail/{id}/activate", consumes = { "application/json" }, produces = {
			"application/json" })
	@Operation(summary = "Activate product detail ", description = "return success messgae after activating product detail details", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product Detail info after updating product  details", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product detail", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse activate(@PathVariable Long id) {
		return productDetailService.activate(id);
	}

	@DeleteMapping(value = "product/detail/{id}", produces = { "application/json" })
	@Operation(summary = "Delete product detail ", description = "return success response after successfully deleting the project detail", tags = {
			"Product Detail" })
	@ApiResponses(value = {
			@ApiResponse(description = "Product detail successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete product detail", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If product detail id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@PathVariable Long id) {
		return productDetailService.delete(id);
	}

}
