package com.nxtlife.efkon.license.entity.license;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.enums.LicenseStatus;

@SuppressWarnings("serial")
@Entity
@Table(name = "license")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class License extends BaseEntity implements Serializable {

	@NotNull(message = "code can't be null")
	private String code;

	private String accessId;

	@Column(unique = true)
	private String generatedKey;

	private String name;

	@NotNull(message = "status can't be null")
	private LicenseStatus status;

	@ManyToOne
	private ProjectProduct projectProduct;

	public License() {
		super();
	}

	public License(@NotNull(message = "code can't be null") String code,
			@NotNull(message = "status can't be null") LicenseStatus status, Long projectProductId) {
		super();
		this.code = code;
		this.status = status;
		if (projectProductId != null) {
			this.projectProduct = new ProjectProduct();
			this.projectProduct.setId(projectProductId);
		}
	}

	public License(@NotNull(message = "code can't be null") String code,
			@NotNull(message = "access_id can't be null") String accessId, String generatedKey, String name,
			@NotNull(message = "status can't be null") LicenseStatus status, Long projectProductId) {
		super();
		this.code = code;
		this.accessId = accessId;
		this.generatedKey = generatedKey;
		this.name = name;
		this.status = status;
		if (projectProductId != null) {
			this.projectProduct = new ProjectProduct();
			this.projectProduct.setId(projectProductId);
		}

	}

	public License(@NotNull(message = "code can't be null") String code,
			@NotNull(message = "access_id can't be null") String accessId, String generatedKey, String name,
			@NotNull(message = "status can't be null") LicenseStatus status) {
		super();
		this.code = code;
		this.accessId = accessId;
		this.generatedKey = generatedKey;
		this.name = name;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getGeneratedKey() {
		return generatedKey;
	}

	public void setGeneratedKey(String generatedKey) {
		this.generatedKey = generatedKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LicenseStatus getStatus() {
		return status;
	}

	public void setStatus(LicenseStatus status) {
		this.status = status;
	}

	public ProjectProduct getProjectProduct() {
		return projectProduct;
	}

	public void setProjectProduct(ProjectProduct projectProduct) {
		this.projectProduct = projectProduct;
	}

}
