package com.badminton.club.service;

import java.util.List;

import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.User;
/**
 * 社員專區(社員資料修改，我的活動) 服務介面
 */
public interface MemberService {
	/**
	 * 所有會員
	 */
	public List<Member> findAll();

	/**
	 * 取得"使用者會員資料"
	 */
	public MemberDTO findMemberData(String userNo);

	/**
	 * 修改"會員"資料
	 */
	public MemberDTO updateMemberData(String userNo, MemberDTO dto);

	/**
	 * 修改"會員帳密"密碼
	 */
	public MemberDTO updatePassword(MemberDTO newDto, MemberDTO oldDto);

	/**
	 * 修改"會員"資料(含大頭貼路徑)
	 */
	public void updateMemberSticker(Member member);

	/**
	 * 我的活動 
	 * 會員有參與的活動"signupAvt報名人員清單"(複合式查詢)
	 */
	public List<SignupAvt> searchJoinActivity(String userNo, QueryActivityDTO queryActivityDTO);

	/**
	 * 會員參與的該項活動之所有"signupAvt報名人員清單"(個人加親友)
	 */
	public List<SignupAvt> joinActivityData(String userNo, int theAvtNo);

}
