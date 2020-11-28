package com.nxtlife.efkon.license.view.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.version.VersionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProductDetailResponse implements Response {

	@Schema(description = "Id of the product detail")
	private Long id;
	private Boolean active;

	@Schema(description = "Description of the product detail")
	private String description;

	@Schema(description = "product family id", example = "1")
	private Long productFamilyId;
	@Schema(description = "product family name", example = "ATCS")
	private String productFamilyName;
	@Schema(description = "product family code", example = "AT")
	private String productFamilyCode;
	@Schema(description = "product family description", example = "AT")
	private String productFamilyDescription;

	@Schema(description = "product family of product detail")
	private ProductFamilyResponse productFamily;

	@Schema(description = "product code id", example = "1")
	private Long productCodeId;
	@Schema(description = "product code name", example = "ATCS")
	private String productCodeName;
	@Schema(description = "product code code", example = "0001")
	private String productCodeCode;

	@Schema(description = "product code of product detail")
	private ProductCodeResponse productCode;

	@Schema(description = "version id", example = "1")
	private Long versionId;
	@Schema(description = "version name", example = "v1")
	private String versionName;

	@Schema(description = "version of product detail")
	private VersionResponse version;

	public ProductDetailResponse(Long id, Boolean active, String description, Long productFamilyId,
			String productFamilyName, String productFamilyCode, String productFamilyDescription, Long productCodeId,
			String productCodeName, String productCodeCode, Long versionId, String versionVersion) {
		super();
		this.id = id;
		this.active = active;
		this.description = description;
		this.productFamilyId = productFamilyId;
		this.productFamilyName = productFamilyName;
		this.productFamilyCode = productFamilyCode;
		this.productFamilyDescription = productFamilyDescription;
		this.productCodeId = productCodeId;
		this.productCodeName = productCodeName;
		this.productCodeCode = productCodeCode;
		this.versionId = versionId;
		this.versionName = versionVersion;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductFamilyResponse getProductFamily() {
		return productFamily;
	}

	public void setProductFamily(ProductFamilyResponse productFamily) {
		this.productFamily = productFamily;
	}

	public ProductCodeResponse getProductCode() {
		return productCode;
	}

	public void setProductCode(ProductCodeResponse productCode) {
		this.productCode = productCode;
	}

	public VersionResponse getVersion() {
		return version;
	}

	public void setVersion(VersionResponse version) {
		this.version = version;
	}

	public Long getProductFamilyId() {
		return mask(productFamilyId);
	}

	public void setProductFamilyId(Long productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public String getProductFamilyCode() {
		return productFamilyCode;
	}

	public void setProductFamilyCode(String productFamilyCode) {
		this.productFamilyCode = productFamilyCode;
	}

	public String getProductFamilyDescription() {
		return productFamilyDescription;
	}

	public void setProductFamilyDescription(String productFamilyDescription) {
		this.productFamilyDescription = productFamilyDescription;
	}

	public Long getProductCodeId() {
		return mask(productCodeId);
	}

	public void setProductCodeId(Long productCodeId) {
		this.productCodeId = productCodeId;
	}

	public String getProductCodeName() {
		return productCodeName;
	}

	public void setProductCodeName(String productCodeName) {
		this.productCodeName = productCodeName;
	}

	public String getProductCodeCode() {
		return productCodeCode;
	}

	public void setProductCodeCode(String productCodeCode) {
		this.productCodeCode = productCodeCode;
	}

	public Long getVersionId() {
		return mask(versionId);
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
