package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.product.ProductFamily;
import com.nxtlife.efkon.license.view.product.ProductFamilyResponse;

@Repository
public interface ProductFamilyJpaDao extends JpaRepository<ProductFamily, Long> {

	public Boolean existsByName(String name);

	public Boolean existsByIdAndActive(Long id, Boolean active);

	public ProductFamilyResponse findResponseById(Long id);

	public List<ProductFamilyResponse> findByActive(Boolean active);

	@Query(value = "select id from ProductFamily where  name = ?1")
	public Long findIdByName(String name);

	@Modifying
	@Query("update ProductFamily set name=?1, description=?2, code=?3, modified_by =?5, modified_at =?6 where id = ?4 ")
	public int updateById(String name, String description, String code, Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductFamily set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductFamily set active = true, modified_by =?2, modified_at =?3 where id =?1")
	public int reactivate(Long unmaskId, Long userId, Date date);

}
