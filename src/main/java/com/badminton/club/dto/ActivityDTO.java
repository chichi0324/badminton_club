package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.Activity;
/**
 * 我的活動管理(新增/修改)
 * 活動資料(搜尋結果)
 */
public class ActivityDTO {
	//活動
	private Activity activity;
	//活動日期(起)
	private String activityDateS;
	//活動日期(迄)
	private String activityDateE;
	//集合時間
	private String avtGatDate;
	//活動截止日
	private String avtCutDate;
	
	//新增或修改(我的活動管理，新增/修改)
	private String addOrEdit;
	
	//其他資料
	private List<String> otherDatas=new ArrayList<>();
	
	//可以攜帶親友
	private String enableFriend;
	
	//需要親友資料
	private String friendData;
	
	//是否為草稿
	private String avtEdit;
	
	private AvtImgDTO avtImgDTO=new AvtImgDTO();
	
	//=============================
	
	//管理員在該活動已審核通過。
	private String previewCheck;
	//管理員為該活動主辦人。(yes)
	private String ownActivity;
	
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public String getActivityDateS() {
		return activityDateS;
	}
	public void setActivityDateS(String activityDateS) {
		this.activityDateS = activityDateS;
	}
	public String getActivityDateE() {
		return activityDateE;
	}
	public void setActivityDateE(String activityDateE) {
		this.activityDateE = activityDateE;
	}
	public String getPreviewCheck() {
		return previewCheck;
	}
	public void setPreviewCheck(String previewCheck) {
		this.previewCheck = previewCheck;
	}
	public String getOwnActivity() {
		return ownActivity;
	}
	public void setOwnActivity(String ownActivity) {
		this.ownActivity = ownActivity;
	}

	public String getAvtGatDate() {
		return avtGatDate;
	}
	public void setAvtGatDate(String avtGatDate) {
		this.avtGatDate = avtGatDate;
	}
	public String getAvtCutDate() {
		return avtCutDate;
	}
	public void setAvtCutDate(String avtCutDate) {
		this.avtCutDate = avtCutDate;
	}
	public String getAddOrEdit() {
		return addOrEdit;
	}
	public void setAddOrEdit(String addOrEdit) {
		this.addOrEdit = addOrEdit;
	}
	public List<String> getOtherDatas() {
		return otherDatas;
	}
	public void setOtherDatas(List<String> otherDatas) {
		this.otherDatas = otherDatas;
	}
	public String getEnableFriend() {
		return enableFriend;
	}
	public void setEnableFriend(String enableFriend) {
		this.enableFriend = enableFriend;
	}
	public String getFriendData() {
		return friendData;
	}
	public void setFriendData(String friendData) {
		this.friendData = friendData;
	}
	public String getAvtEdit() {
		return avtEdit;
	}
	public void setAvtEdit(String avtEdit) {
		this.avtEdit = avtEdit;
	}
	public AvtImgDTO getAvtImgDTO() {
		return avtImgDTO;
	}
	public void setAvtImgDTO(AvtImgDTO avtImgDTO) {
		this.avtImgDTO = avtImgDTO;
	}
	

	
}
