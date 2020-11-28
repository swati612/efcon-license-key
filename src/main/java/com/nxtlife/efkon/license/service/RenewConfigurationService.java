package com.nxtlife.efkon.license.service;

import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.view.RenewConfigurationRequest;
import com.nxtlife.efkon.license.view.RenewConfigurationResponse;

public interface RenewConfigurationService {

	/**
	 * this method used to update renew configuration details
	 * 
	 * @param request
	 * @return {@link RenewConfigurationResponse}
	 * @throws NotFoundException
	 *             if renew configuration detail not found
	 */
	public RenewConfigurationResponse update(RenewConfigurationRequest request);

	/**
	 * this method used to fetch renew configuration details
	 * 
	 * @return {@link RenewConfigurationResponse}
	 * @throws NotFoundException
	 *             if renew configuration detail not found
	 */
	public RenewConfigurationResponse find();

}
