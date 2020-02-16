package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.Activity;
/**
 * 我的管理管理(報名人員清冊)
 * 活動管理(社員管理)
 * 活動最高管理(管理員管理)
 */
public class QueryMemberQualifyDTO {
	
	//依關鍵字
	private String keyWord;
	//依審核社員狀態(社員管理)
	//依身份(管理員管理)
	//固定為yes(報名人員清冊)
	private String status;
	//會員搜尋結果列表
	private List<MemberDTO> memberDTOs=new ArrayList<>();
	//當頁頁數(社員管理)
	private int page;
	
	//帶入是否為活動審核(報名人員清冊)
	private int check;
	//帶入該活動資料(報名人員清冊)
	private Activity activity;
	
	
	
	public QueryMemberQualifyDTO() {
		super();
	}
	public QueryMemberQualifyDTO(String keyWord, String status, int page) {
		super();
		this.keyWord = keyWord;
		this.status = status;
		this.page = page;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<MemberDTO> getMemberDTOs() {
		return memberDTOs;
	}
	public void setMemberDTOs(List<MemberDTO> memberDTOs) {
		this.memberDTOs = memberDTOs;
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
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	
	
}
