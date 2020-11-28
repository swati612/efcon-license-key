package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.user.Role;
import com.nxtlife.efkon.license.view.user.security.RoleResponse;

public interface RoleJpaDao extends JpaRepository<Role, Long> {

    public Role findByName(String name);

	public RoleResponse findResponseById(Long id);

	public Set<RoleResponse> findByRoleUsersUserId(Long userId);


	public Boolean existsByName(String name);

	@Query(value = "select id from role where name=?1", nativeQuery = true)
	public Long findIdByName(String name);

	@Query(value = "select id from role where name=?1", nativeQuery = true)
	public Set<Long> findIdsByName(String name);

	@Query(value = "select id from role", nativeQuery = true)
	public List<Long> getAllIds();

	@Query(value = "select id from Role where active=?1")
	public List<Long> getAllIdsByActive(Boolean active);

	@Modifying
	@Query(value = "update role set name = ?1, modified_by =?3, modified_at =?4 where id = ?2", nativeQuery = true)
	public int updateName(String name, Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update Role set active = true, modified_by =?2, modified_at =?3 where id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update Role set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);


}
