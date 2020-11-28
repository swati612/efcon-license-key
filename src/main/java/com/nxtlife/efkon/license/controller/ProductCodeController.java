package com.nxtlife.efkon.license.controller;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProductCodeService;
import com.nxtlife.efkon.license.view.product.ProductCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Product Code",description = "product code api's for fetch product code")
@RequestMapping("/api/")
public class ProductCodeController {

    @Autowired
    private ProductCodeService productCodeService;

    @GetMapping(value = "product/codes/{productFamilyId}", produces = {"application/json"})
    @Operation(summary = "Find all product codes by product family id", description = "return list of product codes by given product family id ", tags = {
            "Product Code"})
    @ApiResponses(value = {
            @ApiResponse(description = "product codes successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductCodeResponse.class)))),
            @ApiResponse(description = "If user doesn't have access to fetch list of product codes", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(description = "If product family id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class)))                            })
    public List<ProductCodeResponse> findAllByProductFamilyId(@PathVariable Long productFamilyId) {
        return productCodeService.findAll(productFamilyId);
    }
}
