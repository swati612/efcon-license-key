package com.nxtlife.efkon.license.view.product;

import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductDetailRequest implements Request {

	@Schema(description = "Id of the product family", example = "1", required = true)
	@NotNull(message = "product family id can't be null")
	private Long productFamilyId;

	@Schema(description = "Id of the product code", example = "1", required = true)
	@NotNull(message = "product code id can't be null")
	private Long productCodeId;

	@Schema(description = "Version name", example = "1", required = true)
	@NotNull(message = "Version name can't be null")
	private String version;

	@NotNull(message = "Description can't be null")
	private String description;

	public Long getProductFamilyId() {
		return unmask(productFamilyId);
	}

	public Long getProductCodeId() {
		return unmask(productCodeId);
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

}
