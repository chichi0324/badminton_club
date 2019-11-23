package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badminton.club.entity.Activity;
/**
 * 我要報名
 * 報名資料(個人)
 */
public class ApplyDTO {
	
	private Activity activity;
    //系統將帶入您的個人資料
	private String checkTake;
	//參與人數(含自己)
	private String joinNumber;
	//報名其他資料
	private List<OtherDataDTO> otherDatas=new ArrayList<>();

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getCheckTake() {
		return checkTake;
	}

	public void setCheckTake(String checkTake) {
		this.checkTake = checkTake;
	}

	public String getJoinNumber() {
		return joinNumber;
	}

	public void setJoinNumber(String joinNumber) {
		this.joinNumber = joinNumber;
	}

	public List<OtherDataDTO> getOtherDatas() {
		return otherDatas;
	}

	public void setOtherDatas(List<OtherDataDTO> otherDatas) {
		this.otherDatas = otherDatas;
	}


}
