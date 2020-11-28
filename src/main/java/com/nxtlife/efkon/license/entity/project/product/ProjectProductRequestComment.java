package com.nxtlife.efkon.license.entity.project.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import com.nxtlife.efkon.license.entity.user.User;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class ProjectProductRequestComment extends BaseEntity implements Serializable {

	@NotEmpty(message = "comment can't be empty")
	private String comment;

	@NotNull(message = "commented by can't be null")
	@ManyToOne
	private User commentedBy;

	private String remark;

	@NotNull(message = "project product license request id can't be null")
	@ManyToOne
	private ProjectProductLicenseRequest projectProductLicenseRequest;

	public ProjectProductRequestComment() {

	}

	public ProjectProductRequestComment(String comment, Long commentedById, String remark,
			Long projectProductLicenseRequestId) {
		super();
		this.comment = comment;
		this.remark = remark;
		if (commentedById != null) {
			this.commentedBy = new User();
			this.commentedBy.setId(commentedById);
		}
		if (projectProductLicenseRequestId != null) {
			this.projectProductLicenseRequest = new ProjectProductLicenseRequest();
			this.projectProductLicenseRequest.setId(projectProductLicenseRequestId);
		}

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(User commentedBy) {
		this.commentedBy = commentedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ProjectProductLicenseRequest getProjectProductLicenseRequest() {
		return projectProductLicenseRequest;
	}

	public void setProjectProductLicenseRequest(ProjectProductLicenseRequest projectProductLicenseRequest) {
		this.projectProductLicenseRequest = projectProductLicenseRequest;
	}

}
