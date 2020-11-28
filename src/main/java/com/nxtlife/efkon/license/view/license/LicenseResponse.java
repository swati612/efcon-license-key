package com.nxtlife.efkon.license.view.license;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class LicenseResponse implements Response {

	@Schema(description = "Id of the license", example = "1")
	private Long id;

	private String code;

	@Schema(description = "Unique access Id of the local system", example = "1", required = true)
	private String accessId;

	private String generatedKey;

	@Schema(description = "Name of license", example = "location name", required = true)
	private String name;

	private Long projectProductId;

	private ProjectProductResponse projectProduct;

	public LicenseResponse get(License license) {
		if (license != null) {
			return new LicenseResponse(license.getId(), license.getName(), license.getCode(), license.getAccessId(),
					license.getGeneratedKey(), license.getProjectProduct().getId());
		}
		return null;
	}

	public LicenseResponse(Long id, String name, String code, String accessId, String generatedKey,
			Long projectProductId) {
		super();
		this.id = id;
		this.accessId = accessId;
		this.code = code;
		this.generatedKey = generatedKey;
		this.name = name;
		this.projectProductId = projectProductId;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectProductResponse getProjectProduct() {
		return projectProduct;
	}

	public void setProjectProduct(ProjectProductResponse projectProduct) {
		this.projectProduct = projectProduct;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getGeneratedKey() {
		return generatedKey;
	}

	public void setGeneratedKey(String generatedKey) {
		this.generatedKey = generatedKey;
	}

	public Long getProjectProductId() {
		return mask(projectProductId);
	}

	public void setProjectProductId(Long projectProductId) {
		this.projectProductId = projectProductId;
	}

	public List<String> columnValues() {
		List<String> columnValues = new ArrayList<>();
		columnValues.add(accessId == null ? "NA" : accessId);
		columnValues.add(generatedKey == null ? "NA" : generatedKey);
		return columnValues;

	}

	public List<String> reportColumnValues() {
		List<String> columnValues = new ArrayList<>();
		if (projectProduct == null) {
			columnValues.addAll(
					Arrays.asList("NA", "NA", "NA", "NA", "NA", "NA", generatedKey == null ? "NA" : generatedKey));
		} else if (projectProduct != null && projectProduct.getProductDetail() == null) {
			columnValues.addAll(Arrays.asList("NA", "NA",
					projectProduct.getLicenseTypeName() == null ? "NA" : projectProduct.getLicenseTypeName(),
					projectProduct.getExpirationMonthCount() == null ? "NA"
							: projectProduct.getExpirationMonthCount() + " Months",
					projectProduct.getStartDate() == null ? "NA" : projectProduct.getStartDate(),
					projectProduct.getEndDate() == null ? "NA" : projectProduct.getEndDate(),
					generatedKey == null ? "NA" : generatedKey));
		} else {
			columnValues.addAll(Arrays.asList(
					String.format("%s-%s",
							projectProduct.getProductDetail().getProductFamilyName() == null ? "NA"
									: projectProduct.getProductDetail().getProductFamilyName(),
							projectProduct.getProductDetail().getProductCodeName() == null ? "NA"
									: projectProduct.getProductDetail().getProductCodeName()),
					projectProduct.getProductDetail().getVersionName() == null ? "NA"
							: projectProduct.getProductDetail().getVersionName(),
					projectProduct.getLicenseTypeName() == null ? "NA" : projectProduct.getLicenseTypeName(),
					projectProduct.getExpirationMonthCount() == null ? "NA"
							: projectProduct.getExpirationMonthCount() + " Months",
					projectProduct.getStartDate() == null ? "NA" : projectProduct.getStartDate(),
					projectProduct.getEndDate() == null ? "NA" : projectProduct.getEndDate(),
					generatedKey == null ? "NA" : generatedKey));
		}
		return columnValues;

	}

	public static List<String> licenseColumnHeaders() {
		List<String> columnHeaders = Arrays.asList("Access Id", "Generated Key", "Product Code", "Product Family",
				"Version", "Remark");
		return columnHeaders;
	}

	public static List<String> licenseReportColumnHeaders() {
		List<String> columnHeaders = Arrays.asList("Product Family-Code", "Version", "LicenseType", "Expiration",
				"Start Date", "End Date", "License Key");
		return columnHeaders;
	}

	public static float[] columnWidth() {
		return new float[] { 1.25f, 2.75f, .75f, .75f, .75f, .75f };
	}
}
