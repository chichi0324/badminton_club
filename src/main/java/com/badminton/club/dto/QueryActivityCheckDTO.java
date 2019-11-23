package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;
/**
 * 活動管理
 * 活動審核
 */
public class QueryActivityCheckDTO {
	//依關鍵字
	private String keyWord;
	//依審核狀態
	private String statusPass;
	//依活動舉辦人
	private String holder;
	//活動搜尋結果列表
	private List<ActivityDTO> activityDTOs=new ArrayList<>();
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getStatusPass() {
		return statusPass;
	}
	public void setStatusPass(String statusPass) {
		this.statusPass = statusPass;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public List<ActivityDTO> getActivityDTOs() {
		return activityDTOs;
	}
	public void setActivityDTOs(List<ActivityDTO> activityDTOs) {
		this.activityDTOs = activityDTOs;
	}
	
	
}
