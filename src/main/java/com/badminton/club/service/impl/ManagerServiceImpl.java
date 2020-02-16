package com.badminton.club.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.ActivityRepository;
import com.badminton.club.dao.AvtPreviewRepository;
import com.badminton.club.dao.MemberRepository;
import com.badminton.club.dao.SystemRepository;
import com.badminton.club.dao.UserRepository;
import com.badminton.club.dto.ActivityDTO;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityCheckDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Authority;
import com.badminton.club.entity.AvtPreview;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.QActivity;
import com.badminton.club.entity.QAuthority;
import com.badminton.club.entity.QAvtPreview;
import com.badminton.club.entity.QMember;
import com.badminton.club.entity.QUser;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.System;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerService;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.FileTool;
import com.badminton.club.tools.MailTool;
import com.opencsv.CSVWriter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
/**
 * 活動管理(活動審核，社員管理) 服務
 */
@Service("ManagerService")
public class ManagerServiceImpl implements ManagerService {

	private static final Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);

	private BasicService basicService;
	
	private MemberRepository memberRepository;
	private UserRepository userRepository;

	private ActivityRepository activityRepository;
	private AvtPreviewRepository avtPreviewRepository;
	private SystemRepository systemRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public ManagerServiceImpl(BasicService basicService, MemberRepository memberRepository,
			UserRepository userRepository, ActivityRepository activityRepository,
			AvtPreviewRepository avtPreviewRepository, SystemRepository systemRepository,
			JPAQueryFactory jpaQueryFactory) {
		super();
		this.basicService = basicService;
		this.memberRepository = memberRepository;
		this.userRepository = userRepository;
		this.activityRepository = activityRepository;
		this.avtPreviewRepository = avtPreviewRepository;
		this.systemRepository = systemRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}
	
	/**
	 * 會員審核(複合式查詢) 取得會員及搜尋條件
	 * 查詢結果總筆數
	 */
	@Override
	public int searchMemberAllCount(QueryMemberQualifyDTO queryDTO) {
		try {
			QueryMemberQualifyDTO queryCountDTO = new QueryMemberQualifyDTO();
			BeanUtils.copyProperties(queryCountDTO, queryDTO);
			int count=this.findMemberAllData(queryCountDTO,0, 0).getMemberDTOs().size();
			return count;
		} catch (Exception e) {
				e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 會員審核(複合式查詢) 取得會員及搜尋條件
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	@Override
	public QueryMemberQualifyDTO findMemberAllData(QueryMemberQualifyDTO queryDTO,int indexPage,int countOnePage) {
		try {
			String keyWord = queryDTO.getKeyWord();
			String status = queryDTO.getStatus();

			JPAQuery<Member> jPAQuerys=new JPAQuery<>();
			List<Member> members = new ArrayList<>();
			QMember theMember = QMember.member;

			BooleanExpression whereSQL = null;
			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = theMember.memUser.like("%" + keyWord + "%").or(theMember.memName.like("%" + keyWord + "%"));
			}
			// 依會員狀態
			if (!StringUtils.isBlank(status)) {
				QUser theUser = QUser.user;
				List<String> userIds = jpaQueryFactory.select(theUser.username).from(theUser)
						.where(theUser.enabled.eq("yes".equals(status) ? (byte) 1 : (byte) 0)).fetch();
				whereSQL = (whereSQL == null) ? theMember.memUser.in(userIds)
						: whereSQL.and(theMember.memUser.in(userIds));
			}
			if (whereSQL == null) {
				jPAQuerys = jpaQueryFactory.selectFrom(theMember).from(theMember).orderBy(theMember.memTime.desc());
			} else {
				jPAQuerys = jpaQueryFactory.selectFrom(theMember).from(theMember).where(whereSQL).orderBy(theMember.memTime.desc());
			}
			
			if(indexPage==0 && countOnePage==0){ //無分頁
				members = jPAQuerys.fetch();
			}else{  //有分頁
				if(indexPage>=1){
					indexPage=indexPage-1;
				}
				members=jPAQuerys.offset(indexPage*7).limit(countOnePage).fetch();
			}
			log.info("keyWord:{}, status:{}", keyWord, status,members==null ? null :members.size());


			List<MemberDTO> memberDTOs = new ArrayList<>();
			members.forEach(m -> {
				User user = this.basicService.findUser(m.getMemUser());
				MemberDTO dto = new MemberDTO();
				dto.setMember(m);
				dto.setUser(user);
				dto.setInform(user.getEnabled() == (byte) 1 ? "on" : null);
				dto.setBirthDate(DateTool.dateToString(m.getMemBirth()));
				if (this.basicService.getAllRoles(user).contains("ROLE_MANAGER")
						|| this.basicService.getAllRoles(user).contains("ROLE_SURPERMANAGER")) {
					dto.setAuthorityDisable("notEdit");
				}
				memberDTOs.add(dto);
			});
			queryDTO.setMemberDTOs(memberDTOs);

			return queryDTO;
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 更新會員帳號權限
	 */
	@Override
	@Transactional
	public void updateMemberAuth(List<MemberDTO> memberDTOs) {
		try {
			memberDTOs.forEach(member -> {
				User user = this.basicService.findUser(member.getMember().getMemUser());
				// 會員(沒有管理員及超級管理員腳色)
				if (!this.basicService.getAllRoles(user).contains("ROLE_MANAGER")
						&& !this.basicService.getAllRoles(user).contains("ROLE_SURPERMANAGER")) {

					if ("on".equals(member.getInform())) {
						user.setEnabled((byte) 1);
					} else {
						user.setEnabled((byte) 0);
					}
					this.userRepository.save(user);
				}
			});
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 刪除會員
	 */
	@Override
	@Transactional
	public void deleteMember(String userId) {
		try {
			Optional<Member> memberResult = this.memberRepository.findById(userId);
			Member member = memberResult.isPresent() ? memberResult.get() : null;
			
			this.memberRepository.deleteById(userId);
			this.userRepository.deleteById(userId);
			
			// 刪除會員大頭貼
			FileTool.deleteFile(member.getMemImg());
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 匯出(依搜尋條件)會員CSV
	 */
	@Override
	public QueryMemberQualifyDTO generateMemberCSV(QueryMemberQualifyDTO dto) throws IOException {
		try {
			dto = this.findMemberAllData(dto,0,0);

			File dir = new File(FileTool.resource_prefix("/images/manager/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(FileTool.resource_prefix("/images/manager/member/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(dir, "會員清冊.csv");
			if (!file.exists()) {
				file.createNewFile();
				log.info("產生csv:{}", file.getName());
			}

			Writer writer = new FileWriter(file);
			CSVWriter csvWriter = new CSVWriter(writer);
			String[] strs = { "社員資格", "註冊時間", "帳號", "姓名", "性別", "生日", "身分證", "手機號碼", "Email", "地址" };
			csvWriter.writeNext(strs);
			dto.getMemberDTOs().forEach(m -> {
				String[] content = new String[10];
				content[0] = (m.getUser().getEnabled() == (byte) 1) ? " ✔" : "";
				content[1] = DateTool.timestampToString(m.getMember().getMemTime());
				content[2] = m.getMember().getMemUser();
				content[3] = m.getMember().getMemName();
				content[4] = m.getMember().getMemGen().equals("M") ? "男" : "女";
				content[5] = DateTool.dateToString(m.getMember().getMemBirth());
				content[6] = m.getMember().getMemIdn();
				content[7] = m.getMember().getMemPhone();
				content[8] = m.getMember().getMemMail();
				content[9] = m.getMember().getMemAddr();
				csvWriter.writeNext(content);
			});
			csvWriter.close();

			return dto;
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 依條件搜尋活動審核(複合式查詢)
	 * 查詢結果總筆數
	 */
	@Override
	public int searchCheckCount(QueryActivityCheckDTO queryDTO, String userNo) {
		try {
			QueryActivityCheckDTO queryCountDTO = new QueryActivityCheckDTO();
			BeanUtils.copyProperties(queryCountDTO, queryDTO);
			int count=this.findActivityAllData(queryCountDTO, userNo,0, 0).getActivityDTOs().size();
			return count;
		} catch (Exception e) {
				e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 活動審核(複合式查詢) 取得活動及搜尋條件
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	@Override
	public QueryActivityCheckDTO findActivityAllData(QueryActivityCheckDTO queryDTO, String userNo,int indexPage,int countOnePage) {
		try {
			String keyWord = queryDTO.getKeyWord();
			String statusPass = queryDTO.getStatusPass();
			String holder = queryDTO.getHolder();

			List<Activity> activities = new ArrayList<>();
			QActivity theActivity = QActivity.activity;
			// 必要條件(一定要不是在草稿中)
			BooleanExpression whereSQL = theActivity.avtEdit.eq((byte) 0);

			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = whereSQL.and(theActivity.avtName.like("%" + keyWord + "%"));
			}
			// 依活動是否通過
			if (!StringUtils.isBlank(statusPass)) {
				whereSQL = whereSQL.and(theActivity.avtPre.eq(("pass".equals(statusPass)) ? (byte) 1 : (byte) 0));
			}
			// 依活動舉辦人
			if (!StringUtils.isBlank(holder)) {
				whereSQL = whereSQL.and(theActivity.member.memUser.eq(holder));
			}
			JPAQuery<Activity> jPAQuerys =jpaQueryFactory.selectFrom(theActivity).from(theActivity).where(whereSQL).orderBy(theActivity.avtDateS.desc());
			if(indexPage==0 && countOnePage==0){ //無分頁
				activities = jPAQuerys.fetch();
			}else{  //有分頁
				if(indexPage>=1){
					indexPage=indexPage-1;
				}
				activities=jPAQuerys.offset(indexPage*7).limit(countOnePage).fetch();
			}
			
			
			log.info("keyWord:{}, statusPass:{}, holder:{}, results:{}", keyWord, statusPass ,holder,activities==null ? null :activities.size());


			List<ActivityDTO> activityDTOs = new ArrayList<>();
			activities.forEach(a -> {
				ActivityDTO dto = new ActivityDTO();
				dto.setActivity(a);
				dto.setActivityDateS(DateTool.dateToString(a.getAvtDateS()));
				dto.setActivityDateE(DateTool.dateToString(a.getAvtDateE()));

				if (this.findAvtPreview(a.getAvtNo(), userNo) != null) {
					dto.setPreviewCheck("on");
				}
				if (userNo.equals(a.getMember().getMemUser())) {
					dto.setOwnActivity("yes");
				}
				activityDTOs.add(dto);
			});
			queryDTO.setActivityDTOs(activityDTOs);
			return queryDTO;
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 活動審核， 管理員在該活動已審核通過。
	 */
	public AvtPreview findAvtPreview(int avtNo, String userNo) {
		QAvtPreview theAvtPreview = QAvtPreview.avtPreview;
		BooleanExpression whereSQL = theAvtPreview.member.memUser.eq(userNo).and(theAvtPreview.activity.avtNo.eq(avtNo))
				.and(theAvtPreview.avtPreCheck.eq((byte) 1));

		AvtPreview avtPreview = jpaQueryFactory.selectFrom(theAvtPreview).from(theAvtPreview).where(whereSQL)
				.fetchFirst();
		return avtPreview;
	}

	/**
	 * 更新活動審核，管理員在該活動審核點選通過與否。
	 */
	@Override
	@Transactional
	public void updateActivity(List<ActivityDTO> activityDTOs, String userNo) {
		try {
			activityDTOs.forEach(a -> {
				if ((a.getActivity().getAvtPre() != (byte) 1) && (!"yes".equals(a.getOwnActivity()))) {

					QAvtPreview theAvtPreview = QAvtPreview.avtPreview;
					BooleanExpression whereSQL = theAvtPreview.member.memUser.eq(userNo)
							.and(theAvtPreview.activity.avtNo.eq(a.getActivity().getAvtNo()));
					AvtPreview avtPreview = jpaQueryFactory.selectFrom(theAvtPreview).from(theAvtPreview)
							.where(whereSQL).fetchFirst();

					if ("on".equals(a.getPreviewCheck())) {

						if (avtPreview == null) {
							avtPreview = new AvtPreview();
							avtPreview.setMember(this.basicService.findMember(userNo));
							avtPreview.setActivity(a.getActivity());
							avtPreview.setAvtPreCheck((byte) 1);
						} else {
							avtPreview.setAvtPreCheck((byte) 1);
						}

						this.avtPreviewRepository.save(avtPreview);

						// 驗證是否通過數量為一半(即更新活動審核為通過)
						if (a.getActivity().getAvtPre() != (byte) 1) {
							this.updateActivityPreview(a.getActivity());
						}
					} else {
						if (avtPreview != null) {
							avtPreview.setAvtPreCheck((byte) 0);
							this.avtPreviewRepository.save(avtPreview);
						}
					}

				}
			});
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 更新"活動"的"活動審核"， (若活動審核通過管理員數量>=管理員數量的二分之一)
	 */
	@Transactional
	public void updateActivityPreview(Activity avt) {
		try {
			List<AvtPreview> avtPreviews = this.findActivityPassed(avt.getAvtNo());

			// 活動審核通過管理員數量
			BigDecimal passCount = new BigDecimal(CollectionUtils.isEmpty(avtPreviews) ? 0 : avtPreviews.size());

			QAuthority theAuthority = QAuthority.authority;
			List<Authority> authorities = jpaQueryFactory.selectFrom(theAuthority).from(theAuthority)
					.where(theAuthority.role.authority.eq("ROLE_MANAGER")).fetch();
			// 管理員總數量
			BigDecimal managerCount = new BigDecimal(CollectionUtils.isEmpty(authorities) ? 0 : authorities.size());

			log.info("活動審核通過管理員數量:{},管理員總數量:{}, 活動名稱:{}", passCount,managerCount,avt.getAvtName());

			
			// "通過數量"需大於或等於管理員數量減一(其中一個管理員為主辦人，不列入計算)的一半
			if (passCount.compareTo((managerCount.subtract(new BigDecimal(1))).divide(new BigDecimal(2))) >= 0) {
				QActivity theActivity = QActivity.activity;
				Activity activity = jpaQueryFactory.selectFrom(theActivity).from(theActivity)
						.where(theActivity.avtNo.eq(avt.getAvtNo())).fetchFirst();
				activity.setAvtPre((byte) 1);
				this.activityRepository.save(activity);
				// 審核通過即寄信件通知
				this.sendMailActivityInfo(activity);
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 寄活動通知信給有勾選活動通知的會員
	 */
	public void sendMailActivityInfo(Activity activity) {
		System systemMail = this.systemRepository.findSystem("系統信箱");

		QMember theMember = QMember.member;
		List<Member> members = jpaQueryFactory.selectFrom(theMember).from(theMember)
				.where(theMember.memInfo.eq((byte) 1)).fetch();

		members.forEach(m -> {
			MailTool.transferActivityInfo(m, activity, systemMail.getSysData(), systemMail.getSysCon());
		});
	}

	/**
	 * 取得該活動活動審核，活動審核已經通過。
	 */
	@Override
	public List<AvtPreview> findActivityPassed(int avtNo) {
		try {
			QAvtPreview theAvtPreview = QAvtPreview.avtPreview;
			List<AvtPreview> avtPreviews = jpaQueryFactory.selectFrom(theAvtPreview).from(theAvtPreview)
					.where(theAvtPreview.activity.avtNo.eq(avtNo).and(theAvtPreview.avtPreCheck.eq((byte) 1))).fetch();
			return avtPreviews;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 匯出(依搜尋條件)活動審核CSV
	 */
	@Override
	public QueryActivityCheckDTO generateCheckCSV(QueryActivityCheckDTO dto, String userNo) throws IOException {	
		

		try {
			dto=this.findActivityAllData(dto, userNo,0,0);

			File dir = new File(FileTool.resource_prefix("/images/manager/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(FileTool.resource_prefix("/images/manager/check/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(dir, "活動審核清冊.csv");
			if (!file.exists()) {
				file.createNewFile();
				log.info("產生csv:{}", file.getName());
			}

			Writer writer = new FileWriter(file);
			CSVWriter csvWriter = new CSVWriter(writer);
			String[] strs = { "活動序號","活動編號", "活動日期", "活動名稱", "活動類型", "活動舉辦人", "您通過的審核", "審核狀態" };
			csvWriter.writeNext(strs);
			for(int i=0;i<dto.getActivityDTOs().size();i++){
				ActivityDTO a=dto.getActivityDTOs().get(i);

				String[] content = new String[8];
				content[0] = String.valueOf(i+1);
				content[1] = "ACT_"+a.getActivity().getAvtNo();
				if(a.getActivity().getAvtDateS().equals(a.getActivity().getAvtDateE())){
					content[2] = DateTool.dateToString(a.getActivity().getAvtDateS());
				}else{
					content[2] = DateTool.dateToString(a.getActivity().getAvtDateS()) +"~" +DateTool.dateToString(a.getActivity().getAvtDateE());;
				}

				content[3] = a.getActivity().getAvtName();
				content[4] = a.getActivity().getActivityType().getAvtTypeName();
				content[5] = a.getActivity().getMember().getMemName();
				if("yes".equals(a.getOwnActivity())){
					content[6] = "您是主辦人";
				}else{
					if("on".equals(a.getPreviewCheck())){
						content[6] = "✔";
					}
				}
				if(a.getActivity().getAvtPre()==(byte)1){
					content[7] = "過半審核通過";
				}
				
				csvWriter.writeNext(content);
			
			}

			csvWriter.close();

			return dto;
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	
	}


}
