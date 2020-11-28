package com.nxtlife.efkon.license.entity.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.userdetails.UserDetails;

import com.nxtlife.efkon.license.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class User extends BaseEntity implements UserDetails, Serializable {

	@NotNull(message = "name can't be null")
	private String name;

	private String code;

	@NotNull(message = "username can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9._]{3,50}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@NotNull(message = "password can't be null")
	private String password;

	private Boolean active;

	private String generatedPassword;

	private String email;

	private Boolean isEmailSend;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit")
	private String contactNo;

	@Column(columnDefinition = "boolean default false")
	private boolean accountExpired = false;

	@Column(columnDefinition = "boolean default false")
	private boolean accountLocked = false;

	@Column(columnDefinition = "boolean default false")
	private boolean credentialsExpired = false;

	@Column(columnDefinition = "boolean default false")
	private boolean enabled = true;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<UserRole> userRoles;

	@Transient
	private Collection<Authority> authorities;

	@Transient
	private Collection<Role> roles;

	@Transient
	private Long userId;

	public User() {

	}

	public User(
			@NotNull @Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20") String username,
			@NotNull String password, Collection<Authority> authorities) {
		super();
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public User(@NotNull(message = "name can't be null") String name, String code,
			@NotNull(message = "username can't be null") @Pattern(regexp = "^[@A-Za-z0-9._]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20") String username,
			@NotNull(message = "password can't be null") String password, String email, Boolean isEmailSend,
			@Size(min = 10, max = 10) @Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit") String contactNo) {
		this.name = name;
		this.code = code;
		this.username = username;
		this.password = password;
		this.email = email;
		this.isEmailSend = isEmailSend;
		this.contactNo = contactNo;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !isCredentialsExpired();
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsEmailSend() {
		return isEmailSend;
	}

	public void setIsEmailSend(Boolean emailSend) {
		isEmailSend = emailSend;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

}
