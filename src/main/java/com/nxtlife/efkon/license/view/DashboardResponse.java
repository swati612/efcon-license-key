package com.nxtlife.efkon.license.view;

import com.nxtlife.efkon.license.enums.ProjectProductStatus;

public class DashboardResponse {

	private String name;
	private ProjectProductStatus status;
	private Long productCount;
	private Long licenseCount;

	public DashboardResponse(String name, ProjectProductStatus status, Long productCount, Long licenseCount) {
		super();
		this.name = name;
		this.status = status;
		this.productCount = productCount;
		this.licenseCount = licenseCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectProductStatus status) {
		this.status = status;
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
