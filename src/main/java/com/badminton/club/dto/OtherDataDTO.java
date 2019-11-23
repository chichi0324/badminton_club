package com.badminton.club.dto;
/**
 * 報名其他資料
 * (除了會員基本資料，該活動也需要的其他資料)
 */
public class OtherDataDTO {
	//其他編號
	private String no;
	//其他項目名稱
	private String name;
	//其他項目內容
	private String data;
	

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	

}
