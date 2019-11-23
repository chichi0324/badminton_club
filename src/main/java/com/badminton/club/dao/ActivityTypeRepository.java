package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.badminton.club.entity.ActivityType;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {

}
