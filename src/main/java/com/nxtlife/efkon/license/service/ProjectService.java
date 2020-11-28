package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectRequest;
import com.nxtlife.efkon.license.view.project.ProjectResponse;

public interface ProjectService {

	/**
	 * this method used to save the project
	 *
	 * @throws ValidationException if project type id or project manager id not
	 *                             found
	 *
	 * @return {@Link ProjectResponse}
	 */

	public ProjectResponse save(ProjectRequest request);

	/**
	 * this method used to fetch all projects
	 *
	 * @return list of <tt>ProjectResponse</tt>
	 */
	public List<ProjectResponse> findAll();

	/**
	 * this method is used to update project
	 * 
	 * @param id
	 * @param request
	 * @return {@link ProjectResponse}
	 */
	public ProjectResponse update(Long id, ProjectRequest request);

	/**
	 * this method is used to fetch project details by project id
	 * 
	 * @param id
	 * @return {@link ProjectResponse}
	 */
	public ProjectResponse findById(Long id);

	public SuccessResponse delete(Long id);

}
