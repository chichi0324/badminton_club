package com.badminton.club.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.OtherDataAnRepository;
import com.badminton.club.dao.OtherDataRepository;
import com.badminton.club.dao.SignupAvtRepository;
import com.badminton.club.dto.OtherDataDTO;
import com.badminton.club.dto.QueryActivitySignDTO;
import com.badminton.club.dto.SignAvtDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.OtherData;
import com.badminton.club.entity.OtherDataAn;
import com.badminton.club.entity.QSignupAvt;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerSignUpAvtService;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.FileTool;
import com.opencsv.CSVWriter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
/**
 * 活動管理(我的活動管理-->報名人員清冊部分) 服務
 */
@Service("ManagerSignUpAvtService")
public class ManagerSignUpAvtServiceImpl implements ManagerSignUpAvtService {

	private static final Logger log = LoggerFactory.getLogger(ManagerSignUpAvtServiceImpl.class);

	private BasicService basicService;

	private SignupAvtRepository signupAvtRepository;
	private OtherDataRepository OtherDataRepository;
	private OtherDataAnRepository otherDataAnRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public ManagerSignUpAvtServiceImpl(BasicService basicService, SignupAvtRepository signupAvtRepository,
			com.badminton.club.dao.OtherDataRepository otherDataRepository, OtherDataAnRepository otherDataAnRepository,
			JPAQueryFactory jpaQueryFactory) {
		this.basicService = basicService;
		this.signupAvtRepository = signupAvtRepository;
		OtherDataRepository = otherDataRepository;
		this.otherDataAnRepository = otherDataAnRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	/**
	 * 報名人員清單(搜尋和結果)
	 */
	@Override
	public QueryActivitySignDTO findActivitySignData(QueryActivitySignDTO queryDTO, int avtNo) {
		try {
			// 活動資料
			queryDTO.setActivity(this.basicService.findActivity(avtNo));

			String keyWord = queryDTO.getKeyWord();

			// 活動報名清單
			QSignupAvt theSignupActivity = QSignupAvt.signupAvt;
			List<SignupAvt> signUpAvts = new ArrayList<>();
			// 必要條件:該活動的報名清單
			BooleanExpression whereSQL = theSignupActivity.activity.avtNo.eq(avtNo);
			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = whereSQL.and(theSignupActivity.member.memUser.like("%" + keyWord + "%")
						.or(theSignupActivity.signName.like("%" + keyWord + "%")).or(theSignupActivity.member.memName.like("%" + keyWord + "%")));
			}

			signUpAvts = jpaQueryFactory.selectFrom(theSignupActivity).from(theSignupActivity).where(whereSQL)
					.orderBy(theSignupActivity.signTime.desc()).fetch();
			
			log.info("keyWord:{}, 報名人員數量:{}", keyWord, signUpAvts==null ? null :signUpAvts.size());


			List<SignAvtDTO> signDTOs = new ArrayList<>();
			signUpAvts.forEach(s -> {
				SignAvtDTO dto = new SignAvtDTO();
				dto.setSignupAvt(s);
				dto.setBirthDate(DateTool.dateToString(s.getSignBirth()));

				signDTOs.add(dto);
			});
			queryDTO.setSignAvtDTOs(signDTOs);
			queryDTO.setCount(this.signupAvtRepository.getActivitySum(avtNo));

			return queryDTO;
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 刪除報名資料
	 */
	@Override
	@Transactional
	public void deleteSignAvt(int signAvtNo) {
		try {

			Optional<SignupAvt> signupAvtResult = signupAvtRepository.findById(signAvtNo);
			SignupAvt signAvtDelete = signupAvtResult.isPresent() ? signupAvtResult.get() : null;

			QSignupAvt theSignupAvt = QSignupAvt.signupAvt;

			// 若刪除會員，該會員有帶親友，親友獨立參加，親友人數給1
			if (!StringUtils.isBlank(signAvtDelete.getSignUser()) && signAvtDelete.getSignCount() > 1) {
				List<SignupAvt> signUpAvts = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
						.where(theSignupAvt.member.memUser.eq(signAvtDelete.getSignUser())
								.and(theSignupAvt.activity.avtNo.eq(signAvtDelete.getActivity().getAvtNo())))
						.fetch();
				signUpAvts.forEach(s -> {
					s.setSignCount(1);
				});
				signupAvtRepository.saveAll(signUpAvts);
			}

			// 若刪除為某會員的親友，會員沒被刪除，會員親友人數減1
			if (StringUtils.isBlank(signAvtDelete.getSignUser())) {
				SignupAvt signUpAvt = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
						.where(theSignupAvt.signUser.eq(signAvtDelete.getMember().getMemUser())
								.and(theSignupAvt.activity.avtNo.eq(signAvtDelete.getActivity().getAvtNo())))
						.fetchFirst();

				if (signUpAvt != null) {
					signUpAvt.setSignCount(signUpAvt.getSignCount() - 1);
					signupAvtRepository.save(signUpAvt);
				}
			}

			this.signupAvtRepository.delete(signAvtDelete);
			
			//活動限制名額是否額滿，若額滿更新活動狀態(改為"已額滿")，反之為其他狀態
			basicService.updateActivityStatus(signAvtDelete.getActivity().getAvtNo());

		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 匯出(依搜尋條件)報名人員清單CSV
	 */
	@Override
	public QueryActivitySignDTO generateSignAvtCSV(QueryActivitySignDTO dto) throws IOException {
		try {
			dto = this.findActivitySignData(dto, dto.getActivity().getAvtNo());

			File dir = new File(FileTool.resource_prefix("/images/manager/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(FileTool.resource_prefix("/images/manager/activity/"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(dir, dto.getActivity().getAvtName() + "報名人員清冊.csv");
			if (!file.exists()) {
				file.createNewFile();
				log.info("產生csv:{}", file.getName());

			}
			


			Writer writer = new FileWriter(file);
			CSVWriter csvWriter = new CSVWriter(writer);
			int titleHeader = 11 + dto.getActivity().getOtherData().size();
			String[] titleStrs = new String[titleHeader];
			titleStrs[0] = "註冊時間";
			int x = 1;
			if (dto.getActivity().getAvtFrdData() == (byte) 1) {
				titleStrs[1] = "會員親友";
				x=0;
			}
			titleStrs[2-x] = "姓名";
			titleStrs[3-x] = "性別";
			titleStrs[4-x] = "生日";
			titleStrs[5-x] = "身分證";
			titleStrs[6-x] = "手機號碼";
			titleStrs[7-x] = "Email";
			titleStrs[8-x] = "地址";
			int i = 8-x;
			for (OtherData data : dto.getActivity().getOtherData()) {
				i = i + 1;
				titleStrs[i] = data.getOthName();
			}

			titleStrs[i + 1] = "會員帳號";
			titleStrs[i + 2] = "人數";

			csvWriter.writeNext(titleStrs);
			for (SignAvtDTO m : dto.getSignAvtDTOs()){
				String[] content = new String[titleHeader];
				content[0] = DateTool.timestampToString(m.getSignupAvt().getSignTime());
				int y = 1;
				if (dto.getActivity().getAvtFrdData() == (byte) 1) {
					content[1] = m.getSignupAvt().getMember().getMemName();
					y=0;
				}
				content[2-y] = m.getSignupAvt().getSignName();
				content[3-y] = m.getSignupAvt().getSignGen().equals("M") ? "男" : "女";
				content[4-y] = DateTool.dateToString(m.getSignupAvt().getSignBirth());
				content[5-y] = m.getSignupAvt().getSignIdn();
				content[6-y] = m.getSignupAvt().getSignPhone();
				content[7-y] = m.getSignupAvt().getSignMail();
				content[8-y] = m.getSignupAvt().getSignAddr();
				int j = 8-y;
				for (OtherDataAn ans : m.getSignupAvt().getOtherDataAns()) {
					j = j + 1;
					content[j] = ans.getOtheaCon();
				}

				content[j + 1] = m.getSignupAvt().getSignUser();
				content[j + 2] = Integer.toString(m.getSignupAvt().getSignCount());
				csvWriter.writeNext(content);
			};
			csvWriter.close();

			return dto;
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 新增修改報名資料畫面帶入
	 */
	@Override
	public void editSignAvt(SignAvtDTO queryDTO) {

		// 活動資料
		Activity activity = this.basicService.findActivity(queryDTO.getActivity().getAvtNo());
		queryDTO.setActivity(activity);

		// 活動報名資料
		if (queryDTO.getSignupAvt() != null) {
			if (queryDTO.getSignupAvt().getSignNo() != 0) {
				SignupAvt signAvt = this.basicService.findSignupAvt(queryDTO.getSignupAvt().getSignNo());
				queryDTO.setSignupAvt(signAvt);
				queryDTO.setBirthDate(DateTool.dateToString(signAvt.getSignBirth()));
				// 人數
				queryDTO.setCount(String.valueOf(queryDTO.getSignupAvt().getSignCount()));

			}
		}
		
		if(CollectionUtils.isEmpty(queryDTO.getOtherDatas())){
			// 除了個人資料外．其他資料
			activity.getOtherData().forEach(o -> {
				OtherDataDTO other = new OtherDataDTO();
				other.setNo(String.valueOf(o.getOthNo()));
				other.setName(o.getOthName());
				if (queryDTO.getSignupAvt() != null) {
					if (!CollectionUtils.isEmpty(queryDTO.getSignupAvt().getOtherDataAns())) {
						queryDTO.getSignupAvt().getOtherDataAns().forEach(an -> {
							if (an.getOtherData().getOthNo() == o.getOthNo()) {
								other.setData(an.getOtheaCon());
							}
						});
					}
				}
				queryDTO.getOtherDatas().add(other);
			});
		}



	}

	/**
	 * 存檔報名資料(新增/修改)
	 * 
	 * @throws ParseException
	 */
	@Override
	@Transactional
	public void saveEditSignAvt(SignAvtDTO queryDTO){
		try {
			log.info("活動新增或修改:{}", queryDTO.getAddOrEdit());

			// 活動資料
			Activity activity = this.basicService.findActivity(queryDTO.getActivity().getAvtNo());
			SignupAvt signupAvt = null;
			if ("新增人員".equals(queryDTO.getAddOrEdit())) {

				signupAvt = queryDTO.getSignupAvt();
				signupAvt.setSignBirth(DateTool.getDate(queryDTO.getBirthDate()));
				// 若該活動不須親友資料，會員帳號帶入該會員
				if (queryDTO.getActivity().getAvtFrdData() == (byte) 0) {
					signupAvt.setMember(this.basicService.findMember(queryDTO.getMember().getMemUser()));
					queryDTO.getSignupAvt().setMember(signupAvt.getMember());
				} else {
					signupAvt.setMember(this.basicService.findMember(queryDTO.getSignupAvt().getMember().getMemUser()));
				}
				// 若親友可以參加，但不須要親友資料，帶入畫面人數
				if (queryDTO.getActivity().getAvtOnly() == (byte) 0
						&& queryDTO.getActivity().getAvtFrdData() == (byte) 0) {
					signupAvt.setSignCount(Integer.parseInt(queryDTO.getCount()));
				} else {
					signupAvt.setSignCount(this.getSignCountForSave(queryDTO));
				}

				signupAvt.setActivity(activity);

				// 若新增為會員，需帶入報名人員會員帳號
				if (queryDTO.getMember() != null && !StringUtils.isBlank(queryDTO.getMember().getMemUser())) {
					signupAvt.setSignUser(queryDTO.getMember().getMemUser());
				}

				this.signupAvtRepository.save(signupAvt);
				SignupAvt newSignupAvt = this.getNewSignupAvt(signupAvt);

				queryDTO.getOtherDatas().forEach(oth -> {
					Optional<OtherData> otherDataResult = this.OtherDataRepository
							.findById(Integer.parseInt(oth.getNo()));
					OtherData otherData = otherDataResult.isPresent() ? otherDataResult.get() : null;

					OtherDataAn otherDataAn = new OtherDataAn();
					otherDataAn.setOtherData(otherData);
					otherDataAn.setOtheaCon(oth.getData());
					otherDataAn.setSignupAvt(newSignupAvt);

					this.otherDataAnRepository.save(otherDataAn);
				});
				
			} else {
				SignupAvt signupAvtModify = queryDTO.getSignupAvt();
				signupAvt = this.basicService.findSignupAvt(signupAvtModify.getSignNo());
				signupAvt.setSignName(signupAvtModify.getSignName());
				signupAvt.setSignBirth(DateTool.getDate(queryDTO.getBirthDate()));
				signupAvt.setSignGen(signupAvtModify.getSignGen());
				signupAvt.setSignIdn(signupAvtModify.getSignIdn());
				signupAvt.setSignPhone(signupAvtModify.getSignPhone());
				signupAvt.setSignMail(signupAvtModify.getSignMail());
				signupAvt.setSignAddr(signupAvtModify.getSignAddr());
				// 若親友可以參加，但不須要親友資料，帶入畫面人數
				if (queryDTO.getActivity().getAvtOnly() == (byte) 0
						&& queryDTO.getActivity().getAvtFrdData() == (byte) 0) {
					signupAvt.setSignCount(Integer.parseInt(queryDTO.getCount()));
				}
				this.signupAvtRepository.save(signupAvt);

				for (OtherDataDTO o : queryDTO.getOtherDatas()) {
					for (OtherDataAn an : signupAvt.getOtherDataAns()) {
						if (an.getOtherData().getOthNo() == Integer.parseInt(o.getNo())) {
							an.setOtheaCon(o.getData());
							this.otherDataAnRepository.save(an);
						}
					}
				}

			}
			
			//活動限制名額是否額滿，若額滿更新活動狀態(改為"已額滿")
			basicService.updateActivityStatus(queryDTO.getActivity().getAvtNo());

		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 報名人員清單人數部分
	 */
	@Transactional
	public int getSignCountForSave(SignAvtDTO queryDTO) {
		try {
		QSignupAvt theSignupAvt = QSignupAvt.signupAvt;
		// 若新增為會員
		// 若報名清單已經有該會員的親友，親友的人數給0，會員的報名清單帶入會員(含會員親友)的總人數．
		if (queryDTO.getMember() != null && !StringUtils.isBlank(queryDTO.getMember().getMemUser())) {
			List<SignupAvt> signupAvts = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
					.where(theSignupAvt.member.memUser.eq(queryDTO.getSignupAvt().getMember().getMemUser())
							.and(theSignupAvt.activity.avtNo.eq(queryDTO.getActivity().getAvtNo())))
					.fetch();
			signupAvts.forEach(s -> {
				s.setSignCount(0);
				this.signupAvtRepository.save(s);
			});
			if (CollectionUtils.isEmpty(signupAvts)) {
				return 1;
			} else {
				return signupAvts.size() + 1;
			}

			// 若新增為非會員(親友)
			// 若報名清單已經有該親友的會員，親友的人數給0，該親友的會員人數加1．
			// 若報名清單已經不含該親友的會員，親友的人數給1
		} else {
			SignupAvt signupAvt = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
					.where(theSignupAvt.signUser.eq(queryDTO.getSignupAvt().getMember().getMemUser())
							.and(theSignupAvt.member.memUser.eq(queryDTO.getSignupAvt().getMember().getMemUser()))
							.and(theSignupAvt.activity.avtNo.eq(queryDTO.getActivity().getAvtNo())))
					.fetchFirst();

			if (signupAvt == null) {
				return 1;
			} else {
				signupAvt.setSignCount(signupAvt.getSignCount() + 1);
				this.signupAvtRepository.save(signupAvt);
				return 0;
			}
		}
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 查詢報名資料
	 */
	@Override
	public List<SignupAvt> findSignupAvt(String user, String name, String idn, int avtNo) {
		try {
			QSignupAvt theSignupAvt = QSignupAvt.signupAvt;
			List<SignupAvt> signupAvts = null;
			// 若非會員帶入資料，檢查依姓名和身份證，是否重複
			if (StringUtils.isBlank(user)) {
				signupAvts = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt).where(theSignupAvt.signName
						.eq(name).and(theSignupAvt.signIdn.eq(idn)).and(theSignupAvt.activity.avtNo.eq(avtNo))).fetch();
				// 若會員帶入資料，檢查報名人員帳號，是否重複
			} else {
				signupAvts = jpaQueryFactory.selectFrom(theSignupAvt).from(theSignupAvt)
						.where(theSignupAvt.signUser.eq(user).and(theSignupAvt.activity.avtNo.eq(avtNo))).fetch();
			}
			return signupAvts;
		} catch (Exception e) {
			throw e;
		}
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
	 * 會員資料帶入報名新增人員
	 */
	@Override
	public void loadMemberToSignAvt(SignAvtDTO addDTO) {

		SignupAvt signupAvt = new SignupAvt();
		signupAvt.setSignUser(addDTO.getMember().getMemUser());
		signupAvt.setSignName(addDTO.getMember().getMemName());
		signupAvt.setSignBirth(addDTO.getMember().getMemBirth());
		signupAvt.setSignGen(addDTO.getMember().getMemGen());
		signupAvt.setSignIdn(addDTO.getMember().getMemIdn());
		signupAvt.setSignPhone(addDTO.getMember().getMemPhone());
		signupAvt.setSignMail(addDTO.getMember().getMemMail());
		signupAvt.setSignAddr(addDTO.getMember().getMemAddr());
		signupAvt.setMember(addDTO.getMember());
		addDTO.setSignupAvt(signupAvt);
		addDTO.setBirthDate(DateTool.dateToString(signupAvt.getSignBirth()));
	}

}
