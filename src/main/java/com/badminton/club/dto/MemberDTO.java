package com.badminton.club.dto;

import com.badminton.club.entity.Member;
import com.badminton.club.entity.User;
/**
 * 會員資料(含帳密，權限)
 * (日期，通知等轉換用)
 */
public class MemberDTO {
	//會員
	private Member member;
	//會員帳密
	private User user;
	
	//確認密碼(註冊用)
	private String ckeckPwd;
	//會員生日
	private String birthDate;
	//會員信箱通知
	//(若在 社員管理用在User的enabled會員資格)
	//(若在 管理員管理用在Roles的Manager會員資格)
	private String inform;
	
	//活動管理-->社員管理  用
	//role為ROLE_MANAGER或ROLE_SURPERMANAGER值為notEdit(無法編輯)
	//活動最高管理-->管理員管理  用
	//role為ROLE_SURPERMANAGER值為notEdit(無法編輯)
	private String authorityDisable;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getInform() {
		return inform;
	}

	public void setInform(String inform) {
		this.inform = inform;
	}

	public String getCkeckPwd() {
		return ckeckPwd;
	}

	public void setCkeckPwd(String ckeckPwd) {
		this.ckeckPwd = ckeckPwd;
	}

	public String getAuthorityDisable() {
		return authorityDisable;
	}

	public void setAuthorityDisable(String authorityDisable) {
		this.authorityDisable = authorityDisable;
	}
	
	

}
