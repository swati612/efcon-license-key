package com.nxtlife.efkon.license.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.common.RenewConfiguration;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RenewConfigurationResponse {

	private Integer showBeforeDays;

	private Boolean startDateChange;

	public RenewConfigurationResponse(Integer showBeforeDays, Boolean startDateChange) {
		super();
		this.showBeforeDays = showBeforeDays;
		this.startDateChange = startDateChange;
	}

	public Integer getShowBeforeDays() {
		return showBeforeDays;
	}

	public void setShowBeforeDays(Integer showBeforeDays) {
		this.showBeforeDays = showBeforeDays;
	}

	public Boolean getStartDateChange() {
		return startDateChange;
	}

	public void setStartDateChange(Boolean startDateChange) {
		this.startDateChange = startDateChange;
	}

	public static RenewConfigurationResponse get(RenewConfiguration renewConfiguration) {
		return new RenewConfigurationResponse(renewConfiguration.getShowBeforeDays(),
				renewConfiguration.getStartDateChange());
	}

}
