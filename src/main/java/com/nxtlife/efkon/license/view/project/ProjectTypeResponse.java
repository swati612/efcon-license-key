package com.nxtlife.efkon.license.view.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.project.ProjectType;
import com.nxtlife.efkon.license.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ProjectTypeResponse implements Response {

    @Schema(description = "Id of the project type", example = "1")
    private Long id;

    @Schema(description = "Name of the project type", example = "Traffic Control")
    private String name;

    @Schema(description = "active status of project type", example = "true")
    private Boolean active;

    public ProjectTypeResponse(){super();}
    
    public ProjectTypeResponse(ProjectType projectType)
    {
        super();
        this.id=projectType.getId();
        this.name=projectType.getName();
        this.active=projectType.getActive();
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
