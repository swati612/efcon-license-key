package com.nxtlife.efkon.license.entity.project;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.entity.user.User;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Project extends BaseEntity implements Serializable {

	@NotNull(message = "customer code can't be null")
	private String customerCode;

	@NotNull(message = "customer name can't be null")
	private String customerName;

	@NotNull(message = "customer email can't be null")
	private String customerEmail;

	private String customerContactNo;

	@NotNull(message = "is_email_send can't be null")
	private Boolean isEmailSend;

	@NotNull(message = "project type can't be null")
	@ManyToOne
	private ProjectType projectType;

	@NotNull(message = "project manager can't be null")
	@ManyToOne
	private User projectManager;

	@Transient
	private Long tProjectManagerId;

	@Transient
	private Long tProjectTypeId;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private Set<ProjectProduct> projectProducts;

	public Project() {
		super();
	}

	public Project(@NotNull(message = "customer name can't be null") String customerName, String customerEmail,
			String customerContactNo, @NotNull(message = "is_email_send can't be null") Boolean isEmailSend) {
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerContactNo = customerContactNo;
		this.isEmailSend = isEmailSend;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerContactNo() {
		return customerContactNo;
	}

	public void setCustomerContactNo(String customerContactNo) {
		this.customerContactNo = customerContactNo;
	}

	public Boolean getIsEmailSend() {
		return isEmailSend;
	}

	public void setIsEmailSend(Boolean emailSend) {
		isEmailSend = emailSend;
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

	public Long gettProjectTypeId() {
		return tProjectTypeId;
	}

	public void settProjectTypeId(Long tProjectTypeId) {
		if (tProjectTypeId != null) {
			this.projectType = new ProjectType();
			this.projectType.setId(tProjectTypeId);
		}
		this.tProjectTypeId = tProjectTypeId;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public Set<ProjectProduct> getProjectProducts() {
		return projectProducts;
	}

	public void setProjectProducts(Set<ProjectProduct> projectProducts) {
		this.projectProducts = projectProducts;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

}
