package com.nxtlife.efkon.license.entity.common;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class RenewConfiguration extends BaseEntity implements Serializable {

	@NotNull(message = "Show before days can't be null")
	private Integer showBeforeDays;

	@NotNull(message = "Start date change can't be null")
	private Boolean startDateChange;

	public RenewConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RenewConfiguration(@NotNull(message = "Show before days can't be null") Integer showBeforeDays,
			@NotNull(message = "Start date change can't be null") Boolean startDateChange) {
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

}
