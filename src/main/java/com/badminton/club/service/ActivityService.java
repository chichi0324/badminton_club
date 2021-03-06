package com.badminton.club.service;

import java.util.List;

import com.badminton.club.dto.ApplyAllDTO;
import com.badminton.club.dto.ApplyDTO;
import com.badminton.club.dto.MessageDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.entity.Activity;
/**
 * 活動專區(活動總覽，活動報名) 服務介面
 */
public interface ActivityService {
	/**
	 * 依條件搜尋活動(複合式查詢)
	 * 查詢結果總筆數
	 */
	public int searchCount(QueryActivityDTO queryActivityDTO);

	/**
	 * 依條件搜尋活動(複合式查詢)
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	public List<Activity> search(QueryActivityDTO queryActivityDTO,int indexPage,int countOnePage);

	
	/**
	 * 取得活動 
	 * 活動所有心得回饋照留言時間排序(新-->舊)
	 */
	public Activity findActivityIncludeMessageBytimeDesc(int theId);
	
	/**
	 * 新增報名資料(個人)
	 */
	public void saveSignAvtActivity(String userNo, ApplyDTO applyDTO);

	/**
	 * 新增報名資料(個人加親友)
	 */
	public void saveSignAvtActivity(String userNo, ApplyAllDTO applyAllDTO);


	/**
	 * 取得"signupAvt報名人員清單"總數
	 */
	public int getActivitySum(int theId);

	/**
	 * 存檔"avt_message活動心得回饋"
	 */
	public void saveActivityMessage(String userNo, MessageDTO messageDTO);

	/**
	 * 刪除回饋心得
	 */
	public void deleteActivityMessage(int msg_no);
	


}
