package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the member database table.
 * 會員
 */
@Entity
@Table(name="member")
@NamedQuery(name="Member.findAll", query="SELECT m FROM Member m")
public class Member  {
	private static final long serialVersionUID = 1L;

	//會員帳號
	@Id
	@Column(name="mem_user")
	private String memUser;

	//會員地址
	@Column(name="mem_addr")
	private String memAddr;

	//會員生日
	@Temporal(TemporalType.DATE)
	@Column(name="mem_birth")
	private Date memBirth;

	//會員性別
	@Column(name="mem_gen")
	private String memGen;

	//會員身分證
	@Column(name="mem_idn")
	private String memIdn;

	//會員圖片
	@Column(name="mem_img")
	private String memImg;

	//信箱通知
	@Column(name="mem_info")
	private byte memInfo;

	//會員電子信箱
	@Column(name="mem_mail")
	private String memMail;

	//會員姓名
	@Column(name="mem_name")
	private String memName;

	//會員手機號碼
	@Column(name="mem_phone")
	private String memPhone;

	//註冊時間
	@Column(name="mem_time")
	private Timestamp memTime;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="member")
	private List<Activity> activities;

	//bi-directional many-to-one association to AvtMessage
	@OneToMany(mappedBy="member")
	private List<AvtMessage> avtMessages;

	//bi-directional many-to-one association to AvtPreview
	@OneToMany(mappedBy="member")
	private List<AvtPreview> avtPreviews;

	//bi-directional many-to-one association to SignupAvt
	@OneToMany(mappedBy="member")
	private List<SignupAvt> signupAvts;

	public Member() {
	}

	public String getMemUser() {
		return this.memUser;
	}

	public void setMemUser(String memUser) {
		this.memUser = memUser;
	}

	public String getMemAddr() {
		return this.memAddr;
	}

	public void setMemAddr(String memAddr) {
		this.memAddr = memAddr;
	}

	public Date getMemBirth() {
		return this.memBirth;
	}

	public void setMemBirth(Date memBirth) {
		this.memBirth = memBirth;
	}

	public String getMemGen() {
		return this.memGen;
	}

	public void setMemGen(String memGen) {
		this.memGen = memGen;
	}

	public String getMemIdn() {
		return this.memIdn;
	}

	public void setMemIdn(String memIdn) {
		this.memIdn = memIdn;
	}

	public String getMemImg() {
		return this.memImg;
	}

	public void setMemImg(String memImg) {
		this.memImg = memImg;
	}

	public byte getMemInfo() {
		return this.memInfo;
	}

	public void setMemInfo(byte memInfo) {
		this.memInfo = memInfo;
	}

	public String getMemMail() {
		return this.memMail;
	}

	public void setMemMail(String memMail) {
		this.memMail = memMail;
	}

	public String getMemName() {
		return this.memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemPhone() {
		return this.memPhone;
	}

	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}

	public Timestamp getMemTime() {
		return this.memTime;
	}

	public void setMemTime(Timestamp memTime) {
		this.memTime = memTime;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setMember(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setMember(null);

		return activity;
	}

	public List<AvtMessage> getAvtMessages() {
		return this.avtMessages;
	}

	public void setAvtMessages(List<AvtMessage> avtMessages) {
		this.avtMessages = avtMessages;
	}

	public AvtMessage addAvtMessage(AvtMessage avtMessage) {
		getAvtMessages().add(avtMessage);
		avtMessage.setMember(this);

		return avtMessage;
	}

	public AvtMessage removeAvtMessage(AvtMessage avtMessage) {
		getAvtMessages().remove(avtMessage);
		avtMessage.setMember(null);

		return avtMessage;
	}

	public List<AvtPreview> getAvtPreviews() {
		return this.avtPreviews;
	}

	public void setAvtPreviews(List<AvtPreview> avtPreviews) {
		this.avtPreviews = avtPreviews;
	}

	public AvtPreview addAvtPreview(AvtPreview avtPreview) {
		getAvtPreviews().add(avtPreview);
		avtPreview.setMember(this);

		return avtPreview;
	}

	public AvtPreview removeAvtPreview(AvtPreview avtPreview) {
		getAvtPreviews().remove(avtPreview);
		avtPreview.setMember(null);

		return avtPreview;
	}

	public List<SignupAvt> getSignupAvts() {
		return this.signupAvts;
	}

	public void setSignupAvts(List<SignupAvt> signupAvts) {
		this.signupAvts = signupAvts;
	}

	public SignupAvt addSignupAvt(SignupAvt signupAvt) {
		getSignupAvts().add(signupAvt);
		signupAvt.setMember(this);

		return signupAvt;
	}

	public SignupAvt removeSignupAvt(SignupAvt signupAvt) {
		getSignupAvts().remove(signupAvt);
		signupAvt.setMember(null);

		return signupAvt;
	}

}