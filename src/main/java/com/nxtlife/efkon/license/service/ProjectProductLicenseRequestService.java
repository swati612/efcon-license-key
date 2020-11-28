package com.nxtlife.efkon.license.service;

import java.util.List;

import javax.validation.Valid;

import com.nxtlife.efkon.license.enums.LicenseRequestStatus;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;

public interface ProjectProductLicenseRequestService {

	/**
	 * this method is used for saving project product license reuest
	 * 
	 * @param request
	 * @return {@link ProjectProductLicenseRequestResponse}
	 */
	public ProjectProductLicenseRequestResponse save(Long projectProductId,
			ProjectProductLicenseRequestRequest request);

	/**
	 * this method is used for updating project product license request
	 * 
	 * @param id
	 * @param request
	 * @return {@link ProjectProductLicenseRequestResponse}
	 */
	public ProjectProductLicenseRequestResponse update(Long id, @Valid ProjectProductLicenseRequestRequest request);

	/**
	 * this method is used for finding project product license request using
	 * project product license request id
	 * 
	 * @param id
	 * @return {@link ProjectProductLicenseRequestResponse}
	 */
	public ProjectProductLicenseRequestResponse findById(Long id);

	/**
	 * this method is used for deleting project product license request using
	 * project product license request id
	 * 
	 * @param id
	 * @return {@link SuccessResponse}
	 */
	public SuccessResponse delete(Long id);

	/**
	 * this method is used for updating project product license request status
	 * to accept using project product license request id
	 * 
	 * @param id
	 * @param status
	 * @param request
	 * @return {@link ProjectProductLicenseRequestResponse}
	 */
	public ProjectProductLicenseRequestResponse accept(Long id, LicenseRequestStatus accept,
			ProjectProductRequest request);

	/**
	 * this method is used for updating project product license request status
	 * to reject using project product license request id
	 * 
	 * @param id
	 * @param reject
	 * @param comment
	 * @return
	 */
	public ProjectProductLicenseRequestResponse reject(Long id, LicenseRequestStatus reject, String comment);

	/**
	 * this method is used to find project product license request by status
	 * 
	 * @return
	 */
	public List<ProjectProductLicenseRequestResponse> findByStatus(LicenseRequestStatus status);

}
