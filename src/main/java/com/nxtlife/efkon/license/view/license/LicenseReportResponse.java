package com.nxtlife.efkon.license.view.license;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.view.Response;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class LicenseReportResponse implements Response {

	private String customerName;

	private String productFamily;

	private String code;

	private String licenseType;

	private Integer expiration;

	private String startDate;

	private String endDate;

	private Integer totalLicenseDistributed;

	public LicenseReportResponse() {

	}

	public LicenseReportResponse(String customerName, String productFamily, String code, String licenseType,
			String startDate, String endDate, Integer expiration, Integer totalLicenseDistributed) {
		this.customerName = customerName;
		this.productFamily = productFamily;
		this.code = code;
		this.licenseType = licenseType;
		this.expiration = expiration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalLicenseDistributed = totalLicenseDistributed;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductFamily() {
		return productFamily;
	}

	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public Integer getExpiration() {
		return expiration;
	}

	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalLicenseDistributed() {
		return totalLicenseDistributed;
	}

	public void setTotalLicenseDistributed(Integer totalLicenseDistributed) {
		this.totalLicenseDistributed = totalLicenseDistributed;
	}

}
