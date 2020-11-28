package com.nxtlife.efkon.license.entity.project;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class ProjectType extends BaseEntity implements Serializable {

	@NotEmpty(message = "name can't be null")
	private String name;

	@OneToMany(mappedBy = "projectType", cascade = CascadeType.ALL)
	private Set<Project> projects;

	public ProjectType() {
		super();
	}

	public ProjectType(@NotEmpty(message = "name can't be null") String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
