package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

public interface ProjectTypeService {

	/**
	 * this method used to save project type
	 * 
	 * @param projectType
	 * @return {@link ProjectTypeResponse}
	 */
	public ProjectTypeResponse save(String projectType);

	/**
	 * this method used to fetch all projectTypes
	 *
	 * @return list of <tt>ProjectTypeResponse</tt>
	 */
	public List<ProjectTypeResponse> findAll();

	/**
	 * this method is used to delete the project type
	 * 
	 * @param id
	 * @return {@link SuccessResponse}
	 */
	public SuccessResponse delete(Long id);

	/**
	 * this method is used to reactivate the project type
	 * 
	 * @param id
	 * @return
	 */
	public ProjectTypeResponse reactivate(Long id);

}
