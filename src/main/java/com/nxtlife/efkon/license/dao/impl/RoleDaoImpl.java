package com.nxtlife.efkon.license.dao.impl;

import com.nxtlife.efkon.license.dao.RoleDao;
import com.nxtlife.efkon.license.entity.user.Role;
import org.springframework.stereotype.Repository;

@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDao<Long, Role> implements RoleDao {

}
