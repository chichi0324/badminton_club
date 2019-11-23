package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the advocate database table.
 * 活動宣傳圖片
 */
@Entity
@NamedQuery(name="Advocate.findAll", query="SELECT a FROM Advocate a")
public class Advocate  {
	private static final long serialVersionUID = 1L;

	//宣傳編號
	@Id
	@Column(name="adv_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int advNo;

	//宣傳圖片
	@Column(name="adv_img")
	private String advImg;

	//bi-directional many-to-one association to Activity
	//活動編號
	@ManyToOne
	@JoinColumn(name="avt_no")
	private Activity activity;

	public Advocate() {
	}

	public int getAdvNo() {
		return this.advNo;
	}

	public void setAdvNo(int advNo) {
		this.advNo = advNo;
	}

	public String getAdvImg() {
		return this.advImg;
	}

	public void setAdvImg(String advImg) {
		this.advImg = advImg;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}