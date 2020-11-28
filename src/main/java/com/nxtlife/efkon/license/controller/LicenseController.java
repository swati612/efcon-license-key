package com.nxtlife.efkon.license.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.efkon.license.LicenseManagementApp;
import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.LicenseService;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.license.LicenseRequest;
import com.nxtlife.efkon.license.view.license.LicenseResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductGraphResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "License", description = "License api's for fetch,save,update and delete the license")
@RequestMapping("/api/")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

	@PutMapping(value = "license/{id}/replace", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "replace generated license key of product", description = "return license resonse after generating license key of project product", tags = {
			"license" })
	@ApiResponses(value = {
			@ApiResponse(description = "license response after successfully replace the license key of product in a project", responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "If user doesn't have access to replace license key of product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if license id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if required fileds are not filled properly or license doesn't exist", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))) })
	public LicenseResponse replaceGenerateKey(@PathVariable Long id, @Valid @RequestBody LicenseRequest request) {
		return licenseService.replaceGenerateKey(id, request);

	}

	@PutMapping(value = "license/{id}/generate-key", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "generate license key of product", description = "return license resonse after generating license key of project product", tags = {
			"license" })
	@ApiResponses(value = {
			@ApiResponse(description = "license response after successfully generate the license key of product in a project", responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "If user doesn't have access to generate license key of product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if license id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if required fileds are not filled properly or license doesn't exist", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))) })
	public LicenseResponse generateKey(@PathVariable Long id, @Valid @RequestBody LicenseRequest request) {
		return licenseService.generateKey(id, request);
	}

	@RequestMapping(value = "/license/generate-key/excel-upload", method = RequestMethod.PUT)
	public List<LicenseResponse> generateLicenseKeyFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, value = "projectProductId") Long projectProductId) {
		return licenseService.generateLicenseKeyFromExcel(file, projectProductId);
	}

	@GetMapping(value = "license/generate-key/excel-template")
	public void fetchTemplate(HttpServletResponse response) throws IOException {
		String filepath = LicenseManagementApp.class.getClassLoader().getResource("LicenseTemplate.xlsx").toString();
		Resource resource = new UrlResource(filepath);
		Response.setTemplateResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "licenses", produces = { "application/json" })
	@Operation(summary = "Find all license", description = "return a list of license", tags = { "License" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch license", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findAll() {
		return licenseService.findAll();
	}

	@GetMapping(value = "licenses/excel", produces = { "application/json" })
	@Operation(summary = "Find all licenses and create excel", description = "return a excel file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License excel file successfully created", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch license", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findAllLicensesExcel(HttpServletResponse response) throws IOException {
		Resource resource = licenseService.findAllExcel();
		Response.setFileResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "licenses/pdf", produces = { "application/json" })
	@Operation(summary = "Find all licenses and create pdf", description = "return a pdf file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "pdf file of license", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findAllLicensesPdf(HttpServletResponse response) throws IOException {
		Resource resource = licenseService.findAllPdf();
		Response.setFileResponseHeader(resource, "application/pdf", response);
	}

	@GetMapping(value = "project/{projectId}/product/{productId}/licenses", produces = { "application/json" })
	@Operation(summary = "Find all licenses of a product of a project", description = "return a list of license", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "List of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id or product detail id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findLicensesByProjectIdandProductId(@PathVariable Long projectId,
			@PathVariable Long productId) {
		return licenseService.findByProjectIdAndProductId(projectId, productId);
	}

	@GetMapping(value = "project/{projectId}/product/{productId}/licenses/excel", produces = { "application/json" })
	@Operation(summary = "Find all licenses of product of a project and create excel", description = "return a excel file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "Excel file of license created successfully", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id or product detail id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesByProjectIdandProductIdExcel(@PathVariable Long projectId, @PathVariable Long productId,
			HttpServletResponse response) throws IOException {
		Resource resource = licenseService.findLicensesByProjectIdAndProductIdExcel(projectId, productId);
		Response.setFileResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "project/{projectId}/product/{productId}/licenses/pdf", produces = { "application/json" })
	@Operation(summary = "Find all licenses of product of a project and create pdf", description = "return a pdf file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "pdf file of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesByProjectIdandProductIdPdf(@PathVariable Long projectId, @PathVariable Long productId,
			HttpServletResponse response) throws IOException {
		Resource resource = licenseService.findLicensesByProjectIdAndProductIdPdf(projectId, productId);
		Response.setFileResponseHeader(resource, "application/pdf", response);
	}

	@GetMapping(value = "project/{projectId}/licenses", produces = { "application/json" })
	@Operation(summary = "Find all licenses of a project", description = "return a list of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "List of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findByProjectId(@PathVariable Long projectId) {
		return licenseService.findByProjectId(projectId);
	}

	@GetMapping(value = "project/{projectId}/licenses/excel", produces = { "application/json" })
	@Operation(summary = "Find all licenses of a project and create excel", description = "return a excel file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "Excel file of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesByProjectIdExcel(@PathVariable Long projectId, HttpServletResponse response)
			throws IOException {
		Resource resource = licenseService.findLicensesByProjectIdExcel(projectId);
		Response.setFileResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "project/{projectId}/licenses/pdf", produces = { "application/json" })
	@Operation(summary = "Find all licenses of a project and create pdf", description = "return a pdf file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "pdf file of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesByProjectIdPdf(@PathVariable Long projectId, HttpServletResponse response)
			throws IOException {
		Resource resource = licenseService.findLicensesByProjectIdPdf(projectId);
		Response.setFileResponseHeader(resource, "application/pdf", response);
	}

	@GetMapping(value = "license/{licenseId}", produces = { "application/json" })
	@Operation(summary = "Find license of a product", description = "return a license", tags = { "License" })
	@ApiResponses(value = {
			@ApiResponse(description = "single license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If license id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public LicenseResponse findById(@PathVariable Long licenseId) {
		return licenseService.findById(licenseId);
	}

	@GetMapping(value = "dashboard/license", produces = { "application/json" })
	@Operation(summary = "Find all active licenses", description = "return active license count", tags = { "License" })
	@ApiResponses(value = {
			@ApiResponse(description = "active and expired license count", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectProductGraphResponse> findTotalActiveAndExpiredLicenses() {
		return licenseService.findTotalActiveAndExpiredLicenses();
	}

	@GetMapping(value = "license/report", produces = { "application/json" })
	@Operation(summary = "Report of the licenses by customer email", description = "return a license report", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "license report", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license report ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> licenseReportByEmail(@RequestParam(value = "email") String email) {
		return licenseService.licenseReportByEmail(email);
	}

	@GetMapping(value = "license/report/excel")
	@Operation(summary = "Find licenses report and create excel", description = "return a excel file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesReportExcelByEmail(@RequestParam(value = "email") String email,
			HttpServletResponse response) throws IOException {
		Resource resource = licenseService.findLicensesReportExcelByEmail(email);
		Response.setFileResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "license/report/pdf")
	@Operation(summary = "Find licenses report and create pdf", description = "return a pdf file of licenses", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public void findLicensesReportPdfByEmail(@RequestParam(value = "email") String email, HttpServletResponse response)
			throws IOException {
		Resource resource = licenseService.findLicensesReportPdfByEmail(email);
		Response.setFileResponseHeader(resource, "application/octet-stream", response);

	}

	@GetMapping(value = "project/product/{productId}/generated-license", produces = { "application/json" })
	@Operation(summary = "Find all generated licenses of a product of a project", description = "return generated license", tags = {
			"License" })
	@ApiResponses(value = {
			@ApiResponse(description = "generated licenses", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id or product detail id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findGeneratedLicenses(@PathVariable(value = "productId") Long projectProductId) {
		return licenseService.findGeneratedLicenses(projectProductId);
	}

}
