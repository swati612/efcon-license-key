package com.nxtlife.efkon.license.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.ProductCodeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductDetailJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductFamilyJpaDao;
import com.nxtlife.efkon.license.entity.product.ProductCode;
import com.nxtlife.efkon.license.entity.product.ProductFamily;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProductFamilyService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductCodeRequest;
import com.nxtlife.efkon.license.view.product.ProductFamilyRequest;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;

@Service("productFamilyServiceImpl")
@Transactional
public class ProductFamilyServiceImpl extends BaseService implements ProductFamilyService {

	private static Logger logger = LoggerFactory.getLogger(ProductFamilyServiceImpl.class);

	@Autowired
	private ProductFamilyJpaDao productFamilyDao;

	@Autowired
	private ProductCodeJpaDao productCodeDao;

	@Autowired
	private ProductDetailJpaDao productDetailDao;

	public void validate(ProductFamilyRequest request) {
		if (productFamilyDao.existsByName(request.getName())) {
			throw new ValidationException(String.format("Product family (%s) already exist", request.getName()));
		}
		request.getProductCodes().stream().forEach(codeRequest -> {
			if (codeRequest.getId() == null && productCodeDao.existsByName(codeRequest.getName())) {
				throw new ValidationException(String.format("Project code (%s) already exist", codeRequest.getName()));
			}
		});
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_CREATE)
	public ProductFamilyResponse save(ProductFamilyRequest request) {
		validate(request);
		ProductFamily productFamily = request.toEntity();
		productFamily.setProductCodes(new HashSet<>());
		Integer sequence = sequenceGenerator("PRODUCTCODE");
		for (ProductCodeRequest codeRequest : request.getProductCodes()) {
			if (codeRequest.getId() == null) {
				productFamily.getProductCodes()
						.add(new ProductCode(codeRequest.getName(), String.format("%02d", sequence++), productFamily));
			}
		}
		updateSequenceGenerator("PRODUCTCODE", sequence);
		productFamilyDao.save(productFamily);
		ProductFamilyResponse response = productFamilyDao.findResponseById(productFamily.getId());
		response.setProductCodes(productCodeDao.findByProductFamilyIdAndActive(productFamily.getId(), true));
		return response;

	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_FETCH)
	public List<ProductFamilyResponse> findAllActivated() {
		List<ProductFamilyResponse> productFamilies = productFamilyDao.findByActive(true);
		productFamilies.stream().forEach(productFamily -> productFamily
				.setProductCodes(productCodeDao.findByProductFamilyIdAndActive(unmask(productFamily.getId()), true)));
		if (productFamilies.isEmpty()) {
			throw new NotFoundException("no product family found");
		}
		return productFamilies;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_FETCH)
	public List<ProductFamilyResponse> findAll() {
		List<ProductFamilyResponse> productFamilies = productFamilyDao.findAll().stream()
				.map(ProductFamilyResponse::get).collect(Collectors.toList());
		productFamilies.stream().forEach(productFamily -> productFamily
				.setProductCodes(productCodeDao.findByProductFamilyId(unmask(productFamily.getId()))));

		if (productFamilies.isEmpty()) {
			throw new NotFoundException("no product family found");
		}
		return productFamilies;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_UPDATE)
	public ProductFamilyResponse update(Long id, ProductFamilyRequest request) {
		Long unmaskId = unmask(id);
		ProductFamilyResponse productFamily = productFamilyDao.findResponseById(unmaskId);
		if (productFamily == null) {
			throw new NotFoundException("Product family not found");
		}
		if (request.getName() != null) {
			if (!productFamily.getName().equals(request.getName())) {
				if (productFamilyDao.existsByName(request.getName())) {
					throw new ValidationException("This product family already exists");
				}
			}
		}
		if (request.getProductCodes() != null) {
			request.getProductCodes().forEach(productCode -> {
				Long productCodeId = productCodeDao.findIdByName(productCode.getName());
				if (productCode.getId() != null && productCodeId != null
						&& !productCode.getId().equals(productCodeId)) {
					throw new ValidationException(
							String.format("This product code(%s) already exist", productCode.getName()));
				}
			});
		}
		if (request.getName() != null) {
			int rows = productFamilyDao.updateById(request.getName(), request.getDescription(), request.getCode(),
					unmaskId, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Product family {} updated successfully", unmaskId);
			}
		}
		List<Long> productFamilyCodeIds = productCodeDao.findAllIdsByProductFamilyIdAndActive(unmaskId, true);
		ProductCode productcode;
		Integer sequence = sequenceGenerator("PRODUCTCODE");
		for (ProductCodeRequest codeRequest : request.getProductCodes()) {
			if (codeRequest.getId() == null) {
				productcode = new ProductCode(codeRequest.getName(), String.format("%02d", sequence++), unmaskId);
				productCodeDao.save(productcode);
			} else {
				productCodeDao.updateNameById(codeRequest.getName(), codeRequest.getId(), getUserId(), new Date());
				productFamilyCodeIds.remove(codeRequest.getId());
			}
		}
		updateSequenceGenerator("PRODUCTCODE", sequence);
		if (!productFamilyCodeIds.isEmpty()) {
			// productCodeDao.deleteByIds(productFamilyCodeIds, getUserId(), new
			// Date());
		}

		ProductFamilyResponse response = productFamilyDao.findResponseById(unmaskId);
		response.setProductCodes(productCodeDao.findByProductFamilyIdAndActive(unmaskId, true));
		return response;

	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!productFamilyDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("Product Family (%s) not found", id));
		}
		if (productDetailDao.existsByProductFamilyIdAndActive(unmaskId, true)) {
			throw new ValidationException(String.format(
					"This product family can't be deactivate as some of the products are related to this product family "));
		}
		productCodeDao.deleteByProductFamilyId(unmaskId, getUserId(), new Date());
		int rows = productFamilyDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Product family {} successfuly deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Product family deactivated successfully");
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_FAMILY_UPDATE)
	public ProductFamilyResponse reactivate(Long id) {
		Long unmaskId = unmask(id);
		if (!productFamilyDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("Product Family (%s) not found", id));
		}
		int rows = productFamilyDao.reactivate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Product family {} successfuly reactivated", unmaskId);
		}
		productCodeDao.updateByProductFamilyId(unmaskId, getUserId(), new Date());

		ProductFamilyResponse response = productFamilyDao.findResponseById(unmaskId);
		response.setProductCodes(productCodeDao.findByProductFamilyIdAndActive(unmaskId, true));
		return response;
	}
}
