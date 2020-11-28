package com.nxtlife.efkon.license.dao;

import com.nxtlife.efkon.license.entity.user.RoleAuthority;

import java.util.List;

public interface RoleAuthorityDao {

    public void save(RoleAuthority roleAuthority);

    public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds);
}
