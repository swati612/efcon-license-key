package com.nxtlife.efkon.license.view.project.product;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.view.Response;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProjectProductCommentResponse implements Response {

	public Long commentedById;
	public String commentedBy;
	public String comment;
	public String remark;
	public String createdAt;
	public Date modifiedAt;

	public ProjectProductCommentResponse(Long commentedById, String commentedByName, String comment, String remark,
			String createdAt, Date modifiedAt) {
		super();
		this.commentedById = commentedById;
		this.commentedBy = commentedByName;
		this.comment = comment;
		this.remark = remark;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Long getCommentedById() {
		return mask(commentedById);
	}

	public void setCommentedById(Long commentedById) {
		this.commentedById = commentedById;
	}

	public String getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(String commentedBy) {
		this.commentedBy = commentedBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
