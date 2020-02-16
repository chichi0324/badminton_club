package com.badminton.club.service.impl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import com.badminton.club.dao.ActivityTypeRepository;
import com.badminton.club.dao.AdvocateRepository;
import com.badminton.club.dao.OtherDataAnRepository;
import com.badminton.club.dao.OtherDataRepository;
import com.badminton.club.dto.ActivityDTO;
import com.badminton.club.dto.QueryActivityCheckDTO;
import com.badminton.club.dto.QueryActivityHoldDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.ActivityType;
import com.badminton.club.entity.Advocate;
import com.badminton.club.entity.OtherData;
import com.badminton.club.entity.QActivity;
import com.badminton.club.entity.QAdvocate;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerActivityService;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.FileTool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * 活動管理(我的活動管理) 服務
 */
@Service("ManagerActivityService")
public class ManagerActivityServiceImpl implements ManagerActivityService {

	private static final Logger log = LoggerFactory.getLogger(ManagerActivityServiceImpl.class);

	private BasicService basicService;

	private ActivityRepository activityRepository;
	private ActivityTypeRepository activityTypeRepository;
	private AdvocateRepository advocateRepository;
	private OtherDataRepository OtherDataRepository;
	private OtherDataAnRepository otherDataAnRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public ManagerActivityServiceImpl(BasicService basicService, ActivityRepository activityRepository,
			ActivityTypeRepository activityTypeRepository, AdvocateRepository advocateRepository,
			com.badminton.club.dao.OtherDataRepository otherDataRepository, OtherDataAnRepository otherDataAnRepository,
			JPAQueryFactory jpaQueryFactory) {
		super();
		this.basicService = basicService;
		this.activityRepository = activityRepository;
		this.activityTypeRepository = activityTypeRepository;
		this.advocateRepository = advocateRepository;
		OtherDataRepository = otherDataRepository;
		this.otherDataAnRepository = otherDataAnRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}
	
	/**
	 * 依條件搜尋活動審核(複合式查詢)
	 * 查詢我的活動管理(複合式查詢)總筆數
	 */
	@Override
	public int searchActivityHoldCount(QueryActivityHoldDTO queryDTO, String userNo) {
		try {
			QueryActivityHoldDTO queryCountDTO = new QueryActivityHoldDTO();
			BeanUtils.copyProperties(queryCountDTO, queryDTO);
			int count=this.findAllActivityHold(queryCountDTO, userNo,0, 0).getActivityDTOs().size();
			return count;
		} catch (Exception e) {
				e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 我的活動管理(複合式查詢) 取得該管理員主辦的活動及搜尋條件
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 */
	@Override
	public QueryActivityHoldDTO findAllActivityHold(QueryActivityHoldDTO queryDTO, String userNo,int indexPage,int countOnePage) {
		try {
			String keyWord = queryDTO.getKeyWord();
			String type = queryDTO.getType();
			String checkStatus = queryDTO.getCheckStatus();
			String signUpStatus = queryDTO.getSignUpStatus();

			QActivity theActivity = QActivity.activity;
			// 必要條件(該管理員舉辦的活動)
			BooleanExpression whereSQL = theActivity.member.memUser.eq(userNo);

			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = whereSQL.and(theActivity.avtName.like("%" + keyWord + "%"));
			}
			// 依活動類型
			if (!StringUtils.isBlank(type)) {
				whereSQL = whereSQL.and(theActivity.activityType.avtTypeNo.eq(Integer.valueOf(type)));
			}
			// 依審核狀態
			if (!StringUtils.isBlank(checkStatus)) {
				if ("pass".equals(checkStatus)) {
					whereSQL = whereSQL.and(theActivity.avtPre.eq((byte) 1));// 已審核
				} else if ("no".equals(checkStatus)) {
					whereSQL = whereSQL.and(theActivity.avtPre.eq((byte) 0)).and(theActivity.avtEdit.eq((byte) 0));// 待審核
				} else {
					whereSQL = whereSQL.and(theActivity.avtEdit.eq((byte) 1));// 草稿
				}
			}
			// 依活動狀態
			if (!StringUtils.isBlank(signUpStatus)) {

				switch (signUpStatus) {
				case "1":
					signUpStatus = "報名中";
					whereSQL = whereSQL.and(theActivity.avtStat.eq(signUpStatus));
					break;
				case "2":
					signUpStatus = "已額滿";
					whereSQL = whereSQL.and(theActivity.avtStat.eq(signUpStatus));
					break;
				case "3":
					signUpStatus = "已截止";
					whereSQL = whereSQL.and(theActivity.avtStat.eq(signUpStatus));
					break;
				case "4":
					signUpStatus = "已結束";
					whereSQL = whereSQL.and(theActivity.avtStat.eq(signUpStatus));
					break;
				case "5":
					whereSQL = whereSQL.and(theActivity.avtEdit.eq((byte) 1));
					break;
				default:
					break;
				}

			}
			JPAQuery<Activity> jPAQuerys =jpaQueryFactory.selectFrom(theActivity).from(theActivity).where(whereSQL)
					.orderBy(theActivity.avtDateS.desc());

			List<Activity> activities = new ArrayList<>();
			if(indexPage==0 && countOnePage==0){ //無分頁
				activities = jPAQuerys.fetch();
			}else{  //有分頁
				if(indexPage>=1){
					indexPage=indexPage-1;
				}
				activities=jPAQuerys.offset(indexPage*7).limit(countOnePage).fetch();
			}
			log.info("keyWord:{}, type:{}, checkStatus:{}, signUpStatus:{}", keyWord, type, checkStatus,signUpStatus);


			List<ActivityDTO> activityDTOs = new ArrayList<>();
			activities.forEach(a -> {
				ActivityDTO dto = new ActivityDTO();
				dto.setActivity(a);
				dto.setActivityDateS(DateTool.dateToString(a.getAvtDateS()));
				dto.setActivityDateE(DateTool.dateToString(a.getAvtDateE()));

				activityDTOs.add(dto);
			});
			queryDTO.setActivityDTOs(activityDTOs);

			return queryDTO;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 我的活動管理(複合式查詢) 刪除活動即相關資料(子table和圖片)
	 */
	@Override
	@Transactional
	public void deleteActivity(int avtNo) {
		try {
			QActivity theActivity = QActivity.activity;
			Activity activity = jpaQueryFactory.selectFrom(theActivity).from(theActivity)
					.where(theActivity.avtNo.eq(avtNo)).fetchFirst();
			// 刪除活動圖片
			FileTool.deleteFile(activity.getAvtImg());
			// 刪除活動宣傳圖片
			activity.getAdvocates().forEach(advo -> {
				FileTool.deleteFile(advo.getAdvImg());
			});
			// 刪除活動宣傳資料夾
			FileTool.deleteDirectory("/images/activity/avt_" + activity.getAvtNo());

			this.activityRepository.delete(activity);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 我的活動管理(修改) 取得活動資料
	 */
	@Override
	public ActivityDTO getActivityDTO(int avtNo) {
		ActivityDTO activityDTO = new ActivityDTO();
		Activity activity = this.basicService.findActivity(avtNo);
		activityDTO.setActivity(activity);
		activityDTO.setActivityDateS(DateTool.dateToString(activity.getAvtDateS()));
		activityDTO.setActivityDateE(DateTool.dateToString(activity.getAvtDateE()));
		activityDTO.setAvtGatDate(DateTool.timestampToStringForActivity(activity.getAvtGatDate()));
		activityDTO.setAvtCutDate(DateTool.dateToString(activity.getAvtCutDate()));

		if (activity.getAvtOnly() == (byte) 0) {
			activityDTO.setEnableFriend("on");
		}

		if (activity.getAvtFrdData() == (byte) 1) {
			activityDTO.setFriendData("on");
		}

		activity.getOtherData().forEach(o -> {
			activityDTO.getOtherDatas().add(o.getOthName());
		});

		if (CollectionUtils.isEmpty(activity.getOtherData())) {
			activityDTO.getOtherDatas().add("");
		}
		return activityDTO;
	}

	/**
	 * 我的活動管理(修改) 取得活動圖片
	 */
	@Override
	public void getActivityAdvocateDTO(ActivityDTO activityDTO, String username) {

		Activity activity = this.basicService.findActivity(activityDTO.getActivity().getAvtNo());
		if (activity == null) {

			activityDTO.getAvtImgDTO().setAvtImg("/images/activity/avt_" + username + ".jpg");
			activityDTO.getActivity().setAdvocates(new ArrayList<>());
			activityDTO.getAvtImgDTO().setUplImg1("/images/activity/avt_" + username + "/adv_1.jpg");
			activityDTO.getAvtImgDTO().setUplImg2("/images/activity/avt_" + username + "/adv_2.jpg");
			activityDTO.getAvtImgDTO().setUplImg3("/images/activity/avt_" + username + "/adv_3.jpg");
			activityDTO.getAvtImgDTO().setUplImg4("/images/activity/avt_" + username + "/adv_4.jpg");
			activityDTO.getAvtImgDTO().setUplImg5("/images/activity/avt_" + username + "/adv_5.jpg");

		} else {
			// 活動圖片
			if (StringUtils.isBlank(activity.getAvtImg())) {
				activityDTO.getAvtImgDTO().setAvtImg("/images/activity/avt_" + activity.getAvtNo() + ".jpg");
			} else {
				activityDTO.getActivity().setAvtImg(activity.getAvtImg());
				activityDTO.getAvtImgDTO().setAvtImg(activity.getAvtImg());
			}

			// 活動宣傳圖片
			if (CollectionUtils.isEmpty(activity.getAdvocates())) {
				activityDTO.getActivity().setAdvocates(new ArrayList<>());
				activityDTO.getAvtImgDTO().setUplImg1("/images/activity/avt_" + activity.getAvtNo() + "/adv_1.jpg");
				activityDTO.getAvtImgDTO().setUplImg2("/images/activity/avt_" + activity.getAvtNo() + "/adv_2.jpg");
				activityDTO.getAvtImgDTO().setUplImg3("/images/activity/avt_" + activity.getAvtNo() + "/adv_3.jpg");
				activityDTO.getAvtImgDTO().setUplImg4("/images/activity/avt_" + activity.getAvtNo() + "/adv_4.jpg");
				activityDTO.getAvtImgDTO().setUplImg5("/images/activity/avt_" + activity.getAvtNo() + "/adv_5.jpg");
			} else {
				activityDTO.getActivity().setAdvocates(activity.getAdvocates());
				activityDTO.getAvtImgDTO()
						.setUplImg1(activity.getAdvocates().size() > 0 ? activity.getAdvocates().get(0).getAdvImg()
								: "/images/activity/avt_" + activity.getAvtNo() + "/adv_1.jpg");
				activityDTO.getAvtImgDTO()
						.setUplImg2(activity.getAdvocates().size() > 1 ? activity.getAdvocates().get(1).getAdvImg()
								: "/images/activity/avt_" + activity.getAvtNo() + "/adv_2.jpg");
				activityDTO.getAvtImgDTO()
						.setUplImg3(activity.getAdvocates().size() > 2 ? activity.getAdvocates().get(2).getAdvImg()
								: "/images/activity/avt_" + activity.getAvtNo() + "/adv_3.jpg");
				activityDTO.getAvtImgDTO()
						.setUplImg4(activity.getAdvocates().size() > 3 ? activity.getAdvocates().get(3).getAdvImg()
								: "/images/activity/avt_" + activity.getAvtNo() + "/adv_4.jpg");
				activityDTO.getAvtImgDTO()
						.setUplImg5(activity.getAdvocates().size() > 4 ? activity.getAdvocates().get(4).getAdvImg()
								: "/images/activity/avt_" + activity.getAvtNo() + "/adv_5.jpg");
			}

			// 是否為草稿
			activityDTO.setAvtEdit(activity.getAvtEdit() == (byte) 1 ? "on" : "");

		}
	}

	/**
	 * 我的活動管理(新增修改存檔)
	 * 
	 * 回傳 該筆 新增修改 活動編號
	 */
	@Override
	@Transactional
	public int saveActivity(ActivityDTO PreviousActivityDTO, ActivityDTO activityDTO, String username) {
		try {
			Activity activity = null;
			
			log.info("Activity AddOrEdit:{}",PreviousActivityDTO.getAddOrEdit());

			if ("新增活動".equals(PreviousActivityDTO.getAddOrEdit())) {
				activity = new Activity();
			} else {
				activity = this.basicService.findActivity(PreviousActivityDTO.getActivity().getAvtNo());
			}
			activity.setAvtName(PreviousActivityDTO.getActivity().getAvtName());
			activity.setActivityType(
					this.findActivityType(PreviousActivityDTO.getActivity().getActivityType().getAvtTypeNo()));
			activity.setAvtDateS(DateTool.getDate(PreviousActivityDTO.getActivityDateS()));
			activity.setAvtDateE(DateTool.getDate(PreviousActivityDTO.getActivityDateE()));
			activity.setAvtGatDate(DateTool.DateLocalTotimestamp(PreviousActivityDTO.getAvtGatDate()));
			activity.setAvtLoc(PreviousActivityDTO.getActivity().getAvtLoc());
			activity.setAvtPrice(PreviousActivityDTO.getActivity().getAvtPrice());
			activity.setAvtCutDate(DateTool.getDate(PreviousActivityDTO.getAvtCutDate()));
			activity.setAvtOnly("on".equals(PreviousActivityDTO.getEnableFriend()) ? (byte) 0 : (byte) 1);
			activity.setAvtFrdData("on".equals(PreviousActivityDTO.getFriendData()) ? (byte) 1 : (byte) 0);
			activity.setAvtIntr(PreviousActivityDTO.getActivity().getAvtIntr());
			activity.setAvtMemo(PreviousActivityDTO.getActivity().getAvtMemo());

			activity.setMember(this.basicService.findMember(username));

			activity.setAvtOnly("on".equals(PreviousActivityDTO.getEnableFriend()) ? (byte) 0 : (byte) 1);
			activity.setAvtFrdData("on".equals(PreviousActivityDTO.getFriendData()) ? (byte) 1 : (byte) 0);

			activity.setAvtEdit("on".equals(activityDTO.getAvtEdit()) ? (byte) 1 : (byte) 0);
			if (!"on".equals(activityDTO.getAvtEdit()) && activity.getAvtStat() == null) {
				activity.setAvtStat("報名中");
			}
			
			activity.setAvtUpp(PreviousActivityDTO.getActivity().getAvtUpp());
			activity.setAvtLow(PreviousActivityDTO.getActivity().getAvtLow());
			
			//新增/修改  舊活動
			this.setOldActivity(activity);

			if ("新增活動".equals(PreviousActivityDTO.getAddOrEdit())) {

				this.activityRepository.save(activity);

				QActivity theActivity = QActivity.activity;
				activity = jpaQueryFactory.selectFrom(theActivity).from(theActivity)
						.where(theActivity.member.memUser.eq(username)).orderBy(theActivity.avtNo.desc()).fetchFirst();

				activity.setAvtImg("/images/activity/avt_" + activity.getAvtNo() + ".jpg");
				
				this.activityRepository.save(activity);
				PreviousActivityDTO.setActivity(activity);

				// 活動宣傳圖片
				this.saveActivityAdvocate(PreviousActivityDTO, username);

				// 報名其他資料
				this.saveActivityOtherdata(PreviousActivityDTO);
			} else {
				log.info("是否審核通過:{}",activity.getAvtPre());

				
				// 未審核通過可以修改
				if (activity.getAvtPre() == (byte) 0) {

					PreviousActivityDTO.setActivity(activity);
					// 報名其他資料
					this.saveActivityOtherdata(PreviousActivityDTO);
				}

				this.activityRepository.save(activity);
				PreviousActivityDTO.setActivity(activity);

				// 活動宣傳圖片
				this.saveActivityAdvocate(PreviousActivityDTO, username);
				
				//修改時，更新活動狀態
				if("修改活動".equals(PreviousActivityDTO.getAddOrEdit())){
					List<OtherData> otherDatas=this.OtherDataRepository.findAllOtherDataActivity(activity.getAvtNo());
					activity.setOtherData(otherDatas);
					this.basicService.updateActivityStatus(activity);
				}
			}

			return PreviousActivityDTO.getActivity().getAvtNo();
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * "舊活動"資料
	 */
	public void setOldActivity(Activity activity) {
		try {
			
			Date nowDate=new Date();
			if(nowDate.after(activity.getAvtDateS())){
				activity.setAvtStat("已結束");
				activity.setAvtPre((byte)1);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得"活動類型"資料
	 */
	public ActivityType findActivityType(int avtTypeNo) {
		try {
			Optional<ActivityType> activityTypeResult = this.activityTypeRepository.findById(avtTypeNo);
			ActivityType activityType = activityTypeResult.isPresent() ? activityTypeResult.get() : null;
			return activityType;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * "活動宣傳圖片"存檔
	 */
	@Transactional
	public void saveActivityAdvocate(ActivityDTO PreviousActivityDTO, String username) {
		try {
			if ("新增活動".equals(PreviousActivityDTO.getAddOrEdit())) {

				File file = new File(FileTool.resource_prefix("/images/activity/avt_" + username + ".jpg"));
				File newFile = new File(FileTool.resource_prefix(
						"/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + ".jpg"));
				
				log.info("圖片:{},改名為:{},改名是否成功:{}",  file.getName() , newFile.getName() ,file.renameTo(newFile));

				File dir = new File(FileTool.resource_prefix("/images/activity/avt_" + username + "/"));
				File newDir = new File(FileTool
						.resource_prefix("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/"));
				
				log.info("資料夾:{},改名為:{},改名是否成功:{}",  dir.getName() , newDir.getName() ,dir.renameTo(newDir));


				if (newDir.exists()) {
					File TrxFiles[] = newDir.listFiles(); //此資料夾底下檔案
					for (int i = 0; i < TrxFiles.length; i++) {
						if (!".DS_Store".equals(TrxFiles[i].getName().trim())) {
							Advocate advocate = new Advocate();
							advocate.setActivity(PreviousActivityDTO.getActivity());
							advocate.setAdvImg("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo()
									+ "/" + "adv_" + (i + 1) + ".jpg");
							log.info("此資料夾底下圖片:{}",  advocate.getAdvImg());
							this.advocateRepository.save(advocate);
						}
					}
				}

			} else {
				QAdvocate theAdvocate = QAdvocate.advocate;

				File dir = new File(FileTool
						.resource_prefix("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/"));
				
				log.info("此資料夾:{}",  "/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/");
				
				if (dir.exists()) {
					File TrxFiles[] = dir.listFiles();
					for (int i = 0; i < TrxFiles.length; i++) {
						if (!".DS_Store".equals(TrxFiles[i].getName().trim())) {
							Advocate advocate = jpaQueryFactory.selectFrom(theAdvocate).from(theAdvocate)
									.where(theAdvocate.advImg
											.eq("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo()
													+ "/" + TrxFiles[i].getName()))
									.fetchFirst();
							if (advocate == null) {
								advocate = new Advocate();
								advocate.setActivity(PreviousActivityDTO.getActivity());
								advocate.setAdvImg("/images/activity/avt_"
										+ PreviousActivityDTO.getActivity().getAvtNo() + "/" + TrxFiles[i].getName());
								this.advocateRepository.save(advocate);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}

	/**
	 * "報名其他資料"存檔
	 */
	@Transactional
	public void saveActivityOtherdata(ActivityDTO PreviousActivityDTO) {
		try {
			if (!"新增活動".equals(PreviousActivityDTO.getAddOrEdit())) {
				this.OtherDataRepository.deleteAll(PreviousActivityDTO.getActivity().getOtherData());
			}

			for (int i = 0; i < PreviousActivityDTO.getOtherDatas().size(); i++) {
				String data = PreviousActivityDTO.getOtherDatas()
						.get(PreviousActivityDTO.getOtherDatas().size() - i - 1);
				if (!StringUtils.isBlank(data)) {
					OtherData otherData = new OtherData();
					otherData.setActivity(PreviousActivityDTO.getActivity());
					otherData.setOthName(data);
					this.OtherDataRepository.save(otherData);
				}

			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}

	}

	/**
	 * 我的活動管理(新增修改) 刪除活動宣傳圖片
	 */
	@Override
	@Transactional
	public void deleteAdvocate(String fileName) {
		try {
			QAdvocate theAdvocate = QAdvocate.advocate;
			Advocate advocate = jpaQueryFactory.selectFrom(theAdvocate).from(theAdvocate)
					.where(theAdvocate.advImg.eq(fileName)).fetchFirst();
			if (advocate != null) {
				this.advocateRepository.delete(advocate);
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

}
