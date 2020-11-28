package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.view.project.ProjectResponse;

@Repository
public interface ProjectJpaDao extends JpaRepository<Project, Long> {

	public Boolean existsByIdAndActive(Long id, Boolean active);
	
	public Boolean existsByProjectTypeId(Long id);

	public Project findByCustomerName(String name);

	public ProjectResponse findResponseById(Long id);

	public List<ProjectResponse> findByActive(Boolean active);

	public List<ProjectResponse> findByCustomerEmailAndActive(String customerEmail, Boolean active);

	public List<ProjectResponse> findByProjectManagerIdAndActive(Long projectManagerId, Boolean active);

	public Boolean existsByCustomerContactNoAndActive(String customerContactNo, Boolean active);

	public ProjectResponse findByIdAndActive(Long unmaskProjectId, Boolean active);

	public ProjectResponse findByCustomerContactNoAndActive(String customerContactNo, Boolean active);

	@Modifying
	@Query(value = "update Project set customerContactNo=?2, isEmailSend=?3, customerName=?4, projectManager.id=?5, modifiedBy.id =?6, modifiedAt =?7 where id =?1")
	public int update(Long unmaskId, String customerContactNo, Boolean isEmailSend, String customerName,
			Long projectManagerId, Long userId, Date date);

	public ProjectResponse findByIdAndCustomerEmailAndActive(Long unmaskId, String email, Boolean active);

	public ProjectResponse findByIdAndProjectManagerIdAndActive(Long unmaskId, Long userId, Boolean active);

	@Modifying
	@Query(value = "update Project set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long unmaskId, Long userId, Date date);

}
