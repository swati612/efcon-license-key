package com.nxtlife.efkon.license.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.ProductDetailJpaDao;
import com.nxtlife.efkon.license.dao.jpa.VersionJpaDao;
import com.nxtlife.efkon.license.entity.version.Version;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.VersionService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.version.VersionRequest;
import com.nxtlife.efkon.license.view.version.VersionResponse;

@Service("versionServiceImpl")
@Transactional
public class VersionServiceImpl extends BaseService implements VersionService {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(VersionServiceImpl.class);

	@Autowired
	public VersionJpaDao versionDao;

	@Autowired
	private ProductDetailJpaDao productDetailJpaDao;

	public void validate(VersionRequest request) {
		if (versionDao.existsByVersionAndActive(request.getVersion(), true)) {
			throw new ValidationException(String.format("Version (%s) already exist", request.getVersion()));
		}
	}

	@Override
	@Secured(AuthorityUtils.VERSION_CREATE)
	public VersionResponse save(VersionRequest request) {
		validate(request);
		Version version = request.toEntity();
		version = versionDao.save(version);
		return VersionResponse.get(version);
	}

	@Override
	@Secured(AuthorityUtils.VERSION_FETCH)
	public List<VersionResponse> findAll() {
		return versionDao.findByActive(true);
	}

	@Override
	@Secured(AuthorityUtils.VERSION_UPDATE)
	public VersionResponse update(Long id, VersionRequest request) {
		Long unmaskId = unmask(id);
		Version version = versionDao.findById(unmaskId).orElse(null);
		if (version == null) {
			throw new NotFoundException(String.format("Version (%s) not found", id));
		}
		if (request.getVersion() != null && !version.getVersion().equalsIgnoreCase(request.getVersion())) {
			version.setVersion(request.getVersion());
			versionDao.save(version);
		}
		return VersionResponse.get(version);
	}

	@Override
	@Secured(AuthorityUtils.VERSION_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!versionDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("Version (%s) not found", id));
		}
		if (productDetailJpaDao.existsByVersionIdAndActive(unmaskId, true)) {
			throw new ValidationException("You can't delete this version because its already in use");
		}
		int rows = versionDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Version {} successfully deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Version successfully deleted");
	}
}
