package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.SignupAvt;
/**
 * 報名人員清單(親友資料)
 * (除了會員資料，親友資料)
 */
public class FriendDTO {
	//報名人員清單
	private SignupAvt signupAvt;
	//報名人員生日
	String birthDate;
	//報名其他資料列表
	List<OtherDataDTO> otherDatas = new ArrayList<>();

	public SignupAvt getSignupAvt() {
		return signupAvt;
	}

	public void setSignupAvt(SignupAvt signupAvt) {
		this.signupAvt = signupAvt;
	}

	public List<OtherDataDTO> getOtherDatas() {
		return otherDatas;
	}

	public void setOtherDatas(List<OtherDataDTO> otherDatas) {
		this.otherDatas = otherDatas;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	
}
