package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.common.SequenceGenerator;

public interface SequenceGeneratorJpaDao extends JpaRepository<SequenceGenerator, Long> {

	public SequenceGenerator findByType(String type);

	@Modifying
	@Query(value = "update sequence_generator set sequence=sequence+1, modified_by=?2, modified_at=?3 where id=?1", nativeQuery = true)
	public int incrementSequence(long id, Long userId, Date date);

	@Modifying
	@Query(value = "update sequence_generator set sequence=sequence-1, modified_by=?2, modified_at=?3 where id=?1", nativeQuery = true)
	public int decrementSequence(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update sequence_generator set sequence=?2, modified_by=?3, modified_at=?4 where id=?1", nativeQuery = true)
	public int updateSequence(Long id, Integer sequence, Long userId, Date date);
	
}
