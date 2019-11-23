package com.badminton.club.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the activity database table.
 * 活動
 */
@Entity
@NamedQuery(name="Activity.findAll", query="SELECT a FROM Activity a")
public class Activity  {
	private static final long serialVersionUID = 1L;

	//活動編號
	@Id
	@Column(name="avt_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int avtNo;

	//活動截止日期
	@Temporal(TemporalType.DATE)
	@Column(name="avt_cut_date")
	private Date avtCutDate;

	//活動日期(迄)
	@Temporal(TemporalType.DATE)
	@Column(name="avt_date_e")
	private Date avtDateE;

	//活動日期(起)
	@Temporal(TemporalType.DATE)
	@Column(name="avt_date_s")
	private Date avtDateS;

	//活動草稿
	@Column(name="avt_edit")
	private byte avtEdit;

	//需親友資料
	@Column(name="avt_frd_data")
	private byte avtFrdData;

	//活動集合時間
	@Column(name="avt_gat_date")
	private Timestamp avtGatDate;

	//活動圖片
	@Column(name="avt_img")
	private String avtImg;

	//活動介紹
	@Lob
	@Column(name="avt_intr")
	private String avtIntr;

	//集合地點
	@Column(name="avt_loc")
	private String avtLoc;

	//活動名額下限
	@Column(name="avt_low")
	private int avtLow;

	//活動備註
	@Lob
	@Column(name="avt_memo")
	private String avtMemo;

	//活動名稱
	@Column(name="avt_name")
	private String avtName;

	//僅會員參加
	@Column(name="avt_only")
	private byte avtOnly;

	//活動審核
	@Column(name="avt_pre")
	private byte avtPre;

	//活動費用
	@Column(name="avt_price")
	private int avtPrice;

	//活動狀態
	@Column(name="avt_stat")
	private String avtStat;

	//活動名額上限
	@Column(name="avt_upp")
	private int avtUpp;

	//bi-directional many-to-one association to ActivityType
	//活動類型編號
	@ManyToOne
	@JoinColumn(name="avt_type_no")
	private ActivityType activityType;

	//bi-directional many-to-one association to Member
	//會員編號
	@ManyToOne
	@JoinColumn(name="mem_user")
	private Member member;

	//bi-directional many-to-one association to Advocate
	@OneToMany(mappedBy="activity",cascade=CascadeType.REMOVE)
	private List<Advocate> advocates;

	//bi-directional many-to-one association to AvtMessage
	@OneToMany(mappedBy="activity",cascade=CascadeType.REMOVE)
	private List<AvtMessage> avtMessages;

	//bi-directional many-to-one association to AvtPreview
	@OneToMany(mappedBy="activity",cascade=CascadeType.REMOVE)
	private List<AvtPreview> avtPreviews;

	//bi-directional many-to-one association to OtherData
	@OneToMany(mappedBy="activity",cascade=CascadeType.REMOVE)
	private List<OtherData> otherData;

	//bi-directional many-to-one association to SignupAvt
	@OneToMany(mappedBy="activity",cascade=CascadeType.REMOVE)
	private List<SignupAvt> signupAvts;

	public Activity() {
	}

	public int getAvtNo() {
		return this.avtNo;
	}

	public void setAvtNo(int avtNo) {
		this.avtNo = avtNo;
	}

	public Date getAvtCutDate() {
		return this.avtCutDate;
	}

	public void setAvtCutDate(Date avtCutDate) {
		this.avtCutDate = avtCutDate;
	}

	public Date getAvtDateE() {
		return this.avtDateE;
	}

	public void setAvtDateE(Date avtDateE) {
		this.avtDateE = avtDateE;
	}

	public Date getAvtDateS() {
		return this.avtDateS;
	}

	public void setAvtDateS(Date avtDateS) {
		this.avtDateS = avtDateS;
	}

	public byte getAvtEdit() {
		return this.avtEdit;
	}

	public void setAvtEdit(byte avtEdit) {
		this.avtEdit = avtEdit;
	}

	public byte getAvtFrdData() {
		return this.avtFrdData;
	}

	public void setAvtFrdData(byte avtFrdData) {
		this.avtFrdData = avtFrdData;
	}

	public Timestamp getAvtGatDate() {
		return this.avtGatDate;
	}

	public void setAvtGatDate(Timestamp avtGatDate) {
		this.avtGatDate = avtGatDate;
	}

	public String getAvtImg() {
		return this.avtImg;
	}

	public void setAvtImg(String avtImg) {
		this.avtImg = avtImg;
	}

	public String getAvtIntr() {
		return this.avtIntr;
	}

	public void setAvtIntr(String avtIntr) {
		this.avtIntr = avtIntr;
	}

	public String getAvtLoc() {
		return this.avtLoc;
	}

	public void setAvtLoc(String avtLoc) {
		this.avtLoc = avtLoc;
	}

	public int getAvtLow() {
		return this.avtLow;
	}

	public void setAvtLow(int avtLow) {
		this.avtLow = avtLow;
	}

	public String getAvtMemo() {
		return this.avtMemo;
	}

	public void setAvtMemo(String avtMemo) {
		this.avtMemo = avtMemo;
	}

	public String getAvtName() {
		return this.avtName;
	}

	public void setAvtName(String avtName) {
		this.avtName = avtName;
	}

	public byte getAvtOnly() {
		return this.avtOnly;
	}

	public void setAvtOnly(byte avtOnly) {
		this.avtOnly = avtOnly;
	}

	public byte getAvtPre() {
		return this.avtPre;
	}

	public void setAvtPre(byte avtPre) {
		this.avtPre = avtPre;
	}

	public int getAvtPrice() {
		return this.avtPrice;
	}

	public void setAvtPrice(int avtPrice) {
		this.avtPrice = avtPrice;
	}

	public String getAvtStat() {
		return this.avtStat;
	}

	public void setAvtStat(String avtStat) {
		this.avtStat = avtStat;
	}

	public int getAvtUpp() {
		return this.avtUpp;
	}

	public void setAvtUpp(int avtUpp) {
		this.avtUpp = avtUpp;
	}

	public ActivityType getActivityType() {
		return this.activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<Advocate> getAdvocates() {
		return this.advocates;
	}

	public void setAdvocates(List<Advocate> advocates) {
		this.advocates = advocates;
	}

	public Advocate addAdvocate(Advocate advocate) {
		getAdvocates().add(advocate);
		advocate.setActivity(this);

		return advocate;
	}

	public Advocate removeAdvocate(Advocate advocate) {
		getAdvocates().remove(advocate);
		advocate.setActivity(null);

		return advocate;
	}

	public List<AvtMessage> getAvtMessages() {
		return this.avtMessages;
	}

	public void setAvtMessages(List<AvtMessage> avtMessages) {
		this.avtMessages = avtMessages;
	}

	public AvtMessage addAvtMessage(AvtMessage avtMessage) {
		getAvtMessages().add(avtMessage);
		avtMessage.setActivity(this);

		return avtMessage;
	}

	public AvtMessage removeAvtMessage(AvtMessage avtMessage) {
		getAvtMessages().remove(avtMessage);
		avtMessage.setActivity(null);

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
		avtPreview.setActivity(this);

		return avtPreview;
	}

	public AvtPreview removeAvtPreview(AvtPreview avtPreview) {
		getAvtPreviews().remove(avtPreview);
		avtPreview.setActivity(null);

		return avtPreview;
	}

	public List<OtherData> getOtherData() {
		return this.otherData;
	}

	public void setOtherData(List<OtherData> otherData) {
		this.otherData = otherData;
	}

	public OtherData addOtherData(OtherData otherData) {
		getOtherData().add(otherData);
		otherData.setActivity(this);

		return otherData;
	}

	public OtherData removeOtherData(OtherData otherData) {
		getOtherData().remove(otherData);
		otherData.setActivity(null);

		return otherData;
	}

	public List<SignupAvt> getSignupAvts() {
		return this.signupAvts;
	}

	public void setSignupAvts(List<SignupAvt> signupAvts) {
		this.signupAvts = signupAvts;
	}

	public SignupAvt addSignupAvt(SignupAvt signupAvt) {
		getSignupAvts().add(signupAvt);
		signupAvt.setActivity(this);

		return signupAvt;
	}

	public SignupAvt removeSignupAvt(SignupAvt signupAvt) {
		getSignupAvts().remove(signupAvt);
		signupAvt.setActivity(null);

		return signupAvt;
	}

}