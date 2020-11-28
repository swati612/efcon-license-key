package com.nxtlife.efkon.license.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.nxtlife.efkon.license.dao.jpa.RenewConfigurationJpaDao;
import com.nxtlife.efkon.license.entity.common.RenewConfiguration;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.service.RenewConfigurationService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.RenewConfigurationRequest;
import com.nxtlife.efkon.license.view.RenewConfigurationResponse;

@Service
public class RenewConfigurationServieImpl implements RenewConfigurationService {

	@Autowired
	private RenewConfigurationJpaDao renewConfigurationJpaDao;

	@PostConstruct
	public void init() {
		if (renewConfigurationJpaDao.findAll().isEmpty()) {
			renewConfigurationJpaDao.save(new RenewConfiguration(30, true));
		}
	}

	@Override
	@Secured(AuthorityUtils.RENEW_CONFIGURATION_UPDATE)
	public RenewConfigurationResponse update(RenewConfigurationRequest request) {
		List<RenewConfiguration> renewConfigurations = renewConfigurationJpaDao.findAll();
		if (!renewConfigurations.isEmpty()) {
			RenewConfiguration renewConfiguration = renewConfigurations.get(0);
			renewConfiguration.setShowBeforeDays(request.getShowBeforeDays());
			renewConfiguration.setStartDateChange(request.getStartDateChange());
			renewConfigurationJpaDao.save(renewConfiguration);
			return RenewConfigurationResponse.get(renewConfiguration);
		} else {
			throw new NotFoundException("Renew configuration not found");
		}

	}

	@Override
	public RenewConfigurationResponse find() {
		List<RenewConfiguration> renewConfigurations = renewConfigurationJpaDao.findAll();
		if (!renewConfigurations.isEmpty()) {
			return RenewConfigurationResponse.get(renewConfigurations.get(0));
		}else {
			throw new NotFoundException("Renew configuration not found");
		}
	}

}
