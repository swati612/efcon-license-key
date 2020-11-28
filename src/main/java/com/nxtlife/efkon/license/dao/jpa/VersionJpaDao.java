package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.version.Version;
import com.nxtlife.efkon.license.view.version.VersionResponse;

@Repository
public interface VersionJpaDao extends JpaRepository<Version, Long> {

	public List<VersionResponse> findByActive(Boolean active);

	public VersionResponse findResponseById(Long id);

	@Query(value = "select id from Version where version = ?1 and active = ?2")
	public Long findIdByNameAndActive(String version, Boolean active);

	public Boolean existsByVersionAndActive(String version, Boolean active);

	public Boolean existsByIdAndActive(Long id, Boolean active);

	@Modifying
	@Query(value = "update Version set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

}
