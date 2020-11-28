package com.nxtlife.efkon.license.entity.project.product;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
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
import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.entity.product.ProductDetail;
import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.enums.ExpirationPeriodType;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;

@SuppressWarnings("serial")
@Entity
@Table(name = "project_product")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class ProjectProduct extends BaseEntity implements Serializable {

	@NotNull(message = "license count can't be null")
	private Integer licenseCount;

	@NotNull(message = "license_type can't be null")
	@ManyToOne
	private LicenseType licenseType;

	@Transient
	private Long tLicenseTypeId;

	@NotNull(message = "expiration_period_type can't be null")
	@Enumerated(EnumType.STRING)
	private ExpirationPeriodType expirationPeriodType;

	private Integer expirationMonthCount;

	@NotNull(message = "start_date can't be null")
	private String startDate;

	private String endDate;

	@NotNull(message = "status can't be null")
	@Enumerated(EnumType.STRING)
	private ProjectProductStatus status;

	@ManyToOne
	private Project project;

	@Transient
	private Long tProjectId;

	@Transient
	private Long tProductDetailId;

	@ManyToOne
	private ProductDetail productDetail;

	@ManyToOne
	private ProjectProduct pastProjectProduct;

	@Transient
	private Long tPastProjectProductId;

	@OneToMany(mappedBy = "projectProduct")
	private Set<License> licenses;

	@OneToMany(mappedBy = "projectProduct")
	private Set<ProjectProductComment> projectProductComments;

	@OneToMany(mappedBy = "projectProduct", cascade = CascadeType.ALL)
	private Set<ProjectProductLicenseRequest> projectProductLicenseRequest;

	public ProjectProduct() {
		super();
	}

	public ProjectProduct(Integer licenseCount, @NotNull(message = "license_type can't be null") Long licenseTypeId,
			@NotNull(message = "expiration_period_type can't be null") ExpirationPeriodType expirationPeriodType,
			Integer expirationMonthCount, @NotNull(message = "start_date can't be null") String startDate,
			String endDate, @NotNull(message = "status can't be null") ProjectProductStatus status) {
		this.licenseCount = licenseCount;
		if (licenseTypeId != null) {
			this.licenseType = new LicenseType();
			this.licenseType.setId(licenseTypeId);
		}
		this.expirationPeriodType = expirationPeriodType;
		this.expirationMonthCount = expirationMonthCount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public Integer getLicenseCount() {
		return licenseCount;
	}

	public void setLicenseCount(Integer licenseCount) {
		this.licenseCount = licenseCount;
	}

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public Long gettLicenseTypeId() {
		return tLicenseTypeId;
	}

	public void settLicenseTypeId(Long tLicenseTypeId) {
		if (tLicenseTypeId != null) {
			this.licenseType = new LicenseType();
			this.licenseType.setId(tLicenseTypeId);
		}
		this.tLicenseTypeId = tLicenseTypeId;
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

	public Long gettProjectId() {
		return tProjectId;
	}

	public void settProjectId(Long tProjectId) {
		if (tProjectId != null) {
			this.project = new Project();
			this.project.setId(tProjectId);
		}
		this.tProjectId = tProjectId;
	}

	public Long gettProductDetailId() {
		return tProductDetailId;
	}

	public void settProductDetailId(Long tProductDetailId) {
		if (tProductDetailId != null) {
			this.productDetail = new ProductDetail();
			this.productDetail.setId(tProductDetailId);
		}
		this.tProductDetailId = tProductDetailId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ProjectProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectProductStatus status) {
		this.status = status;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}

	public ProjectProduct getPastProjectProduct() {
		return pastProjectProduct;
	}

	public void setPastProjectProduct(ProjectProduct pastProjectProduct) {
		this.pastProjectProduct = pastProjectProduct;
	}

	public Long gettPastProjectProductId() {
		return tPastProjectProductId;
	}

	public void settPastProjectProductId(Long tPastProjectProductId) {
		if (tPastProjectProductId != null) {
			this.pastProjectProduct = new ProjectProduct();
			this.pastProjectProduct.setId(tPastProjectProductId);
		}
		this.tPastProjectProductId = tPastProjectProductId;
	}

	public Set<License> getLicenses() {
		return licenses;
	}

	public void setLicenses(Set<License> licenses) {
		this.licenses = licenses;
	}

	public Set<ProjectProductComment> getProjectProductComments() {
		return projectProductComments;
	}

	public void setProjectProductComments(Set<ProjectProductComment> projectProductComments) {
		this.projectProductComments = projectProductComments;
	}

	public Set<ProjectProductLicenseRequest> getProjectProductLicenseRequest() {
		return projectProductLicenseRequest;
	}

	public void setProjectProductLicenseRequest(Set<ProjectProductLicenseRequest> projectProductLicenseRequest) {
		this.projectProductLicenseRequest = projectProductLicenseRequest;
	}

}
