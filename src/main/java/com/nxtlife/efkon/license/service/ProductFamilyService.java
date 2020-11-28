package com.nxtlife.efkon.license.service;

import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductFamilyRequest;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;

import java.util.List;

public interface ProductFamilyService {

	/**
	 * this method used to save project family details.
	 *
	 * @param
	 * @param request
	 * @return {@link ProductFamilyResponse} after saving product family information
	 * @throws ValidationException if name already exists or code already exist
	 */
	public ProductFamilyResponse save(ProductFamilyRequest request);

	/**
	 * this method used to fetch all activated product families
	 *
	 * @return list of <tt>ProductFamilyResponse</tt>
	 */
	public List<ProductFamilyResponse> findAllActivated();

	/**
	 * this method used to fetch all product families
	 *
	 * @return list of <tt>ProductFamilyResponse</tt>
	 */
	public List<ProductFamilyResponse> findAll();

	/**
	 * this method used to update product family details. It throws exception if
	 * product family id isn't correct
	 *
	 * @param id
	 * @param request
	 * @return {@link ProductFamilyResponse} after updating product family
	 *         information
	 * @throws NotFoundException   if product family id isn't correct
	 * @throws ValidationException if name already exists or code already exist
	 */
	public ProductFamilyResponse update(Long id, ProductFamilyRequest request);

	/**
	 * this method used to delete product family. It throws exception if product
	 * family id not found
	 *
	 * @param id
	 * @return {@link SuccessResponse} if product family deleted successfully
	 * @throws NotFoundException if product family not found
	 */
	public SuccessResponse delete(Long id);

	public ProductFamilyResponse reactivate(Long id);

}
