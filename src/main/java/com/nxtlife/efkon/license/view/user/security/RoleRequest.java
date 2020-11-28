package com.nxtlife.efkon.license.view.user.security;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.license.entity.user.Role;
import com.nxtlife.efkon.license.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class RoleRequest implements Request {

	@Schema(description = "Name of the role which cannot be null", example = "Admin", required = true)
	@NotNull(message = "Role name can't be null")
	private String name;

	@Schema(description = "List of the authority ids which cannot be null", example = "[2,3]", required = true)
	@NotEmpty(message = "Authority ids can't be null or empty")
	private Set<Long> authorityIds;

	public Role toEntity() {
		Role role = new Role();
		role.setName(name);
		return role;
	}

	public String getName() {
		return name;
	}

	public Set<Long> getAuthorityIds() {
		return authorityIds.stream().map(id -> unmask(id)).collect(Collectors.toSet());
	}
}
