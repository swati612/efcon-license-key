package com.nxtlife.efkon.license.dao.impl;




import com.nxtlife.efkon.license.dao.RoleAuthorityDao;
import com.nxtlife.efkon.license.entity.common.RoleAuthorityKey;
import com.nxtlife.efkon.license.entity.user.RoleAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roleAuthorityDaoImpl")
public class RoleAuthorityDaoImpl extends BaseDao<RoleAuthorityKey, RoleAuthority> implements RoleAuthorityDao {

	@Override
	public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds) {
		return query("delete from RoleAuthority where role_id=:roleId and authority_id in :authorityIds")
				.setParameter("roleId", roleId).setParameter("authorityIds", authorityIds).executeUpdate();
	}

}
