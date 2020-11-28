package com.nxtlife.efkon.license.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.common.RoleAuthorityKey;
import com.nxtlife.efkon.license.entity.user.RoleAuthority;

public interface RoleAuthorityJpaDao extends JpaRepository<RoleAuthority, RoleAuthorityKey> {

	public List<RoleAuthority> findAllByRoleId(Long roleId);

	@Query(value = "select authority_id from role_authority where role_id = ?1", nativeQuery = true)
	public List<Long> getAllAuthorityIdsByRoleId(Long roleId);

	@Modifying
	@Query(value="delete from role_authority where role_id=?1 and authority_id=?2", nativeQuery = true)
	public int deleteByRoleIdAndAuthorityId(Long roleId, Long authorityId);

	@Modifying
	@Query(value="delete from role_authority where role_id=?1 and authority_id in ?2", nativeQuery = true)
	public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds);

	@Modifying
	@Query(value="delete from role_authority where role_id=?1", nativeQuery = true)
	public int deleteByRoleId(Long roleId);

}
