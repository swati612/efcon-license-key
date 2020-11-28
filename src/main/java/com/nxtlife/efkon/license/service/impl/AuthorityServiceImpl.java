package com.nxtlife.efkon.license.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.AuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleAuthorityJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.license.entity.user.Authority;
import com.nxtlife.efkon.license.entity.user.RoleAuthority;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.service.AuthorityService;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.user.security.AuthorityResponse;

@Service("authorityServiceImpl")
@Transactional
public class AuthorityServiceImpl extends BaseService implements AuthorityService {

	private static Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);

	@Autowired
	private AuthorityJpaDao authorityDao;

	@Autowired
	private RoleJpaDao roleDao;
	
	@Autowired
	private RoleAuthorityJpaDao roleAuthorityDao;

	/**
	 * 
	 * it will loads the authorities defined in AuthorityUtil at the time when
	 * program runs If Authority not exist then Logger will give info
	 *
	 */
	@PostConstruct
	public void init() {
		Field[] fields = AuthorityUtils.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
				logger.info("Found authority {} ", f.getName());
				Authority authority;
				if (!authorityDao.existsByName(f.getName())) {
					authority = new Authority();
					authority.setName(f.getName());
					authorityDao.save(authority);
					logger.info("Not in db, saved {}", f.getName());
					Set<Long> roleIds = roleDao.findIdsByName("SuperAdmin");
					for (Long roleId : roleIds) {
						roleAuthorityDao.save(new RoleAuthority(roleId, authority.getId()));
					}

				}
			}
		}
	}

	/**
	 * save the Authority
	 *
	 * @Param authortiy call save method of jpa
	 * @return <tt>RoleResponse</tt>
	 */
	@Override
	public void save(Authority authority) {
		authorityDao.save(authority);
	}

	/**
	 * return an Authority by given id it wil call findById(id) of jpa which
	 * return optional(object) <tt>isPresent()</tt> method tells whether
	 * findById return an object or not.
	 *
	 * it will throw NotFoundException if given not exist
	 */
	@Override
	public Authority findById(long id) {
		Optional<Authority> authority = authorityDao.findById(id);
		if (authority.isPresent()) {
			return authority.get();
		} else {
			logger.error("Authority {} not found", id);
			throw new NotFoundException(String.format("Authority (%s) not found", id));
		}
	}

	/**
	 * return a list of authorities. call findAll() method of jpa which return
	 * list of objects
	 *
	 *
	 * @return List of <tt>AuthorityResponse</tt>
	 */
	@Override
	@Secured(AuthorityUtils.AUTHORITY_FETCH)
	public List<AuthorityResponse> getAllAuthorities() {
		return authorityDao.findAll().stream().map(AuthorityResponse::new).collect(Collectors.toList());
	}

	/**
	 * return a list of Authorities available by the given role id.
	 * <tt>unmask()</tt>unmask the given id <tt>existById</tt> return true if
	 * given id exist in database throw NotFoundException role not exist
	 * <tt>findByAuthorityRolesRoleId</tt> return list of authorities
	 *
	 * @Param roleId
	 * @return List of <tt>AuthorityResponse</tt>
	 */
	@Override
	@Secured(AuthorityUtils.AUTHORITY_FETCH)
	public List<AuthorityResponse> getAllAuthoritiesByRoleId(Long roleId) {
		Long unmaskRoleId = unmask(roleId);
		Boolean exist = roleDao.existsById(unmaskRoleId);
		if (!exist) {
			logger.error("Role {} not found", unmaskRoleId);
			throw new NotFoundException(String.format("Role (%s) not found", unmaskRoleId));
		}
		List<AuthorityResponse> authorities = authorityDao.findByAuthorityRolesRoleId(unmaskRoleId);
		return authorities;

	}

}
