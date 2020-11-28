package com.nxtlife.efkon.license.entity.user;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.UserRoleKey;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_role")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class UserRole implements Serializable {

	@EmbeddedId
	private UserRoleKey userRoleKey;

	@MapsId("roleId")
	@ManyToOne
	private Role role;

	@MapsId("userId")
	@ManyToOne
	private User user;

	public UserRole() {
		super();
	}

	public UserRole(UserRoleKey userRoleKey, Role role, User user) {
		super();
		this.userRoleKey = userRoleKey;
		this.role = role;
		this.user = user;
	}

	public UserRole(Long userId,Long roleId)
	{
		this.userRoleKey=new UserRoleKey(userId,roleId);
		if(userId!=null)
		{
			this.user=new User();
			this.user.setId(userId);
		}
		if(roleId!=null)
		{
			this.role=new Role();
			this.role.setId(roleId);
		}
	}

	public UserRoleKey getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(UserRoleKey userRoleKey) {
		this.userRoleKey = userRoleKey;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((userRoleKey == null) ? 0 : userRoleKey.hashCode());
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
		UserRole other = (UserRole) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userRoleKey == null) {
			if (other.userRoleKey != null)
				return false;
		} else if (!userRoleKey.equals(other.userRoleKey))
			return false;
		return true;
	}

	
}
