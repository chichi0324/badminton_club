package com.badminton.club.service;

import java.util.List;

import com.badminton.club.dto.FooterDTO;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityTypeDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.ActivityType;
import com.badminton.club.entity.System;
/**
 * 活動最高管理(社團介紹編輯，活動類型及審核，管理員管理) 服務介面
 */
public interface SuperManagerService {
	
	/**
	 * 管理員管理(複合式查詢) 
	 * 取得會員及搜尋條件
	 */
	public QueryMemberQualifyDTO findMemberAllData(QueryMemberQualifyDTO dto);
	
	/**
	 * 更新會員帳號權限
	 */
	public void updateMemberAuth(List<MemberDTO> memberDTOs);
	
	/**
	 * 活動類型及審核(關鍵字查詢) 
	 */
	public QueryActivityTypeDTO findActivityType(QueryActivityTypeDTO dto);
	
	/**
	 * 取得活動類型
	 */
	public ActivityType getActivityType(int avtTypeNo);
	
	/**
	 * 新增或修改活動類型
	 */
	public void saveOrUpdateActivityType(ActivityType activityType);
	
	/**
	 * 刪除活動類型
	 */
	public void deleteActivityType(int avtTypeNo);
	
	/**
	 * 取得系統設定
	 */
	public System getSystem(String sysName);
	
	/**
	 * 系統設定更新存檔
	 */
	public void saveSystem(System theSystem);
	
}
