package com.nxtlife.efkon.license.view.product;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.license.entity.product.ProductFamily;
import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductFamilyRequest implements Request {

	@Schema(description = "name of the product family")
	@NotEmpty(message = "name can't be empty")
	private String name;

	@Schema(description = "code of the product family")
	@NotEmpty(message = "code can't be empty")
	private String code;

	@Schema(description = "description of the product family")
	@NotEmpty(message = "description can't be empty")
	private String description;

	@Schema(description = "codes of product code")
	@NotEmpty(message = "product codes can't be empty")
	@Valid
	private Set<ProductCodeRequest> productCodes;

	public ProductFamily toEntity() {
		ProductFamily productFamily = new ProductFamily();
		productFamily.setName(name);
		productFamily.setCode(code);
		productFamily.setDescription(description);
		return productFamily;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Set<ProductCodeRequest> getProductCodes() {
		return productCodes;
	}

}
