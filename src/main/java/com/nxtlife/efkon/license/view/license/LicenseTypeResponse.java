package com.nxtlife.efkon.license.view.license;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class LicenseTypeResponse implements Response {

	@Schema(description = "Id of the license type", example = "1")
	private Long id;

	@Schema(description = "name of the license type", example = "1")
	private String name;

	@Schema(description = "Code of license type")
	private String code;

	@Schema(description = "Max month count for license type")
	private Integer maxMonthCount;

	public LicenseTypeResponse(Long id, String name, String code, Integer maxMonthCount) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.maxMonthCount = maxMonthCount;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getMaxMonthCount() {
		return maxMonthCount;
	}

	public void setMaxMonthCount(Integer maxMonthCount) {
		this.maxMonthCount = maxMonthCount;
	}

	public static LicenseTypeResponse get(LicenseType type) {
		if (type != null) {
			return new LicenseTypeResponse(type.getId(), type.getName(), type.getCode(), type.getMaxMonthCount());
		}
		return null;
	}
}
