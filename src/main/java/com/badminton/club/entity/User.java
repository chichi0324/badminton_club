package com.badminton.club.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the users database table.
 * 會員帳密
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User  {
	private static final long serialVersionUID = 1L;

	//會員帳號
	@Id
	private String username;

	//會員資格
	private byte enabled;

	//會員密碼
	private String password;

	//bi-directional many-to-one association to Authority
	@OneToMany(mappedBy="user",cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
	private List<Authority> authorities;

	public User() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte getEnabled() {
		return this.enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public Authority addAuthority(Authority authority) {
		getAuthorities().add(authority);
		authority.setUser(this);

		return authority;
	}

	public Authority removeAuthority(Authority authority) {
		getAuthorities().remove(authority);
		authority.setUser(null);

		return authority;
	}

}