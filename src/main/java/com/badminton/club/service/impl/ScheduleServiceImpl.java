package com.badminton.club.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badminton.club.dao.ActivityRepository;
import com.badminton.club.dao.SystemRepository;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.QActivity;
import com.badminton.club.entity.QMember;
import com.badminton.club.entity.QSignupAvt;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.System;
import com.badminton.club.service.ScheduleService;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.MailTool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * 排成器執行 服務
 */
@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	private static final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

	private ActivityRepository activityRepository;
	private SystemRepository systemRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public ScheduleServiceImpl(ActivityRepository activityRepository, SystemRepository systemRepository,
			JPAQueryFactory jpaQueryFactory) {
		super();
		this.activityRepository = activityRepository;
		this.systemRepository = systemRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	/**
	 * 更新活動狀態 (已截止，已額滿)
	 */
	@Override
	public void updateActivityStatus() {

		QActivity theActivity = QActivity.activity;
		BooleanExpression whereSQL = theActivity.avtPre.eq((byte) 1).and(theActivity.avtEdit.eq((byte) 0));
		List<Activity> activityQuerys = jpaQueryFactory.selectFrom(theActivity).from(theActivity).where(whereSQL)
				.fetch();

		System systemMail = this.systemRepository.findSystem("系統信箱");

		activityQuerys.forEach(activity -> {
			String nowDate = DateTool.dateToString(new Date());
			String activityCutDate = DateTool.dateToString(activity.getAvtCutDate());
			String activityDate = DateTool.dateToString(activity.getAvtDateS());

			log.info("活動名稱:{},系統日期:{}, 活動截止:{}, 活動時間:{}", activity.getAvtName(),nowDate,activityCutDate,activityDate);


			if (nowDate.equals(activityCutDate)) {
				activity.setAvtStat("已截止");
				this.activityRepository.save(activity);
			}
			if (nowDate.equals(activityDate)) {
				activity.setAvtStat("已結束");
				this.activityRepository.save(activity);

				QSignupAvt theSignupAvt = QSignupAvt.signupAvt;
				List<SignupAvt> signupAvtQuerys = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
						.where(theSignupAvt.activity.avtNo.eq(activity.getAvtNo()).and(theSignupAvt.signUser.isNotNull())).fetch();

				if(CollectionUtils.isNotEmpty(signupAvtQuerys)){
					signupAvtQuerys.forEach(s -> {
							QMember theMember = QMember.member;
							Member member = jpaQueryFactory.selectFrom(theMember).from(theMember)
									.where(theMember.memUser.eq(s.getSignUser()).and(theMember.memInfo.eq((byte) 1)))
									.fetchFirst();
							if (member != null) {
								MailTool.transferActivityStartMessage(member, activity, systemMail.getSysData(),
										systemMail.getSysCon());
							}

					});
				}
				


			}

		});

	}

}
