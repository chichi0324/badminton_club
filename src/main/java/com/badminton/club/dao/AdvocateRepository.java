package com.badminton.club.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.Advocate;

public interface AdvocateRepository extends JpaRepository<Advocate, Integer> {
	
	@Query(value = "select * from advocate where avt_no=? order by adv_no", nativeQuery = true)
	List<Advocate> findAdvocateByActivityDesc(int avtNo);
}
