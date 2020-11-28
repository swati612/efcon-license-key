package com.nxtlife.efkon.license.service;

import com.nxtlife.efkon.license.view.product.ProductCodeResponse;

import java.util.List;

public interface ProductCodeService {

    /**
     * this method used to fetch  project codes by product family id
     *
     * @Param productFamilyId
     *
     * @throws NotFoundException
     *              if product family id not found
     * * @return list of <tt>ProductCodeResponse</tt>
     */
    public List<ProductCodeResponse> findAll(Long productFamilyId);
}
