package com.nxtlife.efkon.license.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.efkon.license.entity.user.Authority;
import com.nxtlife.efkon.license.view.user.security.AuthorityResponse;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityJpaDao extends JpaRepository<Authority, Long> {
	
	public Boolean existsByName(String name);
	
	public AuthorityResponse findResponseById(Long id);
	
	public List<AuthorityResponse> findByAuthorityRolesRoleId(Long roleId);

	@Query(value="select id from Authority")
	public List<Long> findAllIds();


}
