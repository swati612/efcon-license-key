package com.nxtlife.efkon.license.dao.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.common.UserRoleKey;
import com.nxtlife.efkon.license.entity.user.UserRole;

public interface UserRoleJpaDao extends JpaRepository<UserRole, UserRoleKey> {

	@Modifying
	@Query(value = "insert into user_role(user_id,role_id) values (?1,?2)", nativeQuery = true)
	public int save(Long userId, Long roleId);

	public Boolean existsByUserIdAndRoleName(Long userId, String roleName);

	@Query(value = "select role_id from user_role where user_id=?1", nativeQuery = true)
	public Set<Long> findRoleIdsByUserId(Long userId);

	@Query(value = "select role_id from user_role u_r inner join role r on u_r.role_id=r.id where user_id=?1 and r.active = ?2", nativeQuery = true)
	public Set<Long> findRoleIdsByUserIdAndRoleActive(Long userId, Boolean roleActive);

	@Query(value = "SELECT * FROM user_role u_r inner join role r where u_r.user_id = ?1 and u_r.role_id=r.id", nativeQuery = true)
	public List<UserRole> findByUserId(Long userId);

//	@Query(value = "select user.id from UserRole where role_id=:roleId")
//	public Set<Long> findUserIdsByRoleId(@Param("roleId") Long roleId);

	@Modifying
	@Query(value = "delete from user_role where user_id=?1 and role_id=?2", nativeQuery = true)
	public int delete(Long userId, Long roleId);

	@Query(value = "select U.id from user_role UR inner join user U on UR.user_id=U.id where UR.role_id=?1 and U.active=?2 ", nativeQuery = true)
	public Set<Long> findUserIdsByRoleIdAndActive(Long roleId, Boolean active);
}
