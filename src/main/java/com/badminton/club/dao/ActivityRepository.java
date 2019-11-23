package com.badminton.club.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	/**
	 * 取得所有活動
	 * (審核通過，非草稿)
	 * 照活動起始日排序(新-->舊)
	 */
	@Query(value = "select * from activity where avt_pre='1' and avt_edit='0' order by avt_date_s desc", nativeQuery = true)
	List<Activity> findActivityByDesc();
	
	/**
	 * 取得所有活動
	 * (審核通過，非草稿，活動狀態為'報名中')
	 * 照活動起始日排序(新-->舊)
	 */
	@Query(value = "select * from activity where avt_pre='1' and avt_edit='0' and avt_stat='報名中' order by avt_date_s desc", nativeQuery = true)
	List<Activity> showActivityLightBox();

}
