package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.project.ProjectType;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

@Repository
public interface ProjectTypeJpaDao extends JpaRepository<ProjectType, Long> {

	public ProjectType findByName(String name);

	public Boolean existsByName(String name);

	public Boolean existsByNameAndActive(String name, Boolean active);

	public ProjectTypeResponse findResponseById(Long id);

	@Modifying
	@Query(value = "update ProjectType set active = false, modifiedBy.id = ?2, modifiedAt = ?3 where id = ?1")
	public int deleteById(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update ProjectType set active = true, modifiedBy.id = ?2, modifiedAt = ?3 where id = ?1")
	public int reactivate(Long unmaskId, Long userId, Date date);

	public boolean existsByIdAndActive(Long unmaskId, boolean b);

}
