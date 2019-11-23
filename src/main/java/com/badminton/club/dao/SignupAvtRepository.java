package com.badminton.club.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.SignupAvt;

public interface SignupAvtRepository extends JpaRepository<SignupAvt, Integer>{
	@Query(value = "select IFNULL(sum(sign_count),0) from signup_avt where avt_no = ? ", nativeQuery = true)
	int getActivitySum(int avtNo);
}
