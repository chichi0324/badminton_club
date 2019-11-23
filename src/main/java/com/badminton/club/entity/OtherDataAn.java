package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the other_data_ans database table.
 * 報名其他資料填寫
 */
@Entity
@Table(name="other_data_ans")
@NamedQuery(name="OtherDataAn.findAll", query="SELECT o FROM OtherDataAn o")
public class OtherDataAn  {
	private static final long serialVersionUID = 1L;

	//其他填寫編號
	@Id
	@Column(name="othea_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int otheaNo;

	//其他填寫資料
	@Lob
	@Column(name="othea_con")
	private String otheaCon;

	//bi-directional many-to-one association to OtherData
	//其他編號
	@ManyToOne
	@JoinColumn(name="oth_no")
	private OtherData otherData;

	//bi-directional many-to-one association to SignupAvt
	//報名人員編號
	@ManyToOne
	@JoinColumn(name="sign_no")
	private SignupAvt signupAvt;

	public OtherDataAn() {
	}

	public int getOtheaNo() {
		return this.otheaNo;
	}

	public void setOtheaNo(int otheaNo) {
		this.otheaNo = otheaNo;
	}

	public String getOtheaCon() {
		return this.otheaCon;
	}

	public void setOtheaCon(String otheaCon) {
		this.otheaCon = otheaCon;
	}

	public OtherData getOtherData() {
		return this.otherData;
	}

	public void setOtherData(OtherData otherData) {
		this.otherData = otherData;
	}

	public SignupAvt getSignupAvt() {
		return this.signupAvt;
	}

	public void setSignupAvt(SignupAvt signupAvt) {
		this.signupAvt = signupAvt;
	}

}