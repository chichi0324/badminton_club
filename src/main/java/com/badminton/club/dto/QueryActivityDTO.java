package com.badminton.club.dto;
/**
 * 活動總覽
 * 活動查詢
 */
public class QueryActivityDTO {
	//依關鍵字
	private String keyWord;
	//依活動類型
	private String type;
	//依活動狀態
	private String status;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
