package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.SignupAvt;
/**
 * 報名人員清單
 */
public class SignAvtDTO {
	
	    //報名人員清單
		private SignupAvt signupAvt;
	
		//==================報名人員清單新增或修改時帶入畫面需要資料===================

		///報名人員生日
		private String birthDate;
		
		//報名其他資料
		private List<OtherDataDTO> otherDatas=new ArrayList<>();
		
		// 依關鍵字
		private String keyWord;
		
		//是否從活動審核入口進去(1:true,0;false)
		private int check;
		
		//活動
		private Activity activity;
		
		// 新增或修改
		private String addOrEdit;
		
		// 會員(新增時可直接帶入報名人員清單)
		private Member member;
		
		// (報名清冊新增)參與人數(含自己)
		private String count;

		public SignupAvt getSignupAvt() {
			return signupAvt;
		}

		public void setSignupAvt(SignupAvt signupAvt) {
			this.signupAvt = signupAvt;
		}

		public String getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(String birthDate) {
			this.birthDate = birthDate;
		}

		public String getKeyWord() {
			return keyWord;
		}

		public void setKeyWord(String keyWord) {
			this.keyWord = keyWord;
		}

		public int getCheck() {
			return check;
		}

		public void setCheck(int check) {
			this.check = check;
		}

		public Activity getActivity() {
			return activity;
		}

		public void setActivity(Activity activity) {
			this.activity = activity;
		}

		public String getAddOrEdit() {
			return addOrEdit;
		}

		public void setAddOrEdit(String addOrEdit) {
			this.addOrEdit = addOrEdit;
		}

		public List<OtherDataDTO> getOtherDatas() {
			return otherDatas;
		}

		public void setOtherDatas(List<OtherDataDTO> otherDatas) {
			this.otherDatas = otherDatas;
		}

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}
		
		
}
