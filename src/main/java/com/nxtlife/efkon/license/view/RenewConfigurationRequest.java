package com.nxtlife.efkon.license.view;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RenewConfigurationRequest {

	@Min(value = 1, message = "Show before days can't be less than 1")
	@NotNull(message = "Show before days can't be null")
	private Integer showBeforeDays;

	@NotNull(message = "Start date change can't be null")
	private Boolean startDateChange;

	public Integer getShowBeforeDays() {
		return showBeforeDays;
	}

	public Boolean getStartDateChange() {
		return startDateChange;
	}

}
