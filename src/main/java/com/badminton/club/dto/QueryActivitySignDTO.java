package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.badminton.club.entity.Activity;
/**
 * 報名人員清單(搜尋和結果)
 */
public class QueryActivitySignDTO {

	// 依關鍵字
	private String keyWord;
	
	//活動
	private Activity activity;
	
	// 人數
	private int count;

	// 報名人員清單列表
	private List<SignAvtDTO> signAvtDTOs = new ArrayList<>();
	
	//是否從活動審核入口進去(1:true,0;false)
	private int check;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<SignAvtDTO> getSignAvtDTOs() {
		return signAvtDTOs;
	}

	public void setSignAvtDTOs(List<SignAvtDTO> signAvtDTOs) {
		this.signAvtDTOs = signAvtDTOs;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	
	
	
}
