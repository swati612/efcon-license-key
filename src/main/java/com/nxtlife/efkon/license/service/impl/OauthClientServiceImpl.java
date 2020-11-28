package com.nxtlife.efkon.license.service.impl;

import javax.annotation.PostConstruct;

import com.nxtlife.efkon.license.dao.jpa.OauthClientDetailsJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.efkon.license.entity.oauth.OauthClientDetails;
import com.nxtlife.efkon.license.service.OauthClientService;

@Service("oauthClientService")
public class OauthClientServiceImpl implements OauthClientService {

	@Autowired
	private OauthClientDetailsJpaDao oauthClientDetailsDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		if (oauthClientDetailsDao.findByClientId("efkon-atcs") == null) {
			OauthClientDetails oauthClientDetails = new OauthClientDetails();
			oauthClientDetails.setAccessTokenValidity(-1);
			oauthClientDetails.setScope("read,write");
			oauthClientDetails.setClientId("efkon-atcs");
			oauthClientDetails.setAuthorizedGrantTypes("password");
			oauthClientDetails.setAutoapprove("1");
			oauthClientDetails.setClientSecret(passwordEncoder.encode("nxtlife"));
			oauthClientDetails.setRefreshTokenValidity(-1);
			oauthClientDetails.setResourceIds("license-key-api");
			oauthClientDetailsDao.save(oauthClientDetails);
		}
	}

}
