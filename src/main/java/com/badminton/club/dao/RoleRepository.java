package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	/**
	 * 依權限取得角色
	 */
	@Query(value = "select * from roles where authority=?", nativeQuery = true)
	Role findRolefromAuthority(String authority);
}
