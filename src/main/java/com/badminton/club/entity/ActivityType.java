package com.badminton.club.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the activity_type database table.
 * 活動類型
 */
@Entity
@Table(name="activity_type")
@NamedQuery(name="ActivityType.findAll", query="SELECT a FROM ActivityType a")
public class ActivityType  {
	private static final long serialVersionUID = 1L;

	//活動類型編號
	@Id
	@Column(name="avt_type_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int avtTypeNo;

	//系活動類型名稱
	@Column(name="avt_type_name")
	private String avtTypeName;

	//活動須經審核
	@Column(name="avt_type_pre")
	private byte avtTypePre;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="activityType",cascade=CascadeType.REMOVE)
	private List<Activity> activities;

	public ActivityType() {
	}

	public int getAvtTypeNo() {
		return this.avtTypeNo;
	}

	public void setAvtTypeNo(int avtTypeNo) {
		this.avtTypeNo = avtTypeNo;
	}

	public String getAvtTypeName() {
		return this.avtTypeName;
	}

	public void setAvtTypeName(String avtTypeName) {
		this.avtTypeName = avtTypeName;
	}

	public byte getAvtTypePre() {
		return this.avtTypePre;
	}

	public void setAvtTypePre(byte avtTypePre) {
		this.avtTypePre = avtTypePre;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setActivityType(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setActivityType(null);

		return activity;
	}

}