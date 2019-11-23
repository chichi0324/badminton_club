package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.badminton.club.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
