package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.badminton.club.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
