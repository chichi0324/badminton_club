package com.badminton.club.service;

import java.io.IOException;
import java.util.List;

import com.badminton.club.dto.ActivityDTO;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityCheckDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.AvtPreview;
/**
 * 活動管理(活動審核，社員管理) 服務介面
 */
public interface ManagerService {
	/**
	 * 會員審核(複合式查詢) 
	 * 取得會員及搜尋條件
	 */
	public QueryMemberQualifyDTO findMemberAllData(QueryMemberQualifyDTO queryDTO,int indexPage,int countOnePage);

	/**
	 * 更新會員帳號權限
	 */
	public void updateMemberAuth(List<MemberDTO> memberDTOs);

	/**
	 * 匯出(依搜尋條件)會員CSV
	 */
	public QueryMemberQualifyDTO generateMemberCSV(QueryMemberQualifyDTO dto) throws IOException;

	/**
	 * 活動審核(複合式查詢) 
	 * 取得活動及搜尋條件
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	public QueryActivityCheckDTO findActivityAllData(QueryActivityCheckDTO queryDTO, String userNo,int indexPage,int countOnePage);
	
	/**
	 * 匯出(依搜尋條件)活動審核CSV
	 */
	public QueryActivityCheckDTO generateCheckCSV(QueryActivityCheckDTO dto, String userNo) throws IOException;

	/**
	 * 更新活動審核， 管理員在該活動審核點選通過與否。
	 */
	public void updateActivity(List<ActivityDTO> activityDTOs, String userNo);

	/**
	 * 取得該活動活動審核，活動審核已經通過。
	 */
	public List<AvtPreview> findActivityPassed(int avtNo);

	/**
	 * 刪除會員
	 */
	public void deleteMember(String userId);

	/**
	 * 依條件搜尋活動審核(複合式查詢)
	 * 查詢結果總筆數
	 */
	public int searchCheckCount(QueryActivityCheckDTO queryDTO, String userNo);

	/**
	 * 會員審核(複合式查詢) 取得會員及搜尋條件
	 * 查詢結果總筆數
	 */
	public int searchMemberAllCount(QueryMemberQualifyDTO queryDTO);
	

	
}
