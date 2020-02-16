package com.badminton.club.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.OtherData;

public interface OtherDataRepository extends JpaRepository<OtherData, Integer>{
	/**
	 * 取得所有活動的其他資料
	 */
	@Query(value = "select * from other_data where avt_no=? ", nativeQuery = true)
	List<OtherData> findAllOtherDataActivity(int avtNo);
}
