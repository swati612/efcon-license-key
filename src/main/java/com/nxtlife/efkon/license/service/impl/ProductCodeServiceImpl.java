package com.nxtlife.efkon.license.service.impl;

import com.nxtlife.efkon.license.dao.jpa.ProductCodeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductFamilyJpaDao;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProductCodeService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.product.ProductCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("productCodeServiceImpl")
@Transactional
public class ProductCodeServiceImpl extends BaseService implements ProductCodeService {

    @Autowired
    private ProductCodeJpaDao productCodeDao;

    @Autowired
    private ProductFamilyJpaDao productFamilyDao;


    @Override
    @Secured(AuthorityUtils.PRODUCT_CODE_FETCH)
    public List<ProductCodeResponse> findAll(Long productFamilyId) {
        if (!productFamilyDao.existsById(unmask(productFamilyId))) {
            throw new NotFoundException(String.format("Product family (%s) not found", productFamilyId));
        }
        return productCodeDao.findByProductFamilyIdAndActive(productFamilyId, true);
    }
}
