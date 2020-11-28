package com.nxtlife.efkon.license.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nxtlife.efkon.license.dao.jpa.SequenceGeneratorJpaDao;
import com.nxtlife.efkon.license.entity.common.SequenceGenerator;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.util.LongObfuscator;

public abstract class BaseService {

	@Autowired
	protected SequenceGeneratorJpaDao sequenceGeneratorJpaDao;
	
	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		if (authentication.getPrincipal() instanceof User) {
			return ((User) authentication.getPrincipal());
		}
		return null;
	}

	public static Long getUserId() {
		User user = getUser();
		return user == null ? null : user.getUserId();
	}

	public static Long mask(final Long number) {
		return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
	}

	public static Long unmask(final Long number) {
		return number != null ? LongObfuscator.INSTANCE.unobfuscate(number) : null;
	}
	
	/**
	 * return generated sequence. first we are fetching the current sequence of
	 * specific type. After that we are concating both of them and incrementing
	 * sequence.
	 *
	 * @param type
	 * @return generated sequence
	 * @throws RuntimeException
	 *             - if sequence didn't increment
	 */
	protected Integer sequenceGenerator(String type) {
		SequenceGenerator sequenceGenerator = sequenceGeneratorJpaDao.findByType(type);
		if (sequenceGenerator == null) {
			sequenceGeneratorJpaDao.save(new SequenceGenerator(type, 2));
			return 1;
		}
		int rows = sequenceGeneratorJpaDao.incrementSequence(sequenceGenerator.getId(), getUser().getUserId(),
				new Date());
		if (rows == 0) {
			throw new RuntimeException("sequence generator row updated is zero");
		}
		return sequenceGenerator.getSequence();
	}
	
	protected void updateSequenceGenerator(String type, Integer sequence) {
		SequenceGenerator sequenceGenerator = sequenceGeneratorJpaDao.findByType(type);
		if (sequenceGenerator == null) {
			sequenceGeneratorJpaDao.save(new SequenceGenerator(type, sequence));
			return ;
		}
		int rows = sequenceGeneratorJpaDao.updateSequence(sequenceGenerator.getId(), sequence, getUser().getUserId(),
				new Date());
		if (rows == 0) {
			throw new RuntimeException("sequence generator row updated is zero");
		}
	}
}
