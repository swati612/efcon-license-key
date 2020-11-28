package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.project.product.ProjectProductLicenseRequest;
import com.nxtlife.efkon.license.enums.LicenseRequestStatus;
import com.nxtlife.efkon.license.view.project.product.ProjectProductLicenseRequestResponse;

public interface ProjectProductLicenseRequestJpaDao extends JpaRepository<ProjectProductLicenseRequest, Long> {

	public ProjectProductLicenseRequestResponse findByIdAndActive(Long unmaskId, boolean b);

	@Modifying
	@Query(value = "update ProjectProductLicenseRequest set licenseCount=?2, modifiedBy.id =?3, modifiedAt =?4 where id =?1")
	public int update(Long unmaskId, Integer licenseCount, Long userId, Date date);

	public ProjectProductLicenseRequestResponse findResponseById(Long unmaskId);

	public ProjectProductLicenseRequestResponse findByIdAndProjectProductProjectCustomerEmailAndActive(Long unmaskId,
			String email, Boolean active);

	public ProjectProductLicenseRequestResponse findByIdAndProjectProductProjectProjectManagerIdAndActive(Long unmaskId,
			Long userId, Boolean active);

	@Modifying
	@Query(value = "update ProjectProductLicenseRequest set active = false, modifiedBy.id=?2, modifiedAt=?3 where id =?1")
	public int delete(Long unmaskId, Long userId, Date date);

	public LicenseRequestStatus findStatusByIdAndProjectProductProjectCustomerEmailAndActive(Long unmaskId,
			String email, Boolean active);

	public LicenseRequestStatus findStatusByIdAndProjectProductProjectProjectManagerIdAndActive(Long unmaskId,
			Long userId, Boolean active);

	@Query(value = "select status from ProjectProductLicenseRequest where id =?1 and active=?2")
	public LicenseRequestStatus findStatusByIdAndActive(Long unmaskId, Boolean active);

	@Modifying
	@Query(value = "update ProjectProductLicenseRequest set status=?2, licenseCount=?3, modifiedBy.id =?4, modifiedAt =?5 where id =?1")
	public int update(Long unmaskId, LicenseRequestStatus status, Integer licenseCount, Long userId, Date date);

	public Integer findLicenseCountById(Long unmaskId);

	public List<ProjectProductLicenseRequestResponse> findByStatusAndProjectProductProjectCustomerEmailAndActive(
			LicenseRequestStatus status, String email, Boolean active);

	public List<ProjectProductLicenseRequestResponse> findByStatusAndProjectProductProjectProjectManagerIdAndActive(
			LicenseRequestStatus pending, Long userId, boolean b);

	public List<ProjectProductLicenseRequestResponse> findByStatusAndActive(LicenseRequestStatus pending, boolean b);

	@Modifying
	@Query(value = "update ProjectProductLicenseRequest set status=?2, modifiedBy.id =?3, modifiedAt =?4 where id =?1")
	public int update(Long unmaskId, LicenseRequestStatus status, Long userId, Date date);

}
