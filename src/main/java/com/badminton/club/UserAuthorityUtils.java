package com.badminton.club;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.badminton.club.entity.Authority;

public class UserAuthorityUtils {
	private static final List<GrantedAuthority> SURPERMANAGER_ROLES = AuthorityUtils
			.createAuthorityList("ROLE_SURPERMANAGER", "ROLE_MANAGER", "ROLE_MEMBER");
	private static final List<GrantedAuthority> MANAGER_ROLES = AuthorityUtils.createAuthorityList("ROLE_MANAGER",
			"ROLE_MEMBER");
	// 利用Spring提供的AuthorityUtils中createAuthorityList將該群組加入相關roles
	// 以便用一個List變數就儲存所有roles
	private static final List<GrantedAuthority> MEMBER_ROLES = AuthorityUtils.createAuthorityList("ROLE_MEMBER");

	public static Collection<? extends GrantedAuthority> createAuthorities(com.badminton.club.entity.User user) {
		List<Authority> authorities = user.getAuthorities();
		
		List<String> roles = new ArrayList<>();
		for (Authority a : authorities) {
			roles.add(a.getRole().getAuthority());
		}
		if (roles.contains("ROLE_SURPERMANAGER")) {
			return SURPERMANAGER_ROLES;
		}else if (roles.contains("ROLE_MANAGER")) {
			return MANAGER_ROLES;
		}else{
			return MEMBER_ROLES; // 否則則為一般使用者
		}
		

	}
}
