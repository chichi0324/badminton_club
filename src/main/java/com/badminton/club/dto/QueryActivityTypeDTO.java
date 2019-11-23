package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.ActivityType;

/**
 * 活動類型
 */
public class QueryActivityTypeDTO {

	// 依關鍵字
	private String keyWord;
	// 活動類型(待修改當筆)
	private ActivityType activityType;
	// 活動類型搜尋結果列表
	private List<ActivityType> activityTypes = new ArrayList<>();

	// 標題(新增或修改)
	private String addOrUpdate;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public List<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(List<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public String getAddOrUpdate() {
		return addOrUpdate;
	}

	public void setAddOrUpdate(String addOrUpdate) {
		this.addOrUpdate = addOrUpdate;
	}
	
	

}
