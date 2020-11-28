package com.nxtlife.efkon.license.view.license;

import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class LicenseRequest implements Request {

	@Schema(description = "Unique access Id of the local system", example = "1")
	@NotNull(message = "Access id can't be null")
	private String accessId;

	@Schema(description = "Name of license", example = "License for varanasi")
	private String name;

	public License toEntity() {
		License license = new License();
		license.setName(name);
		return license;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

}
