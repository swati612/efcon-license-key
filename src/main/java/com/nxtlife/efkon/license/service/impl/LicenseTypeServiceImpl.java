package com.nxtlife.efkon.license.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.LicenseTypeJpaDao;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.enums.LicenseTypeEnum;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.LicenseTypeService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

@Service("licenseTypeServiceImpl")
@Transactional
public class LicenseTypeServiceImpl extends BaseService implements LicenseTypeService {

	@Autowired
	private LicenseTypeJpaDao licenseTypeJpaDao;

	private static Logger logger = LoggerFactory.getLogger(LicenseTypeServiceImpl.class);

	@PostConstruct
	private void init() {
		for (LicenseTypeEnum licenseType : LicenseTypeEnum.values()) {
			if (!licenseTypeJpaDao.existsByName(licenseType.name())) {
				licenseTypeJpaDao.save(
						new LicenseType(licenseType.name(), licenseType.getCode(), licenseType.getDefaultLimit()));
			}
		}
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_FETCH)
	public List<LicenseTypeResponse> findAll() {
		List<LicenseTypeResponse> responseList = licenseTypeJpaDao.findAll().stream().map(LicenseTypeResponse::get)
				.collect(Collectors.toList());
		if (responseList.isEmpty()) {
			throw new NotFoundException("No License Type found");
		}
		return responseList;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_FETCH)
	public List<LicenseTypeResponse> findAllActivated() {
		List<LicenseTypeResponse> responseList = licenseTypeJpaDao.findByActiveTrue();
		if (responseList.isEmpty()) {
			throw new NotFoundException("No License Type found");
		}
		return responseList;
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_UPDATE)
	public SuccessResponse update(Long id, Integer monthCount) {
		Long unmaskId = unmask(id);
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(unmaskId);
		if (!licenseType.isPresent()) {
			throw new NotFoundException(String.format("This license type(%s) not exists", id));
		}
		licenseType.get().setMaxMonthCount(monthCount);
		licenseTypeJpaDao.save(licenseType.get());
		return new SuccessResponse(HttpStatus.OK.value(), "License type successfully updated");
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!licenseTypeJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("License Type (%s) not found", id));
		}
		int rows = licenseTypeJpaDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("License type {} successfuly deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "License type deleted successfully");

	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_UPDATE)
	public LicenseTypeResponse reactivate(Long id) {
		Long unmaskId = unmask(id);
		if (!licenseTypeJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("License Type (%s) not found", id));
		}
		int rows = licenseTypeJpaDao.reactivate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("License type {} successfuly deleted", unmaskId);
		}
		LicenseTypeResponse response = licenseTypeJpaDao.findResponseById(unmaskId);
		return response;
	}

}
