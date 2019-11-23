package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the avt_message database table.
 * 活動心得回饋
 */
@Entity
@Table(name="avt_message")
@NamedQuery(name="AvtMessage.findAll", query="SELECT a FROM AvtMessage a")
public class AvtMessage  {
	private static final long serialVersionUID = 1L;

	//回饋編號
	@Id
	@Column(name="msg_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int msgNo;

	//回饋內容
	@Lob
	@Column(name="msg_con")
	private String msgCon;

	//星號評分
	@Column(name="msg_star")
	private int msgStar;

	//回饋時間
	@Column(name="msg_time")
	private Timestamp msgTime;

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

	public AvtMessage() {
	}

	public int getMsgNo() {
		return this.msgNo;
	}

	public void setMsgNo(int msgNo) {
		this.msgNo = msgNo;
	}

	public String getMsgCon() {
		return this.msgCon;
	}

	public void setMsgCon(String msgCon) {
		this.msgCon = msgCon;
	}

	public int getMsgStar() {
		return this.msgStar;
	}

	public void setMsgStar(int msgStar) {
		this.msgStar = msgStar;
	}

	public Timestamp getMsgTime() {
		return this.msgTime;
	}

	public void setMsgTime(Timestamp msgTime) {
		this.msgTime = msgTime;
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