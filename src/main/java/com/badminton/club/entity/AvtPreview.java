package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the avt_preview database table.
 * 活動審核
 */
@Entity
@Table(name="avt_preview")
@NamedQuery(name="AvtPreview.findAll", query="SELECT a FROM AvtPreview a")
public class AvtPreview  {
	private static final long serialVersionUID = 1L;

	//活動審核編號
	@Id
	@Column(name="avt_pre_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int avtPreNo;

	//活動審核確認
	@Column(name="avt_pre_check")
	private byte avtPreCheck;

	//bi-directional many-to-one association to Activity
	//活動編號
	@ManyToOne
	@JoinColumn(name="avt_no")
	private Activity activity;

	//bi-directional many-to-one association to Member
	//會員編號
	@ManyToOne
	@JoinColumn(name="mem_user")
	private Member member;

	public AvtPreview() {
	}

	public int getAvtPreNo() {
		return this.avtPreNo;
	}

	public void setAvtPreNo(int avtPreNo) {
		this.avtPreNo = avtPreNo;
	}

	public byte getAvtPreCheck() {
		return this.avtPreCheck;
	}

	public void setAvtPreCheck(byte avtPreCheck) {
		this.avtPreCheck = avtPreCheck;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}