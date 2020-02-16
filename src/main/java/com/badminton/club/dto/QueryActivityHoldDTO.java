package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 活動管理 我的活動管理
 */
public class QueryActivityHoldDTO {
	// 依關鍵字
	private String keyWord;
	// 依活動類型
	private String type;
	// 依審核狀態
	private String checkStatus;
	// 依活動狀態
	private String signUpStatus;
	//當頁頁數
	private int page;
	// 活動搜尋結果列表
	private List<ActivityDTO> activityDTOs = new ArrayList<>();
	
	

	public QueryActivityHoldDTO(String keyWord, String type, String checkStatus, String signUpStatus, int page) {
		super();
		this.keyWord = keyWord;
		this.type = type;
		this.checkStatus = checkStatus;
		this.signUpStatus = signUpStatus;
		this.page = page;
	}

	public QueryActivityHoldDTO() {
		super();
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getSignUpStatus() {
		return signUpStatus;
	}

	public void setSignUpStatus(String signUpStatus) {
		this.signUpStatus = signUpStatus;
	}

	public List<ActivityDTO> getActivityDTOs() {
		return activityDTOs;
	}

	public void setActivityDTOs(List<ActivityDTO> activityDTOs) {
		this.activityDTOs = activityDTOs;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	

}
