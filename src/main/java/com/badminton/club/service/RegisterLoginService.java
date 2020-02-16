package com.badminton.club.service;

import com.badminton.club.dto.MemberDTO;
import com.badminton.club.entity.User;
/**
 * 登入及註冊 服務介面
 */
public interface RegisterLoginService {

	/**
	 * 存檔註冊會員資料
	 */
	public void registerAndSave(MemberDTO dto,String imagePath);
	/**
	 * 忘記密碼(寄email通知)
	 */
	public void sendMailPassword(User user);
	/**
	 * 忘記密碼(身分證後六碼)
	 */
	public void getIdCardForPassword(User user);
}
