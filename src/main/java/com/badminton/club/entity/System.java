package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the systems database table.
 * 系統設定
 */
@Entity
@Table(name="systems")
@NamedQuery(name="System.findAll", query="SELECT s FROM System s")
public class System  {
	private static final long serialVersionUID = 1L;

	//系統設定編號
	@Id
	@Column(name="sys_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sysNo;

	//系統設定內容
	@Lob
	@Column(name="sys_con")
	private String sysCon;

	//系統設定資料
	@Column(name="sys_data")
	private String sysData;

	//系統設定名稱
	@Column(name="sys_name")
	private String sysName;

	public System() {
	}

	public int getSysNo() {
		return this.sysNo;
	}

	public void setSysNo(int sysNo) {
		this.sysNo = sysNo;
	}

	public String getSysCon() {
		return this.sysCon;
	}

	public void setSysCon(String sysCon) {
		this.sysCon = sysCon;
	}

	public String getSysData() {
		return this.sysData;
	}

	public void setSysData(String sysData) {
		this.sysData = sysData;
	}

	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

}