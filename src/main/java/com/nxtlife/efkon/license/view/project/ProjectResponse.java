package com.nxtlife.efkon.license.view.project;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProjectResponse implements Response {

	@Schema(description = "Id of the project", example = "1")
	private Long id;

	@Schema(description = "Project created date", example = "2020-12-06'T'12:12:12.234")
	private String createdAt;

	@Schema(description = "Code of the customer", example = "0001")
	private String customerCode;

	@Schema(description = "Name of the customer", example = "Rahul")
	private String customerName;

	@Schema(description = "Email of the customer", example = "abc@gmail.com")
	private String customerEmail;

	@Schema(description = "Is email be send or not", example = "true")
	private Boolean isEmailSend;

	@Schema(description = "Contact of the customer", example = "1234567890")
	private String customerContactNo;

	@Schema(example = "1")
	private Long projectTypeId;

	@Schema(description = "Project type like Clean India or Traffic Related", example = "Clean India")
	private String projectTypeName;

	@Schema(example = "1")
	private Long projectManagerId;

	@Schema(example = "Mr. Kumar")
	private String projectManagerName;

	private ProjectTypeResponse projectType;

	private List<ProjectProductResponse> products;

	@Schema(description = "count of all the products in a project", example = "1")
	private Long productsCount = 0l;
	
	@Schema(description = "count of all approved products in a project", example = "1")
	private Long approvedProductsCount = 0l;

	public ProjectResponse(Long id, String createdAt, String customerCode, String customerName, String customerEmail,
			Boolean isEmailSend, String customerContactNo, Long projectTypeId, String projectTypeName,
			Long projectManagerId, String projectManagerName) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.isEmailSend = isEmailSend;
		this.customerContactNo = customerContactNo;
		this.projectTypeId = projectTypeId;
		this.projectTypeName = projectTypeName;
		this.projectManagerId = projectManagerId;
		this.projectManagerName = projectManagerName;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
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

	public Boolean getIsEmailSend() {
		return isEmailSend;
	}

	public void setIsEmailSend(Boolean emailSend) {
		isEmailSend = emailSend;
	}

	public String getCustomerContactNo() {
		return customerContactNo;
	}

	public void setCustomerContactNo(String customerContactNo) {
		this.customerContactNo = customerContactNo;
	}

	public ProjectTypeResponse getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectTypeResponse projectType) {
		this.projectType = projectType;
	}

	public List<ProjectProductResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProjectProductResponse> products) {
		this.products = products;
	}

	public Long getProjectTypeId() {
		return mask(projectTypeId);
	}

	public void setProjectTypeId(Long projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public Long getProjectManagerId() {
		return mask(projectManagerId);
	}

	public void setProjectManagerId(Long projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	public String getProjectManagerName() {
		return projectManagerName;
	}

	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}

	public Long getProductsCount() {
		return productsCount;
	}

	public void setProductsCount(Long productsCount) {
		this.productsCount = productsCount;
	}

	public Long getApprovedProductsCount() {
		return approvedProductsCount;
	}

	public void setApprovedProductsCount(Long approvedProductsCount) {
		this.approvedProductsCount = approvedProductsCount;
	}

	public static ProjectResponse get(Project project) {
		if (project != null) {
			ProjectResponse response = new ProjectResponse(project.getId(), project.getCreatedAt(),
					project.getCustomerCode(), project.getCustomerName(), project.getCustomerEmail(),
					project.getIsEmailSend(), project.getCustomerContactNo(), null, null, null, null);
			response.setProjectType(new ProjectTypeResponse(project.getProjectType()));
			return response;
		}
		return null;
	}
}
