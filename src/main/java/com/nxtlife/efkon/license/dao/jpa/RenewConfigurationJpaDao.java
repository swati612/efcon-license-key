package com.nxtlife.efkon.license.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.common.RenewConfiguration;
import com.nxtlife.efkon.license.view.RenewConfigurationResponse;

@Repository
public interface RenewConfigurationJpaDao extends JpaRepository<RenewConfiguration, Long> {
	
	public List<RenewConfigurationResponse> findByActiveTrue();

}
