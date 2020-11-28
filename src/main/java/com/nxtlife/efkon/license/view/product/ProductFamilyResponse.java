package com.nxtlife.efkon.license.view.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.product.ProductFamily;
import com.nxtlife.efkon.license.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProductFamilyResponse implements Response {

	@Schema(description = "Id of the product family")
	private Long id;

	@Schema(description = "Name of the product family")
	private String name;

	@Schema(description = "Code of product family")
	private String code;

	@Schema(description = "Description of product family")
	private String description;

	@Schema(description = "Active true if product family is active", example = "true", nullable = false)
	public Boolean active;

	private List<ProductCodeResponse> productCodes;

	public ProductFamilyResponse(Long id, String name, String code, String description, Boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.active = active;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductCodeResponse> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<ProductCodeResponse> productCodes) {
		this.productCodes = productCodes;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public static ProductFamilyResponse get(ProductFamily productFamily) {
		if (productFamily != null) {
			ProductFamilyResponse response = new ProductFamilyResponse(productFamily.getId(), productFamily.getName(),
					productFamily.getCode(), productFamily.getDescription(), productFamily.getActive());
			return response;
		}
		return null;

	}

}
