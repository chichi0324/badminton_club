package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the roles database table.
 * 角色
 */
@Entity
@Table(name="roles")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role  {
	private static final long serialVersionUID = 1L;

	//角色權限編號
	@Id
	private String authority;

	//角色名稱
	@Column(name="role_name")
	private String roleName;

	//bi-directional many-to-one association to Authority
	@OneToMany(mappedBy="role")
	private List<Authority> authorities;

	public Role() {
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public Authority addAuthority(Authority authority) {
		getAuthorities().add(authority);
		authority.setRole(this);

		return authority;
	}

	public Authority removeAuthority(Authority authority) {
		getAuthorities().remove(authority);
		authority.setRole(null);

		return authority;
	}

}