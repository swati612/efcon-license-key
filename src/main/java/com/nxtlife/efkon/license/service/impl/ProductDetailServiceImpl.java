package com.nxtlife.efkon.license.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.nxtlife.efkon.license.dao.jpa.ProjectProductJpaDao;
import com.nxtlife.efkon.license.dao.jpa.VersionJpaDao;
import com.nxtlife.efkon.license.entity.product.ProductDetail;
import com.nxtlife.efkon.license.entity.version.Version;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProductDetailService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.product.ProductCodeResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailRequest;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;
import com.nxtlife.efkon.license.view.version.VersionResponse;

@Service("productDetailServiceImpl")
@Transactional
public class ProductDetailServiceImpl extends BaseService implements ProductDetailService {

	private static Logger logger = LoggerFactory.getLogger(ProductFamilyServiceImpl.class);

	@Autowired
	public ProductDetailJpaDao productDetailDao;

	@Autowired
	public ProductFamilyJpaDao productFamilyDao;

	@Autowired
	public ProductCodeJpaDao productCodeDao;

	@Autowired
	public VersionJpaDao versionDao;

	@Autowired
	ProjectProductJpaDao projectProductDao;

	public void validate(ProductDetailRequest request, Long versionId) {
		if (!productFamilyDao.existsByIdAndActive(request.getProductFamilyId(), true)) {
			throw new ValidationException(
					String.format("Product family (%s) not exist", mask(request.getProductFamilyId())));
		}
		if (!productCodeDao.existsByIdAndProductFamilyIdAndActive(request.getProductCodeId(),
				request.getProductFamilyId(), true)) {
			throw new ValidationException(String.format("This Product code (%s) not exist for product family (%s)",
					mask(request.getProductCodeId()), mask(request.getProductFamilyId())));
		}

		if (versionId != null && productDetailDao.existsByProductFamilyIdAndProductCodeIdAndVersionIdAndActive(
				request.getProductFamilyId(), request.getProductCodeId(), versionId, true)) {
			throw new ValidationException(String.format(
					"This product family (%s), product code (%s) and Version (%s) already exist",
					mask(request.getProductFamilyId()), mask(request.getProductCodeId()), request.getVersion()));
		}
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_FETCH)
	public List<ProductFamilyResponse> findByActiveTrue() {
		List<ProductDetailResponse> productDetailResponseList = productDetailDao
				.findByActiveOrderByProductFamilyNameAscProductCodeNameAscVersionVersionAsc(true);
		Long familyId = null, codeId = null;
		List<ProductFamilyResponse> productFamilies = new ArrayList<>();
		ProductFamilyResponse productFamily = null;
		ProductCodeResponse productCode = null;
		VersionResponse version;
		for (ProductDetailResponse productDetail : productDetailResponseList) {
			if (!productDetail.getProductFamilyId().equals(familyId)) {
				productFamily = new ProductFamilyResponse(unmask(productDetail.getProductFamilyId()),
						productDetail.getProductFamilyName(), productDetail.getProductFamilyCode(),
						productDetail.getProductFamilyDescription(), null);
				productCode = new ProductCodeResponse(unmask(productDetail.getProductCodeId()),
						productDetail.getProductCodeName());
				version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
				version.setProductDetailId(unmask(productDetail.getId()));
				version.setDescription(productDetail.getDescription());
				productCode.setVersions(new ArrayList<>());
				productCode.getVersions().add(version);
				productFamily.setProductCodes(new ArrayList<>());
				productFamily.getProductCodes().add(productCode);
				productFamilies.add(productFamily);
			} else {
				if (!productDetail.getProductCodeId().equals(codeId)) {
					productCode = new ProductCodeResponse(unmask(productDetail.getProductCodeId()),
							productDetail.getProductCodeName());
					version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
					version.setProductDetailId(unmask(productDetail.getId()));
					version.setDescription(productDetail.getDescription());
					productCode.setVersions(new ArrayList<>());
					productCode.getVersions().add(version);
					productFamily.getProductCodes().add(productCode);
				} else {
					version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
					version.setProductDetailId(unmask(productDetail.getId()));
					version.setDescription(productDetail.getDescription());
					productCode.getVersions().add(version);
				}
			}
			familyId = productDetail.getProductFamilyId();
			codeId = productDetail.getProductCodeId();
		}
		return productFamilies;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_FETCH)
	public List<ProductFamilyResponse> findAll() {
		List<ProductDetailResponse> productDetailResponseList = productDetailDao
				.findByOrderByProductFamilyNameAscProductCodeNameAscVersionVersionAsc();
		Long familyId = null, codeId = null;
		List<ProductFamilyResponse> productFamilies = new ArrayList<>();
		ProductFamilyResponse productFamily = null;
		ProductCodeResponse productCode = null;
		VersionResponse version;
		for (ProductDetailResponse productDetail : productDetailResponseList) {
			if (!productDetail.getProductFamilyId().equals(familyId)) {
				productFamily = new ProductFamilyResponse(unmask(productDetail.getProductFamilyId()),
						productDetail.getProductFamilyName(), productDetail.getProductFamilyCode(),
						productDetail.getProductFamilyDescription(), null);
				productCode = new ProductCodeResponse(unmask(productDetail.getProductCodeId()),
						productDetail.getProductCodeName());
				version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
				version.setProductDetailId(unmask(productDetail.getId()));
				version.setDescription(productDetail.getDescription());
				version.setActive(productDetail.getActive());
				productCode.setVersions(new ArrayList<>());
				productCode.getVersions().add(version);
				productFamily.setProductCodes(new ArrayList<>());
				productFamily.getProductCodes().add(productCode);
				productFamilies.add(productFamily);
			} else {
				if (!productDetail.getProductCodeId().equals(codeId)) {
					productCode = new ProductCodeResponse(unmask(productDetail.getProductCodeId()),
							productDetail.getProductCodeName());
					version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
					version.setProductDetailId(unmask(productDetail.getId()));
					version.setDescription(productDetail.getDescription());
					version.setActive(productDetail.getActive());
					productCode.setVersions(new ArrayList<>());
					productCode.getVersions().add(version);
					productFamily.getProductCodes().add(productCode);
				} else {
					version = new VersionResponse(unmask(productDetail.getVersionId()), productDetail.getVersionName());
					version.setProductDetailId(unmask(productDetail.getId()));
					version.setDescription(productDetail.getDescription());
					version.setActive(productDetail.getActive());
					productCode.getVersions().add(version);
				}
			}
			familyId = productDetail.getProductFamilyId();
			codeId = productDetail.getProductCodeId();
		}
		return productFamilies;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_CREATE)
	public ProductDetailResponse save(ProductDetailRequest request) {
		Long versionId = versionDao.findIdByNameAndActive(request.getVersion(), true);
		validate(request, versionId);
		if (versionId == null) {
			Version version = new Version(request.getVersion());
			versionDao.save(version);
			versionId = version.getId();
		}
		ProductDetail productDetail = productDetailDao.save(new ProductDetail(request.getDescription(),
				request.getProductCodeId(), request.getProductFamilyId(), versionId));
		ProductDetailResponse response = productDetailDao.findResponseById(productDetail.getId());
		return response;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_UPDATE)
	public ProductDetailResponse update(Long id, ProductDetailRequest request) {
		Long unmaskId = unmask(id);
		ProductDetail productDetail = productDetailDao.findById(unmaskId).get();
		if (productDetail == null) {
			throw new NotFoundException(String.format("Product Detail (%s) not found", id));
		}
		Long versionId = versionDao.findIdByNameAndActive(request.getVersion(), true);
		validate(request, versionId != null ? (productDetail.getVersion().getId().equals(versionId) ? null : versionId)
				: versionId);
		if (!request.getProductFamilyId().equals(productDetail.getProductFamily().getId())) {
			throw new ValidationException("Product family can't be update");
		}
		if (!request.getProductCodeId().equals(productDetail.getProductCode().getId())) {
			productDetail.settProductCodeId(request.getProductCodeId());
		}
		if (versionId == null) {
			Version version = new Version(request.getVersion());
			versionDao.save(version);
			versionId = version.getId();
		}
		if (request.getDescription() != null) {
			productDetail.setDescription(request.getDescription());
		}
		if (!productDetail.getVersion().getId().equals(versionId))
			productDetail.settVersionId(versionId);
		productDetailDao.save(productDetail);
		ProductDetailResponse response = productDetailDao.findResponseById(unmaskId);
		return response;
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_DELETE)
	public SuccessResponse activate(Long id) {
		Long unmaskId = unmask(id);
		ProductDetailResponse productDetail = productDetailDao.findResponseById(unmaskId);
		if (productDetail == null) {
			throw new NotFoundException(String.format("Product Detail (%s) not found", id));
		}
		if (!productFamilyDao.existsByIdAndActive(unmask(productDetail.getProductFamilyId()), true)) {
			throw new ValidationException(
					String.format("Product family (%s) isn't active currently. Please activate product family.",
							productDetail.getProductFamilyName()));
		}

		if (!productCodeDao.existsByIdAndProductFamilyIdAndActive(unmask(productDetail.getProductCodeId()),
				unmask(productDetail.getProductFamilyId()), true)) {
			throw new ValidationException(
					String.format("Product code (%s) isn't active currently. Please activate product code.",
							productDetail.getProductCodeName()));
		}
		int rows = productDetailDao.activate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Product detail {} successfully activated", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Product detail  successfully activated");
	}

	@Override
	@Secured(AuthorityUtils.PRODUCT_DETAIL_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!productDetailDao.existsByIdAndActive(unmaskId, true)) {
			throw new NotFoundException(String.format("Product Detail (%s) not found", id));
		}
		int rows = productDetailDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Product detail {} successfully deactivated", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Product detail  successfully deactivated");
	}

}
