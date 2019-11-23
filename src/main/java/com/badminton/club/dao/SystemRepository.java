package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.System;

public interface SystemRepository extends JpaRepository<System, Integer> {
	
	@Query(value = "select * from systems where sys_name=?", nativeQuery = true)
	System findSystem(String sysName);

}
