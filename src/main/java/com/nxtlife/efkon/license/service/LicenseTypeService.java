package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

public interface LicenseTypeService {

	/**
	 * this method used to fetch license types
	 * 
	 * @return list of {@link LicenseTypeResponse}
	 */
	public List<LicenseTypeResponse> findAll();

	/**
	 * this method used to update license type month detail
	 * 
	 * @param id
	 * @param monthCount
	 * @return success message if updated successfully
	 */
	public SuccessResponse update(Long id, Integer monthCount);

	/**
	 * this method is used to delete the license typw
	 * 
	 * @param id
	 * @return success message if updated successfully
	 */
	public SuccessResponse delete(Long id);

	/**
	 * this method is used to reactivate the deactivated license type;
	 * 
	 * @param id
	 * @return {@link LicenseTypeResponse}
	 */
	public LicenseTypeResponse reactivate(Long id);

	/**
	 * this method used to fetch all activated license types
	 * 
	 * @return list of {@link LicenseTypeResponse}
	 */
	public List<LicenseTypeResponse> findAllActivated();

}
