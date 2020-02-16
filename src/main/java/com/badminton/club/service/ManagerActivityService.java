package com.badminton.club.service;

import java.text.ParseException;

import com.badminton.club.dto.ActivityDTO;
import com.badminton.club.dto.QueryActivityHoldDTO;
/**
 * 活動管理(我的活動管理) 服務介面
 */
public interface ManagerActivityService {

	/**
	 * 我的活動管理(複合式查詢)
	 * 取得該管理員主辦的活動及搜尋條件
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	public QueryActivityHoldDTO findAllActivityHold(QueryActivityHoldDTO queryDTO, String userNo,int indexPage,int countOnePage);
	
	/**
	 * 依條件搜尋活動審核(複合式查詢)
	 * 查詢我的活動管理(複合式查詢)總筆數
	 */
	public int searchActivityHoldCount(QueryActivityHoldDTO queryDTO, String userNo);
	/**
	 * 我的活動管理(複合式查詢) 
	 * 刪除活動即相關資料(子table和圖片)
	 */
	public void deleteActivity(int avtNo);
	
	/**
	 * 我的活動管理(修改) 
	 * 取得活動資料
	 */
	public ActivityDTO getActivityDTO(int avtNo);
	
	/**
	 * 我的活動管理(修改) 
	 * 取得活動圖片
	 */
	public void getActivityAdvocateDTO(ActivityDTO activityDTO,String username);
	
	/**
	 * 我的活動管理(新增修改存檔) 
	 * @throws ParseException 
	 */
	public int saveActivity(ActivityDTO PreviousActivityDTO,ActivityDTO activityDTO,String username);
	
	/**
	 * 我的活動管理(新增修改) 
	 * 刪除活動宣傳圖片
	 */
	public void deleteAdvocate(String fileName);

}
