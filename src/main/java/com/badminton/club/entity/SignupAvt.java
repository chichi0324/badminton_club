package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the signup_avt database table.
 * 報名人員清單
 */
@Entity
@Table(name="signup_avt")
@NamedQuery(name="SignupAvt.findAll", query="SELECT s FROM SignupAvt s")
public class SignupAvt  {
	private static final long serialVersionUID = 1L;

	//報名人員編號
	@Id
	@Column(name="sign_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int signNo;

	//報名人員地址
	@Column(name="sign_addr")
	private String signAddr;

	//報名人員生日
	@Temporal(TemporalType.DATE)
	@Column(name="sign_birth")
	private Date signBirth;

	//參與人數
	@Column(name="sign_count")
	private int signCount;

	//報名人員性別
	@Column(name="sign_gen")
	private String signGen;

	//報名人員身分證
	@Column(name="sign_idn")
	private String signIdn;

	//報名人員電子信箱
	@Column(name="sign_mail")
	private String signMail;

	//備註
	@Column(name="sign_memo")
	private String signMemo;

	//報名人員姓名
	@Column(name="sign_name")
	private String signName;

	//報名人員手機號碼
	@Column(name="sign_phone")
	private String signPhone;

	//報名時間
	@Column(name="sign_time")
	private Timestamp signTime;

	//報名人員帳號
	@Column(name="sign_user")
	private String signUser;

	//bi-directional many-to-one association to OtherDataAn
	@OneToMany(mappedBy="signupAvt",cascade=CascadeType.REMOVE)
	private List<OtherDataAn> otherDataAns;

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

	public SignupAvt() {
	}

	public int getSignNo() {
		return this.signNo;
	}

	public void setSignNo(int signNo) {
		this.signNo = signNo;
	}

	public String getSignAddr() {
		return this.signAddr;
	}

	public void setSignAddr(String signAddr) {
		this.signAddr = signAddr;
	}

	public Date getSignBirth() {
		return this.signBirth;
	}

	public void setSignBirth(Date signBirth) {
		this.signBirth = signBirth;
	}

	public int getSignCount() {
		return this.signCount;
	}

	public void setSignCount(int signCount) {
		this.signCount = signCount;
	}

	public String getSignGen() {
		return this.signGen;
	}

	public void setSignGen(String signGen) {
		this.signGen = signGen;
	}

	public String getSignIdn() {
		return this.signIdn;
	}

	public void setSignIdn(String signIdn) {
		this.signIdn = signIdn;
	}

	public String getSignMail() {
		return this.signMail;
	}

	public void setSignMail(String signMail) {
		this.signMail = signMail;
	}

	public String getSignMemo() {
		return this.signMemo;
	}

	public void setSignMemo(String signMemo) {
		this.signMemo = signMemo;
	}

	public String getSignName() {
		return this.signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSignPhone() {
		return this.signPhone;
	}

	public void setSignPhone(String signPhone) {
		this.signPhone = signPhone;
	}

	public Timestamp getSignTime() {
		return this.signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

	public String getSignUser() {
		return this.signUser;
	}

	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}

	public List<OtherDataAn> getOtherDataAns() {
		return this.otherDataAns;
	}

	public void setOtherDataAns(List<OtherDataAn> otherDataAns) {
		this.otherDataAns = otherDataAns;
	}

	public OtherDataAn addOtherDataAn(OtherDataAn otherDataAn) {
		getOtherDataAns().add(otherDataAn);
		otherDataAn.setSignupAvt(this);

		return otherDataAn;
	}

	public OtherDataAn removeOtherDataAn(OtherDataAn otherDataAn) {
		getOtherDataAns().remove(otherDataAn);
		otherDataAn.setSignupAvt(null);

		return otherDataAn;
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