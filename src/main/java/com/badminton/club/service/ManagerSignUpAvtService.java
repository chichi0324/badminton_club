package com.badminton.club.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.badminton.club.dto.QueryActivitySignDTO;
import com.badminton.club.dto.SignAvtDTO;
import com.badminton.club.entity.SignupAvt;
/**
 * 活動管理(我的活動管理-->報名人員清冊部分) 服務介面
 */
public interface ManagerSignUpAvtService {
	
	/**
	 * 報名人員清單(搜尋和結果) 
	 */
	public QueryActivitySignDTO findActivitySignData(QueryActivitySignDTO queryDTO,int avtNo);
	
	/**
	 * 刪除報名資料
	 */
	public void deleteSignAvt(int signAvtNo);
	
	/**
	 * 匯出(依搜尋條件)報名人員清單CSV
	 */
	public QueryActivitySignDTO generateSignAvtCSV(QueryActivitySignDTO dto) throws IOException;
	
	/**
	 * 新增修改報名資料畫面帶入
	 */
	public void editSignAvt(SignAvtDTO queryDTO);

	
	/**
	 * 存檔報名資料(新增/修改)
	 * @throws ParseException 
	 */
	public void saveEditSignAvt(SignAvtDTO queryDTO);
	
	/**
	 * 查詢報名資料(依姓名和身份證)
	 */
	public List<SignupAvt> findSignupAvt(String user,String name,String idn,int avtNo);
	
	/**
	 * 會員資料帶入報名新增人員
	 */
	public void loadMemberToSignAvt(SignAvtDTO addDTO);
}
