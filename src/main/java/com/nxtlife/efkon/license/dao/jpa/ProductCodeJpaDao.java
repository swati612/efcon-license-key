package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.product.ProductCode;
import com.nxtlife.efkon.license.view.product.ProductCodeResponse;

public interface ProductCodeJpaDao extends JpaRepository<ProductCode, Long> {

	public List<ProductCodeResponse> findByProductFamilyIdAndActive(Long productFamilyId, Boolean active);

	public ProductCodeResponse findResponseById(Long id);

	public Boolean existsByIdAndProductFamilyIdAndActive(Long id, Long productFamilyId, Boolean active);

	public Boolean existsByName(String name);

	@Query("select name from ProductCode where id=?1")
	public String findNameById(Long id);

	@Query("select id from ProductCode where name=?1")
	public Long findIdByName(String name);

	@Query("select name from ProductCode")
	public List<String> findAllNames();

	@Query("select id from ProductCode where product_Family_id = ?1 and active=?2")
	public List<Long> findAllIdsByProductFamilyIdAndActive(Long productFamilyId, Boolean active);

	@Modifying
	@Query("update ProductCode set name=?1, modified_by =?3, modified_at =?4 where id = ?2 ")
	public int updateNameById(String name, Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductCode set active = false, modified_by =?2, modified_at =?3 where id in ?1")
	public int deleteByIds(List<Long> ids, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductCode set active = false, modified_by =?2, modified_at =?3 where productFamily.id =?1")
	public int deleteByProductFamilyId(Long unmaskId, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductCode set active = true, modified_by =?2, modified_at =?3 where productFamily.id =?1")
	public int updateByProductFamilyId(Long unmaskId, Long userId, Date date);

	public List<ProductCodeResponse> findByProductFamilyId(Long unmask);

}
