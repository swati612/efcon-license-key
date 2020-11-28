package com.nxtlife.efkon.license.entity.project.product;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.enums.LicenseRequestStatus;

@SuppressWarnings("serial")
@Entity
@Table(name = "project_product_license_request")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class ProjectProductLicenseRequest extends BaseEntity implements Serializable {

	@NotNull(message = " license count can't be null")
	private Integer licenseCount;

	@NotNull(message = "status can't be null")
	@Enumerated(EnumType.STRING)
	private LicenseRequestStatus status;

	@NotNull(message = "customer email can't be null")
	private String customerEmail;

	@NotNull(message = "project manager can't be null")
	@ManyToOne
	private User projectManager;

	@Transient
	private Long tProjectManagerId;

	@NotNull(message = "project_product can't be null")
	@ManyToOne
	private ProjectProduct projectProduct;

	@Transient
	private Long tProjectProductId;

	@OneToMany(mappedBy = "projectProductLicenseRequest")
	private Set<ProjectProductRequestComment> projectProductRequestComments;

	public ProjectProductLicenseRequest() {
		super();
	}

	public Integer getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(Integer licenseCount) {
		this.licenseCount = licenseCount;
	}

	public ProjectProduct getProjectProduct() {
		return projectProduct;
	}

	public void setProjectProduct(ProjectProduct projectProduct) {
		this.projectProduct = projectProduct;
	}

	public Long gettProjectProductId() {
		return tProjectProductId;
	}

	public void settProjectProductId(Long tProjectProductId) {
		if (tProjectProductId != null) {
			this.projectProduct = new ProjectProduct();
			this.projectProduct.setId(tProjectProductId);
		}
		this.tProjectProductId = tProjectProductId;
	}

	public LicenseRequestStatus getStatus() {
		return status;
	}

	public void setStatus(LicenseRequestStatus status) {
		this.status = status;
	}

	public Set<ProjectProductRequestComment> getProjectProductRequestComments() {
		return projectProductRequestComments;
	}

	public void setProjectProductRequestComments(Set<ProjectProductRequestComment> projectProductRequestComments) {
		this.projectProductRequestComments = projectProductRequestComments;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public User getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(User projectManager) {
		this.projectManager = projectManager;
	}

	public Long gettProjectManagerId() {
		return tProjectManagerId;
	}

	public void settProjectManagerId(Long tProjectManagerId) {
		if (tProjectManagerId != null) {
			this.projectManager = new User();
			this.projectManager.setId(tProjectManagerId);
		}
		this.tProjectManagerId = tProjectManagerId;
	}

}
