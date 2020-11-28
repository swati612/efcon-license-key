package com.nxtlife.efkon.license.view.project.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.license.entity.project.product.ProjectProductLicenseRequest;
import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProjectProductLicenseRequestRequest implements Request {

	@Schema(description = "No of licenses", example = "4", required = true)
	@NotNull(message = "License count can't be null")
	@Min(message = "license count can't be less than 1", value = 1)
	private Integer licenseCount;
	
	@Schema(description = "comment", example = "abc")
	private String comment;

	public ProjectProductLicenseRequest toEntity() {
		ProjectProductLicenseRequest pplr = new ProjectProductLicenseRequest();
		pplr.setLicenseCount(licenseCount);
		return pplr;

	}

	public Integer getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(Integer licenseCount) {
		this.licenseCount = licenseCount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
