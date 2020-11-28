package com.nxtlife.efkon.license.view.product;

import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductCodeRequest implements Request {

	@Schema(description = "Id of the product code")
	private Long id;

	@Schema(description = "Name of the product code")
	@NotEmpty(message = "name can't be empty")
	private String name;

	public Long getId() {
		return unmask(id);
	}

	public String getName() {
		return name;
	}
}
