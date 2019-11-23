package com.badminton.club.dto;
/**
 * 活動心得回饋
 */
public class MessageDTO {
	//心得回饋
	private String message;
	//星星評分
	private int starCount;
	//活動編號
	private int avtNo;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}

	public int getAvtNo() {
		return avtNo;
	}

	public void setAvtNo(int avtNo) {
		this.avtNo = avtNo;
	}
	
	

}
