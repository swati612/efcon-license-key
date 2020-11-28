package com.nxtlife.efkon.license.entity.user;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "role")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Role extends BaseEntity implements Serializable {

    @NotEmpty(message = "name can't be empty")
    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<RoleAuthority> roleAuthorities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<UserRole> roleUsers;

    public Role(Long id, String name) {
        super();
        this.setId(id);
        this.name = name;
    }

    public Role(String name) {
        super();
        this.name = name;
    }

    public Role() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<UserRole> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(List<UserRole> roleUsers) {
        this.roleUsers = roleUsers;
    }

    public List<RoleAuthority> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(List<RoleAuthority> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((roleAuthorities == null) ? 0 : roleAuthorities.hashCode());
        result = prime * result + ((roleUsers == null) ? 0 : roleUsers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (roleAuthorities == null) {
            if (other.roleAuthorities != null)
                return false;
        } else if (!roleAuthorities.equals(other.roleAuthorities))
            return false;
        if (roleUsers == null) {
            if (other.roleUsers != null)
                return false;
        } else if (!roleUsers.equals(other.roleUsers))
            return false;
        return true;
    }

}
