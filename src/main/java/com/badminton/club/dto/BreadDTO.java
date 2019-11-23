package com.badminton.club.dto;
/**
 * 麵包屑
 */
public class BreadDTO {
	//名稱
	private String name;
	//該名稱連結
	private String link;

	public BreadDTO(String name, String link) {
		super();
		this.name = name;
		this.link = link;
	}

	public BreadDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
