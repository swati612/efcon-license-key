package com.nxtlife.efkon.license.view.license;

import javax.validation.constraints.NotNull;

public class LicenseTypeRequest {

	@NotNull(message = "month count can't be null")
	private Integer monthCount;

	public Integer getMonthCount() {
		return monthCount;
	}

}
