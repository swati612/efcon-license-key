package com.nxtlife.efkon.license.view.project;

import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.view.Request;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProjectRequest implements Request {

	@Schema(description = "Name of the customer", example = "Rahul")
	@NotEmpty(message = "Customer name can't be empty")
	private String customerName;

	@Schema(description = "Email of the customer", example = "abc@gmail.com")
	@NotEmpty(message = "Customer email can't be empty")
	private String customerEmail;

	@Schema(description = "Is email be send or not", example = "true")
	private Boolean isEmailSend;

	@Schema(description = "Contact of the customer", example = "1234567890")
	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit")
	private String customerContactNo;

	@Schema(description = "Id of the project type", example = "1")
	@NotNull(message = "project type id can't be null")
	private Long projectTypeId;

	@Schema(description = "Id of the project manager", example = "1")
	@NotNull(message = "project manager id can't be null")
	private Long projectManagerId;

	public Project toEntity() {
		Project project = new Project();
		project.setCustomerName(customerName);
		project.setCustomerEmail(customerEmail);
		project.setCustomerContactNo(customerContactNo);
		if (isEmailSend != null)
			project.setIsEmailSend(isEmailSend);
		else {
			project.setIsEmailSend(false);
		}
		project.settProjectManagerId(getProjectManagerId());
		project.settProjectTypeId(getProjectTypeId());
		return project;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public Boolean getIsEmailSend() {
		return isEmailSend;
	}

	public String getCustomerContactNo() {
		return customerContactNo;
	}

	public Long getProjectTypeId() {
		return unmask(projectTypeId);
	}

	public Long getProjectManagerId() {
		return unmask(projectManagerId);
	}
}
