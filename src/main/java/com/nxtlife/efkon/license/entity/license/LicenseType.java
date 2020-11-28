package com.nxtlife.efkon.license.entity.license;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "license_type")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class LicenseType extends BaseEntity implements Serializable {

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "code can't be null")
	private String code;

	@NotNull(message = "maximum month count can't be null")
	private Integer maxMonthCount;

	public LicenseType() {
		super();
	}

	public LicenseType(@NotNull(message = "name can't be null") String name,
			@NotNull(message = "code can't be null") String code,
			@NotNull(message = "maximum month count can't be null") Integer maxMonthCount) {
		super();
		this.name = name;
		this.code = code;
		this.maxMonthCount = maxMonthCount;
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


}
