package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	@Query(value = "select * from users where username=? and enabled=1", nativeQuery = true)
	User findByUserName(String username);
}
