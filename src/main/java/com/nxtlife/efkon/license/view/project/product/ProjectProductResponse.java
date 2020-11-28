package com.nxtlife.efkon.license.view.project.product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.enums.ExpirationPeriodType;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.util.DateUtil;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.license.LicenseResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.project.ProjectResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProjectProductResponse implements Response {

	@Schema(description = " Id of the project product", example = "1")
	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date modifiedAt;

	private Long projectId;

	private ProjectResponse project;

	private Long productDetailId;

	private ProductDetailResponse productDetail;

	@Schema(description = "No of license", example = "4")
	private Integer licenseCount;

	@Schema(description = "Generated license count", example = "2")
	private Integer generatedLicenseCount = 0;

	@Schema(description = "Type id of license", example = "1")
	private Long licenseTypeId;

	@Schema(description = "Type name of license", example = "DEMO")
	private String licenseTypeName;

	@Schema(description = "Type code of license", example = "DE")
	private String licenseTypeCode;

	@Schema(description = "Type of expiration period", example = "Demo")
	private ExpirationPeriodType expirationPeriodType;

	@Schema(description = "Month count of expiration", example = "3")
	private Integer expirationMonthCount;

	@Schema(description = "Start date for limited license expiry", example = "2020-04-02T13:56:52.837+0530")
	private String startDate;

	@Schema(description = "End date for limited license expiry", example = "2020-04-02T13:56:52.837+0530")
	private String endDate;

	@Schema(description = "status of the project product", example = "Approved")
	private ProjectProductStatus status;

	@Schema(description = "This property will be set if that is renewed project product. Otherwise it won't be set")
	private Long pastProjectProductId;

	@JsonIgnore
	private Long createdById;

	@JsonIgnore
	private Long modifiedById;

	private List<LicenseResponse> licenses;

	private List<ProjectProductCommentResponse> comments;

	public ProjectProductResponse(Long id, Integer licenseCount, Long licenseTypeId, String licenseTypeName,
			String licenseTypeCode, ExpirationPeriodType expirationPeriodType, Integer expirationMonthCount,
			String startDate, String endDate, ProjectProductStatus status, String createdAt, Date modifiedAt,
			Long projectId, Long productDetailId, Long createdById, Long pastProjectProductId, Long modifiedById) {
		super();
		this.id = id;
		this.licenseCount = licenseCount;
		this.licenseTypeId = licenseTypeId;
		this.licenseTypeName = licenseTypeName;
		this.licenseTypeCode = licenseTypeCode;
		this.expirationPeriodType = expirationPeriodType;
		this.expirationMonthCount = expirationMonthCount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.createdAt = createdAt;
		this.projectId = projectId;
		this.productDetailId = productDetailId;
		this.createdById = createdById;
		this.pastProjectProductId = pastProjectProductId;
		this.modifiedAt = modifiedAt;
		this.modifiedById = modifiedById;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(Integer licenseCount) {
		this.licenseCount = licenseCount;
	}

	public Integer getGeneratedLicenseCount() {
		return generatedLicenseCount;
	}

	public void setGeneratedLicenseCount(Integer generatedLicenseCount) {
		this.generatedLicenseCount = generatedLicenseCount;
	}

	public Long getLicenseTypeId() {
		return mask(licenseTypeId);
	}

	public void setLicenseTypeId(Long licenseTypeId) {
		this.licenseTypeId = licenseTypeId;
	}

	public String getLicenseTypeName() {
		return licenseTypeName;
	}

	public void setLicenseTypeName(String licenseTypeName) {
		this.licenseTypeName = licenseTypeName;
	}

	public String getLicenseTypeCode() {
		return licenseTypeCode;
	}

	public void setLicenseTypeCode(String licenseTypeCode) {
		this.licenseTypeCode = licenseTypeCode;
	}

	public ExpirationPeriodType getExpirationPeriodType() {
		return expirationPeriodType;
	}

	public void setExpirationPeriodType(ExpirationPeriodType expirationPeriodType) {
		this.expirationPeriodType = expirationPeriodType;
	}

	public Integer getExpirationMonthCount() {
		return expirationMonthCount;
	}

	public void setExpirationMonthCount(Integer expirationMonthCount) {
		this.expirationMonthCount = expirationMonthCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public ProjectProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectProductStatus status) {
		this.status = status;
	}

	public ProjectResponse getProject() {
		return project;
	}

	public void setProject(ProjectResponse project) {
		this.project = project;
	}

	public ProductDetailResponse getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetailResponse productDetail) {
		this.productDetail = productDetail;
	}

	public Long getPastProjectProductId() {
		return pastProjectProductId;
	}

	public void setPastProjectProductId(Long pastProjectProductId) {
		this.pastProjectProductId = pastProjectProductId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCreatedAt() {
		if (createdAt != null) {
			try {
				DateFormat dateFormate = new SimpleDateFormat(DateUtil.defaultFormat);
				DateFormat dateFormate1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date date = dateFormate.parse(createdAt);
				createdAt = dateFormate1.format(date);
			} catch (ParseException e) {
				return createdAt;
			}
		}
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Long getProjectId() {
		return mask(projectId);
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProductDetailId() {
		return mask(productDetailId);
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}

	public List<ProjectProductCommentResponse> getComments() {
		return comments;
	}

	public void setComments(List<ProjectProductCommentResponse> comments) {
		this.comments = comments;
	}

	public List<LicenseResponse> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<LicenseResponse> licenses) {
		this.licenses = licenses;
	}

	public static ProjectProductResponse get(ProjectProduct projectProduct) {
		if (projectProduct != null) {
			ProjectProductResponse response = new ProjectProductResponse(projectProduct.getId(),
					projectProduct.getLicenseCount(), projectProduct.getLicenseType().getId(),
					projectProduct.getLicenseType().getName(), projectProduct.getLicenseType().getCode(),
					projectProduct.getExpirationPeriodType(), projectProduct.getExpirationMonthCount(),
					projectProduct.getStartDate(), projectProduct.getEndDate(), projectProduct.getStatus(),
					projectProduct.getCreatedAt(), projectProduct.getModifiedAt(), null, null, null, null, null);
			return response;
		}
		return null;
	}

	public List<String> columnValues() {
		List<String> columnValues = new ArrayList<>();
		if (project == null) {
			columnValues.add("NA");
		} else {
			columnValues.add(project.getProjectTypeName() == null ? "NA" : project.getProjectTypeName());
		}
		columnValues.add(createdAt == null ? "NA" : getCreatedAt());
		if (productDetail == null) {
			columnValues.add("NA");
			columnValues.add("NA");
		} else {
			columnValues.add(String.format("%s-%s",
					productDetail.getProductFamilyName() == null ? "NA" : productDetail.getProductFamilyName(),
					productDetail.getProductCodeName() == null ? "NA" : productDetail.getProductCodeName()));
			columnValues.add(productDetail.getVersionName() == null ? "NA" : productDetail.getVersionName());
		}
		columnValues.add(licenseCount == null ? "NA" : licenseCount + "");
		columnValues.add(licenseTypeName == null ? "NA" : licenseTypeName);
		columnValues.add(expirationPeriodType == null ? "NA" : expirationPeriodType + "");
		columnValues.add(expirationMonthCount == null ? "NA" : expirationMonthCount + "");
		columnValues.add(startDate == null ? "NA" : startDate);
		columnValues.add(endDate == null ? "NA" : endDate);
		return columnValues;

	}

	public static List<String> projectProductsColumnHeaders() {
		List<String> columnHeaders = Arrays.asList("Project Type", "CreatedAt", "Product Family-Code", "Version",
				"License Count", "License Type", "Expiration Period", "Expiration Months", "Start Date", "End Date");
		return columnHeaders;
	}

	public static float[] columnWidth() {
		return new float[] { 3.75f, 3.75f, 3.75f, 3.75f, 1.75f, 3.75f, 3.75f, 1.75f, 3.75f, 3.75f, 3.75f };
	}

}
