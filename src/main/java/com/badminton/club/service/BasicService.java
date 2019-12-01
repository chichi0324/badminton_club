package com.badminton.club.service;

import java.util.List;

import com.badminton.club.entity.System;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.ActivityType;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.Role;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.User;
/**
 * 共用(活動，會員，管理員，超級管理員) 服務介面
 */
public interface BasicService {
	
	/**
	 * 取得會員帳密
	 */
	public User findUser(String theUserNo);
	
	/**
	 * 取得"會員"資料
	 */
	public Member findMember(String userNo);

	/**
	 * 取得"活動"資料
	 */
	public Activity findActivity(int avtNo);
	
	
	/**
	 * 社團介紹資料
	 */
	public System getIntroduceSystem(String sysName);
	
	/**
	 * 取得所有活動類型
	 */
	public List<ActivityType> getAllActivityType();
	
	/**
	 * 幻燈片顯示所有活動 
	 * (審核通過，非草稿，活動狀態為'報名中') 
	 * 照活動起始日排序(新-->舊)
	 */
	public List<Activity> showLightBox();
	
	/**
	 * 查詢報名資料
	 */
	public SignupAvt findSignupAvt(int signNo);
	
	/**
	 * 取得該會員擁有的所有腳色
	 */
	public List<String> getAllRoles(User user);
	
	/**
	 * 取得角色
	 */
	public Role findRole(String authority);
	
	/**
	 * 是否有權限編輯活動報名人員清單(只有活動舉辦人和超級管理員可以)
	 */
	public boolean isEditSignAvtEnable(String userNo,int avtNo);
	

	/**
	 * 驗證活動限制名額是否額滿，若額滿更新活動狀態(改為"已額滿")
	 */
	public void updateActivityStatus(int theId);
}
