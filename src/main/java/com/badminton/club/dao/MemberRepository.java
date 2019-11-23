package com.badminton.club.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	/**
	 * 取得所有會員
	 * 照會員註冊時間排序(新-->舊)
	 */
	@Query(value = "select * from member order by mem_time desc", nativeQuery = true)
	List<Member> findAllMemberByDesc();
	
}
