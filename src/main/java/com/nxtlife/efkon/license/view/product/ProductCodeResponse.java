package com.nxtlife.efkon.license.view.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.product.ProductCode;
import com.nxtlife.efkon.license.view.Response;
import com.nxtlife.efkon.license.view.version.VersionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProductCodeResponse implements Response {

	@Schema(description = "Id of the product code")
	private Long id;

	@Schema(description = "Name of the product code")
	private String name;

	private List<VersionResponse> versions;

	public ProductCodeResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	public List<VersionResponse> getVersions() {
		return versions;
	}

	public void setVersions(List<VersionResponse> versions) {
		this.versions = versions;
	}

	public static ProductCodeResponse get(ProductCode productCode) {
		if (productCode != null) {
			ProductCodeResponse response = new ProductCodeResponse(productCode.getId(), productCode.getName());
			return response;
		}
		return null;

	}

}
