package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the other_data database table.
 * 報名其他資料
 */
@Entity
@Table(name="other_data")
@NamedQuery(name="OtherData.findAll", query="SELECT o FROM OtherData o")
public class OtherData  {
	private static final long serialVersionUID = 1L;

	//其他編號
	@Id
	@Column(name="oth_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int othNo;

	//其他項目
	@Column(name="oth_name")
	private String othName;

	//bi-directional many-to-one association to Activity
	//活動編號
	@ManyToOne
	@JoinColumn(name="avt_no")
	private Activity activity;

	//bi-directional many-to-one association to OtherDataAn
	@OneToMany(mappedBy="otherData",cascade=CascadeType.REMOVE)
	private List<OtherDataAn> otherDataAns;

	public OtherData() {
	}

	public int getOthNo() {
		return this.othNo;
	}

	public void setOthNo(int othNo) {
		this.othNo = othNo;
	}

	public String getOthName() {
		return this.othName;
	}

	public void setOthName(String othName) {
		this.othName = othName;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<OtherDataAn> getOtherDataAns() {
		return this.otherDataAns;
	}

	public void setOtherDataAns(List<OtherDataAn> otherDataAns) {
		this.otherDataAns = otherDataAns;
	}

	public OtherDataAn addOtherDataAn(OtherDataAn otherDataAn) {
		getOtherDataAns().add(otherDataAn);
		otherDataAn.setOtherData(this);

		return otherDataAn;
	}

	public OtherDataAn removeOtherDataAn(OtherDataAn otherDataAn) {
		getOtherDataAns().remove(otherDataAn);
		otherDataAn.setOtherData(null);

		return otherDataAn;
	}

}