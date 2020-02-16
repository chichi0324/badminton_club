package com.badminton.club.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.ActivityRepository;
import com.badminton.club.dao.AvtMessageRepository;
import com.badminton.club.dao.OtherDataAnRepository;
import com.badminton.club.dao.OtherDataRepository;
import com.badminton.club.dao.SignupAvtRepository;
import com.badminton.club.dto.ApplyAllDTO;
import com.badminton.club.dto.ApplyDTO;
import com.badminton.club.dto.MessageDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.AvtMessage;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.OtherData;
import com.badminton.club.entity.OtherDataAn;
import com.badminton.club.entity.QActivity;
import com.badminton.club.entity.QSignupAvt;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.service.ActivityService;
import com.badminton.club.service.BasicService;
import com.badminton.club.tools.DateTool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
/**
 * 活動專區(活動總覽，活動報名) 服務
 */
@Service("ActivityService")
public class ActivityServiceImpl implements ActivityService {
	
	private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);
	
	private BasicService basicService;	
	private ActivityRepository activityRepository;	
	private SignupAvtRepository signupAvtRepository;
	private OtherDataAnRepository otherDataAnRepository;
	private OtherDataRepository OtherDataRepository;
	private AvtMessageRepository avtMessageRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public ActivityServiceImpl(BasicService basicService, ActivityRepository activityRepository, SignupAvtRepository signupAvtRepository,
			OtherDataAnRepository otherDataAnRepository, com.badminton.club.dao.OtherDataRepository otherDataRepository,
			AvtMessageRepository avtMessageRepository, JPAQueryFactory jpaQueryFactory) {
		this.basicService = basicService;
		this.activityRepository = activityRepository;
		this.signupAvtRepository = signupAvtRepository;
		this.otherDataAnRepository = otherDataAnRepository;
		OtherDataRepository = otherDataRepository;
		this.avtMessageRepository = avtMessageRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	/**
	 * 依條件搜尋活動(複合式查詢)
	 * 查詢結果總筆數
	 */
	@Override
	public int searchCount(QueryActivityDTO queryActivityDTO) {
		try {
			int count=this.search(queryActivityDTO, 0, 0).size();
			return count;
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 依條件搜尋活動(複合式查詢)
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	@Override
	public List<Activity> search(QueryActivityDTO queryActivityDTO,int indexPage,int countOnePage) {
		try {
			String keyWord = queryActivityDTO.getKeyWord();
			String type = queryActivityDTO.getType();
			String status = queryActivityDTO.getStatus();

			QActivity activity = QActivity.activity;
			// 必要條件(一定要審核過，不是在草稿中)
			BooleanExpression whereSQL = activity.avtPre.eq((byte) 1).and(activity.avtEdit.eq((byte) 0));

			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = whereSQL.and(activity.avtName.like("%" + keyWord + "%"));
			}
			// 依活動類型
			if (!StringUtils.isBlank(type)) {
				whereSQL = whereSQL.and(activity.activityType.avtTypeNo.eq(Integer.valueOf(type)));
			}
			// 依活動狀態
			if (!StringUtils.isBlank(status)) {
				if ("1".equals(status)) {
					status = "報名中";
				} else if ("2".equals(status)) {
					status = "已額滿";
				} else if ("3".equals(status)) {
					status = "已截止";
				} else if ("4".equals(status)) {
					status = "已結束";
				}
				whereSQL = whereSQL.and(activity.avtStat.eq(status));
			}
			
			JPAQuery<Activity> jPAQuerys = jpaQueryFactory.selectFrom(activity).from(activity).where(whereSQL)
					.orderBy(activity.avtDateS.desc());
			
			List<Activity> activityQuerys=new ArrayList<>();
			if(indexPage==0 && countOnePage==0){ //無分頁
				activityQuerys=jPAQuerys.fetch();
			}else{  //有分頁
				if(indexPage>=1){
					indexPage=indexPage-1;
				}
				activityQuerys=jPAQuerys.offset(indexPage*8).limit(countOnePage).fetch();
			}
			
			log.info("keyWord:{}, type:{}, status:{}, results:{}", keyWord, type, activityQuerys==null ? null :activityQuerys.size());

			return activityQuerys;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 新增報名資料(個人)
	 */
	@Override
	@Transactional
	public void saveSignAvtActivity(String userNo, ApplyDTO applyDTO) {
		try {
			Member member = this.basicService.findMember(userNo);

			Activity activity = applyDTO.getActivity();

			SignupAvt signupAvt = this.memberToSignUpAvt(member);
			signupAvt.setActivity(activity);
			signupAvt.setSignCount(Integer.parseInt(applyDTO.getJoinNumber()));

			this.signupAvtRepository.save(signupAvt);
			SignupAvt newSignupAvt = this.getNewSignupAvt(signupAvt);
			
			log.info("newSignupAvt SignNo:{}, activity Name:{}", newSignupAvt.getSignNo(), newSignupAvt.getActivity().getAvtName());


			applyDTO.getOtherDatas().forEach(oth -> {
				Optional<OtherData> otherDataResult = this.OtherDataRepository.findById(Integer.parseInt(oth.getNo()));
				OtherData otherData = otherDataResult.isPresent() ? otherDataResult.get() : null;

				OtherDataAn otherDataAn = new OtherDataAn();
				otherDataAn.setOtherData(otherData);
				otherDataAn.setOtheaCon(oth.getData());
				otherDataAn.setSignupAvt(newSignupAvt);

				this.otherDataAnRepository.save(otherDataAn);
			});
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 新增報名資料(個人加親友)
	 */
	@Override
	@Transactional
	public void saveSignAvtActivity(String userNo, ApplyAllDTO applyAllDTO) {
		try {
			this.saveSignAvtActivity(userNo, applyAllDTO.getApplyDTO());

			Member member = this.basicService.findMember(userNo);

			Activity activity = applyAllDTO.getApplyDTO().getActivity();

			applyAllDTO.getFriendDTOs().forEach(friend -> {
				
				friend.getSignupAvt().setSignBirth(DateTool.getDate(friend.getBirthDate()));
				friend.getSignupAvt().setActivity(activity);
				friend.getSignupAvt().setSignCount(0);
				friend.getSignupAvt().setMember(member);

				this.signupAvtRepository.save(friend.getSignupAvt());
				SignupAvt newSignupAvt = this.getNewSignupAvt(friend.getSignupAvt());
				
				log.info("newSignupAvt SignNo:{}, activity Name:{}", newSignupAvt.getSignNo(), newSignupAvt.getActivity().getAvtName());


				friend.getOtherDatas().forEach(oth -> {
					Optional<OtherData> otherDataResult = this.OtherDataRepository
							.findById(Integer.parseInt(oth.getNo()));
					OtherData otherData = otherDataResult.isPresent() ? otherDataResult.get() : null;

					OtherDataAn otherDataAn = new OtherDataAn();
					otherDataAn.setOtherData(otherData);
					otherDataAn.setOtheaCon(oth.getData());
					otherDataAn.setSignupAvt(newSignupAvt);

					this.otherDataAnRepository.save(otherDataAn);
				});

			});
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	};

	/**
	 * "member會員"帶入"signupAvt報名人員清單"
	 */
	public SignupAvt memberToSignUpAvt(Member member) {
		SignupAvt signupAvt = new SignupAvt();
		signupAvt.setSignUser(member.getMemUser());
		signupAvt.setSignName(member.getMemName());
		signupAvt.setSignBirth(member.getMemBirth());
		signupAvt.setSignGen(member.getMemGen());
		signupAvt.setSignIdn(member.getMemIdn());
		signupAvt.setSignPhone(member.getMemPhone());
		signupAvt.setSignMail(member.getMemMail());
		signupAvt.setSignAddr(member.getMemAddr());
		signupAvt.setMember(member);
		return signupAvt;
	}

	/**
	 * "signupAvt報名人員清單" 該筆重新查詢(確認資料為最新那筆)
	 */
	public SignupAvt getNewSignupAvt(SignupAvt theSignupAvt) {
		QSignupAvt signupActivity = QSignupAvt.signupAvt;
		BooleanExpression whereSQL = signupActivity.member.memUser.eq(theSignupAvt.getMember().getMemUser())//
				.and(signupActivity.activity.avtNo.eq(theSignupAvt.getActivity().getAvtNo()))
				.and(signupActivity.signIdn.eq(theSignupAvt.getSignIdn()));

		SignupAvt signupAvtQuery = jpaQueryFactory.selectFrom(signupActivity).from(signupActivity).where(whereSQL)
				.orderBy(signupActivity.signUser.desc()).fetchFirst();

		return signupAvtQuery;
	}


	/**
	 * 取得"signupAvt報名人員清單"總數
	 */
	@Override
	public int getActivitySum(int theId) {
		try {
			return signupAvtRepository.getActivitySum(theId);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 存檔"avt_message活動心得回饋"
	 */
	@Override
	@Transactional
	public void saveActivityMessage(String userNo, MessageDTO messageDTO) {
		try {
			Activity activity = this.basicService.findActivity(messageDTO.getAvtNo());
			Member member = this.basicService.findMember(userNo);

			AvtMessage avtMessage = new AvtMessage();
			avtMessage.setMsgCon(messageDTO.getMessage());
			avtMessage.setMsgStar(messageDTO.getStarCount());
			avtMessage.setMember(member);
			avtMessage.setActivity(activity);

			this.avtMessageRepository.save(avtMessage);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 取得活動 活動所有心得回饋照留言時間排序(新-->舊)
	 */
	@Override
	public Activity findActivityIncludeMessageBytimeDesc(int theId) {
		try {
			Activity activity = this.basicService.findActivity(theId);
			List<AvtMessage> avtMessages = this.avtMessageRepository.findAvtMessageByActivityDesc(theId);
			activity.setAvtMessages(avtMessages);
			return activity;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 刪除回饋心得
	 */
	@Override
	@Transactional
	public void deleteActivityMessage(int msg_no) {
		try {
			Optional<AvtMessage> avtMessageResult = this.avtMessageRepository.findById(msg_no);
			AvtMessage avtMessage = avtMessageResult.isPresent() ? avtMessageResult.get() : null;

			this.avtMessageRepository.delete(avtMessage);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}





}
