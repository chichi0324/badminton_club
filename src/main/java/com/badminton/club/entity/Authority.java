package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the authorities database table.
 * 權限
 */
@Entity
@Table(name="authorities")
@NamedQuery(name="Authority.findAll", query="SELECT a FROM Authority a")
public class Authority  {
	private static final long serialVersionUID = 1L;

	//權限編號
	@Id
	@Column(name="authority_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int authorityNo;

	//bi-directional many-to-one association to Role
	//角色權限編號
	@ManyToOne
	@JoinColumn(name="authority")
	private Role role;

	//bi-directional many-to-one association to User
	//會員帳號
	@ManyToOne
	@JoinColumn(name="username")
	private User user;

	public Authority() {
	}

	public int getAuthorityNo() {
		return this.authorityNo;
	}

	public void setAuthorityNo(int authorityNo) {
		this.authorityNo = authorityNo;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}