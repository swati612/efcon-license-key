package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.product.ProductDetail;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;

@Repository
public interface ProductDetailJpaDao extends JpaRepository<ProductDetail, Long> {

	public Boolean existsByIdAndActive(Long id, Boolean active);

	public ProductDetailResponse findResponseById(Long id);

	public List<ProductDetailResponse> findByOrderByProductFamilyNameAscProductCodeNameAscVersionVersionAsc();

	public List<ProductDetailResponse> findByActiveOrderByProductFamilyNameAscProductCodeNameAscVersionVersionAsc(
			Boolean active);

	public List<ProductDetail> findAllByActive(Boolean active);

	public Boolean existsByVersionIdAndActive(Long versionId, Boolean active);

	@Modifying
	@Query(value = "update ProductCode set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int deleteByProductFamilyId(Long id, Long userId, Date date);

	public Boolean existsByProductFamilyIdAndActive(Long productFamilyId, Boolean active);

	public Boolean existsByProductFamilyIdAndProductCodeIdAndVersionIdAndActive(Long productFamilyId,
			Long productCodeId, Long versionId, Boolean active);

	public Optional<ProductDetail> findById(Long id);

	@Modifying
	@Query(value = "update ProductDetail set active = true, modified_by =?2, modified_at =?3 where id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update ProductDetail set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

	public ProductDetailResponse findByIdAndActive(Long id, Boolean active);

}
