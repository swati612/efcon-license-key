package com.nxtlife.efkon.license.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.efkon.license.entity.oauth.OauthClientDetails;

public interface OauthClientDetailsJpaDao extends JpaRepository<OauthClientDetails, String> {
	
	public OauthClientDetails findByClientId(String clientId);

}
