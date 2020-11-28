package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

public interface LicenseTypeJpaDao extends JpaRepository<LicenseType, Long> {

	public Boolean existsByName(String name);

	@Query(value = "select name from LicenseType where id =?1")
	public String findNameById(Long id);

	public LicenseTypeResponse findResponseById(Long unmaskId);

	public List<LicenseTypeResponse> findByActiveTrue();

	@Modifying
	@Query(value = "update LicenseType set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long unmaskId, Long userId, Date date);

	@Modifying
	@Query(value = "update LicenseType set active = true, modified_by =?2, modified_at =?3 where id =?1")
	public int reactivate(Long unmaskId, Long userId, Date date);

}
