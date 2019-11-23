package com.badminton.club.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.MemberRepository;
import com.badminton.club.dao.UserRepository;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.QSignupAvt;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.MemberService;
import com.badminton.club.tools.BCrypt;
import com.badminton.club.tools.DateTool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
/**
 * 社員專區(社員資料修改，我的活動) 服務
 */
@Service("MemberService")
public class MemberServiceImpl implements MemberService {
	
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

	private BasicService basicService;
	private MemberRepository memberRepository;
	private UserRepository userRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public MemberServiceImpl(BasicService basicService, MemberRepository memberRepository,
			UserRepository userRepository, JPAQueryFactory jpaQueryFactory) {
		this.basicService = basicService;
		this.memberRepository = memberRepository;
		this.userRepository = userRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	/**
	 * 所有會員
	 */
	@Override
	public List<Member> findAll() {
		try {
			return this.memberRepository.findAllMemberByDesc();
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 取得"使用者會員資料"
	 */
	@Override
	public MemberDTO findMemberData(String userNo) {
		try {
			Member member = this.basicService.findMember(userNo);

			User user = this.basicService.findUser(userNo);

			if (member == null || user == null) {
				return new MemberDTO();
			}

			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setMember(member);
			memberDTO.setUser(user);

			memberDTO.setCkeckPwd(user.getPassword().substring(6, 12));
			memberDTO.setBirthDate(DateTool.dateToString(member.getMemBirth()));
			memberDTO.setInform(member.getMemInfo() == (byte) 1 ? "on" : null);

			return memberDTO;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 修改"會員"資料
	 */
	@Override
	@Transactional
	public MemberDTO updateMemberData(String userNo, MemberDTO dto){
		try {
			MemberDTO memberDTO = this.findMemberData(userNo);

			Member member = memberDTO.getMember();
			member.setMemName(dto.getMember().getMemName());
			member.setMemGen(dto.getMember().getMemGen());
			member.setMemBirth(DateTool.getDate(dto.getBirthDate()));
			member.setMemIdn(dto.getMember().getMemIdn());
			member.setMemPhone(dto.getMember().getMemPhone());
			member.setMemMail(dto.getMember().getMemMail());
			member.setMemAddr(dto.getMember().getMemAddr());
			member.setMemInfo("on".equals(dto.getInform()) ? (byte) 1 : (byte) 0);

			// 修改會員
			this.memberRepository.save(member);

			memberDTO.setMember(member);
			memberDTO.setBirthDate(DateTool.dateToString(member.getMemBirth()));
			memberDTO.setInform(member.getMemInfo() == (byte) 1 ? "on" : null);

			return memberDTO;
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 修改"會員帳密"密碼
	 */
	@Override
	@Transactional
	public MemberDTO updatePassword(MemberDTO newDto, MemberDTO oldDto) {
		try {
			User user = oldDto.getUser();
			user.setPassword(BCrypt.getDecodePassword(newDto.getCkeckPwd()));

			this.userRepository.save(user);

			oldDto.setUser(user);

			return oldDto;
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 修改"會員"資料(含大頭貼路徑)
	 */
	@Override
	@Transactional
	public void updateMemberSticker(Member member) {
		try {
			this.memberRepository.save(member);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 我的活動 會員有參與的活動"signupAvt報名人員清單"(複合式查詢)
	 */
	@Override
	public List<SignupAvt> searchJoinActivity(String userNo, QueryActivityDTO queryActivityDTO) {
		try {
			String keyWord = queryActivityDTO.getKeyWord();
			String type = queryActivityDTO.getType();
			String status = queryActivityDTO.getStatus();

			QSignupAvt signupActivity = QSignupAvt.signupAvt;
			BooleanExpression whereSQL = signupActivity.signUser.eq(userNo);
			// 依關鍵字
			if (keyWord != null && !"".equals(keyWord.trim())) {
				whereSQL = whereSQL.and(signupActivity.activity.avtName.like("%" + keyWord + "%"));
			}
			// 依活動類型
			if (type != null && !"".equals(type.trim())) {
				whereSQL = whereSQL.and(signupActivity.activity.activityType.avtTypeNo.eq(Integer.valueOf(type)));
			}
			// 依活動狀態
			if (status != null && !"".equals(status.trim())) {
				if ("1".equals(status)) {
					status = "報名中";
				} else if ("2".equals(status)) {
					status = "已額滿";
				} else if ("3".equals(status)) {
					status = "已截止";
				} else if ("4".equals(status)) {
					status = "已結束";
				}
				whereSQL = whereSQL.and(signupActivity.activity.avtStat.eq(status));
			}

			List<SignupAvt> signupAvtQuerys = jpaQueryFactory.selectFrom(signupActivity).from(signupActivity)
					.where(whereSQL).orderBy(signupActivity.signTime.desc()).fetch();
			
			log.info("keyWord:{}, type:{}, status:{}", keyWord, type ,status);

			return signupAvtQuerys;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 會員參與的該項活動之所有"signupAvt報名人員清單"(個人加親友)
	 */
	@Override
	public List<SignupAvt> joinActivityData(String userNo, int theAvtNo) {
		try {
			QSignupAvt signupActivity = QSignupAvt.signupAvt;
			BooleanExpression whereSQL = signupActivity.member.memUser.eq(userNo)
					.and(signupActivity.activity.avtNo.eq(theAvtNo));

			List<SignupAvt> signupAvtQuerys = jpaQueryFactory.selectFrom(signupActivity).from(signupActivity)
					.where(whereSQL).orderBy(signupActivity.signUser.desc()).fetch();

			return signupAvtQuerys;
		} catch (Exception e) {
			throw e;
		}
	}


}
