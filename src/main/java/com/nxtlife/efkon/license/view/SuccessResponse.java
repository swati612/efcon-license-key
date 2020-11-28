package com.nxtlife.efkon.license.view;

import io.swagger.v3.oas.annotations.media.Schema;

public class SuccessResponse {
	
	@Schema(description="success status code", example="200")
	private int status;
	@Schema(description="success message", example="Successfully submitted")
	private String message;

	public SuccessResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
