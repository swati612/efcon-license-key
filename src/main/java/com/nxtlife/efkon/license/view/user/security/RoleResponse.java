package com.nxtlife.efkon.license.view.user.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.efkon.license.entity.user.Role;
import com.nxtlife.efkon.license.view.Response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(value = Include.NON_ABSENT)
public class RoleResponse implements Response {

    @Schema(description = "Name of the role", example = "admin")
    public String name;

    @Schema(description = "Id of the role", example = "1")
    public Long id;

    @Schema(description = "Active true if role is active", example = "true", nullable = true)
    public Boolean active;

    @Schema(description = "authorities for the role")
    public List<AuthorityResponse> authorities;

    public RoleResponse(Long id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<AuthorityResponse> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityResponse> authorities) {
        this.authorities = authorities;
    }

    public static RoleResponse get(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getActive());
    }

}
