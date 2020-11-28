package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.view.user.UserResponse;

public interface UserJpaDao extends JpaRepository<User, Long> {

	public User findByUsername(String username);

	public List<UserResponse> findByUserRolesRoleId(Long roleId);

	public List<UserResponse> findByUserRolesRoleName(String roleName);

	public UserResponse findByEmailAndActive(String email, Boolean active);

	public UserResponse findResponseById(Long userId);

	public Boolean existsByUsername(String username);

	@Query(value = "select id from User where contact_no = ?1 and active = ?2")
	public Long findIdByContactNoAndActive(String contactNo, Boolean active);

	@Query(value = "select id from User where  email = ?1 and active = ?2")
	public Long findIdByEmailAndActive(String email, Boolean active);

	@Query(value = "select email, contact_no as contactNo from user where username= ?1", nativeQuery = true)
	public Map<String, String> findEmailAndContactByUsername(String username);

	@Query(value = "select password from user where id = ?1", nativeQuery = true)
	public String findPasswordById(Long id);

	@Query(value = "select generated_password from user where username = ?1", nativeQuery = true)
	public String findGeneratedPasswordByUsername(String username);

	@Modifying
	@Query(value = "update user set password=:password where username =:username", nativeQuery = true)
	public int setPassword(@Param("username") String username, @Param("password") String password);

	@Modifying
	@Query(value = "update user set password=:password where id =:id", nativeQuery = true)
	public int setPassword(@Param("id") Long id, @Param("password") String password);

	@Modifying
	@Query(value = "update user set generated_password=:password where username =:username", nativeQuery = true)
	public int setGeneratedPassword(@Param("username") String username, @Param("password") String password);

	@Modifying
	@Query(value = "update User set active=true, modified_by =?2, modified_at =?3 where id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update User set active=false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

	public UserResponse findByContactNoAndActive(String customerContactNo, Boolean active);

	public boolean existsByEmail(String email);

}
