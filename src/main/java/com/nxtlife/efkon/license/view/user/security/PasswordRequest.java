package com.nxtlife.efkon.license.view.user.security;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.nxtlife.efkon.license.ex.ValidationException;

import io.swagger.v3.oas.annotations.media.Schema;

public class PasswordRequest {

	@Schema(description = "Required username for password change or forgot", example = "jhon01", required = true)
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username not valid")
	private String username;
	@Schema(description = "New password of user", example = "abc123")
	@NotEmpty(message = "password can't be null/empty")
	private String password;
	@Schema(description="Temporary generated password for change password", example="12345")
	private String generatedPassword;
	@Schema(description = "Old password of user", example = "1234")
	private String oldPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void checkPassword() {
		if (this.password.equals(this.oldPassword)) {
			throw new ValidationException("old password and password can't be same");
		}
	}

	public void checkGeneratedPassword() {
		if(generatedPassword==null){
			throw new ValidationException("Generated password can't be empty");
		}else if (this.password.equals(this.generatedPassword)) {
			throw new ValidationException("generated password and password can't be same");
		}
	}
}
