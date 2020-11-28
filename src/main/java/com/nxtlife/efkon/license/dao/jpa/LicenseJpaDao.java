package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.enums.LicenseStatus;
import com.nxtlife.efkon.license.view.license.LicenseResponse;

@Repository
public interface LicenseJpaDao extends JpaRepository<License, Long> {

	public Boolean existsByProjectProductIdAndActive(Long projectProductId, Boolean active);

	public Boolean existsByProjectProductIdAndCodeAndAccessIdAndActive(Long projectProductId, String code,
			Long accessId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndActive(Long projectProductId, Boolean active);

	public LicenseResponse findResponseByIdAndActive(Long id, Boolean active);

	public List<LicenseResponse> findByActive(Boolean active);

	public List<LicenseResponse> findAllByActive(Boolean active);

	public LicenseResponse findByIdAndActive(Long unmaskId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndActiveTrue(Long id);

	@Modifying
	@Query(value = "update License set accessId=?2, generatedKey=?3, name=?4, modifiedBy.id=?5, modifiedAt=?6 where id =?1")
	public int update(Long unmaskId, String uniqueAccessId, String generatedKey, String name, Long userId, Date date);

	@Modifying
	@Query(value = "update License set active=?2, status=?3 , modifiedBy.id=?4, modifiedAt=?5 where id =?1")
	public int update(Long unmaskId, boolean b, LicenseStatus replaced, Long userId, Date date);

	public List<LicenseResponse> findByProjectProductProjectIdAndActive(Long unmaskProjectId, Boolean active);

	public List<LicenseResponse> findByProjectProductProjectIdAndProjectProductProjectCustomerEmailAndActive(
			Long unmaskProjectId, String email, Boolean active);

	public List<LicenseResponse> findByProjectProductProjectCustomerEmailAndActive(String email, Boolean active);

	public List<LicenseResponse> findByProjectProductProjectProjectManagerIdAndProjectProductProjectCustomerEmailAndActive(
			Long projectManagerId, String email, Boolean active);

	public List<LicenseResponse> findByProjectProductProjectIdAndProjectProductProjectProjectManagerIdAndActive(
			Long unmaskProjectId, Long userId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndProjectProductProjectIdAndProjectProductProjectProjectManagerIdAndActive(
			Long unmaskProjectProductId, Long unmaskProjectId, Long userId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndProjectProductProjectIdAndProjectProductProjectCustomerEmailAndActive(
			Long unmask, Long unmaskProjectId, String email, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndProjectProductProjectIdAndActive(Long unmask,
			Long unmaskProjectId, Boolean active);

	public LicenseResponse findByIdAndProjectProductProjectCustomerEmailAndActive(Long unmaskId, String email,
			Boolean active);

	public LicenseResponse findByIdAndProjectProductProjectProjectManagerIdAndActive(Long unmaskId, Long userId,
			Boolean active);

	public List<LicenseResponse> findByProjectProductProductDetailIdAndProjectProductProjectIdAndProjectProductProjectProjectManagerIdAndActive(
			Long unmaskedProductId, Long unmaskProjectId, Long userId, Boolean active);

	public List<LicenseResponse> findByProjectProductProductDetailIdAndProjectProductProjectIdAndProjectProductProjectCustomerEmailAndActive(
			Long unmaskedProductId, Long unmaskProjectId, String email, Boolean active);

	public List<LicenseResponse> findByProjectProductProductDetailIdAndProjectProductProjectIdAndActive(
			Long unmaskedProductId, Long unmaskProjectId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndAccessIdIsNullAndActive(Long unmaskId, Boolean active);

	@Modifying
	@Query(value = "update License set active = false, modified_by =?2, modified_at =?3 where project_product_id =?1")
	public int deleteByProjectProductId(Long unmask, Long userId, Date date);

	public List<LicenseResponse> findByProjectProductIdAndAccessIdNotNullAndGeneratedKeyNotNullAndProjectProductProjectCustomerEmailAndActive(
			Long unmaskProjectProductId, String email, boolean b);

	public List<LicenseResponse> findByProjectProductIdAndAccessIdNotNullAndGeneratedKeyNotNullAndProjectProductProjectProjectManagerIdAndActive(
			Long unmaskProjectProductId, Long userId, boolean b);

	public List<LicenseResponse> findByProjectProductIdAndAccessIdNotNullAndGeneratedKeyNotNullAndActive(
			Long unmaskProjectProductId, boolean b);

}
