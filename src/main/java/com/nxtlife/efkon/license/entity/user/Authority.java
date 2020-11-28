package com.nxtlife.efkon.license.entity.user;

import com.nxtlife.efkon.license.entity.common.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "authority", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Authority extends BaseEntity implements GrantedAuthority {

    @NotEmpty(message = "name can't be empty")
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authority")
    private List<RoleAuthority> authorityRoles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoleAuthority> getAuthorityRoles() {
        return authorityRoles;
    }

    public void setAuthorityRoles(List<RoleAuthority> authorityRoles) {
        this.authorityRoles = authorityRoles;
    }

    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorityRoles == null) ? 0 : authorityRoles.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Authority other = (Authority) obj;
        if (authorityRoles == null) {
            if (other.authorityRoles != null)
                return false;
        } else if (!authorityRoles.equals(other.authorityRoles))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
