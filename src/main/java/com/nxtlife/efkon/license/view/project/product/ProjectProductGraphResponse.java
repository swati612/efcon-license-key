package com.nxtlife.efkon.license.view.project.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProjectProductGraphResponse {

	@Schema(description = "Status of product", example = "APPROVED")
	private String status;

	@Schema(description = "No of product count by status", example = "4")
	private Long count;

	@Schema(description = "name of status", example = "PENDING")
	private String name;

	private Long productCount;
	private Long licenseCount;

	public ProjectProductGraphResponse() {
	}

	public ProjectProductGraphResponse(String status, Long count) {
		this.status = status;
		this.count = count;
	}

	public ProjectProductGraphResponse(ProjectProductStatus status, Long count) {
		this.status = status.name();
		this.count = count;
	}

	public ProjectProductGraphResponse(String status, String name, Long productCount, Long licenseCount) {
		super();
		this.status = status;
		this.name = name;
		this.productCount = productCount;
		this.licenseCount = licenseCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	public Long getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(Long licenseCount) {
		this.licenseCount = licenseCount;
	}

}
