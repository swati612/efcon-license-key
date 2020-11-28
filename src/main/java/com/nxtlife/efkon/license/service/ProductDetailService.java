package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailRequest;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;

public interface ProductDetailService {

	/**
	 * this method used to fetch all product details
	 *
	 * @return list of <tt>ProductDetailResponse</tt>
	 */

	public List<ProductFamilyResponse> findAll();

	public List<ProductFamilyResponse> findByActiveTrue();

	/**
	 * this method used to save product detail.
	 *
	 * @param
	 * @param request
	 * @return {@link ProductDetailResponse} after saving product detail information
	 * @throws ValidationException if product family id or product code id or
	 *                             version id not exist
	 */

	public ProductDetailResponse save(ProductDetailRequest request);

	/**
	 * this method used to update product detail. It throws exception if product
	 * detail id isn't correct
	 *
	 * @param id
	 * @param request
	 * @return {@link ProductDetailResponse} after updating product detail
	 *         information
	 * @throws NotFoundException   if product detail id isn't correct
	 * @throws ValidationException if product family is update or product code or
	 *                             version id not exist
	 */

	public ProductDetailResponse update(Long id, ProductDetailRequest request);

	public SuccessResponse activate(Long id);

	/**
	 * this method used to delete product detail. It throws exception if product
	 * detail id not found
	 *
	 * @param id
	 * @return {@link SuccessResponse} if product detail deleted successfully
	 * @throws NotFoundException if product detail not found
	 */

	public SuccessResponse delete(Long id);
}
