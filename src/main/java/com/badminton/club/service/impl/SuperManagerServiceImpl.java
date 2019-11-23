package com.badminton.club.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.ActivityTypeRepository;
import com.badminton.club.dao.AuthorityRepository;
import com.badminton.club.dao.SystemRepository;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.QueryActivityTypeDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.ActivityType;
import com.badminton.club.entity.Authority;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.QActivityType;
import com.badminton.club.entity.QAuthority;
import com.badminton.club.entity.QMember;
import com.badminton.club.entity.QUser;
import com.badminton.club.entity.System;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.SuperManagerService;
import com.badminton.club.tools.DateTool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
/**
 * 活動最高管理(社團介紹編輯，活動類型及審核，管理員管理) 服務
 */
@Service("SuperManagerService")
public class SuperManagerServiceImpl implements SuperManagerService {

	private static final Logger log = LoggerFactory.getLogger(SuperManagerServiceImpl.class);

	private BasicService basicService;
	private AuthorityRepository authorityRepository;
	private ActivityTypeRepository activityTypeRepository;
	private SystemRepository systemRepository;
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	public SuperManagerServiceImpl(BasicService basicService, AuthorityRepository authorityRepository,
			ActivityTypeRepository activityTypeRepository, SystemRepository systemRepository,
			JPAQueryFactory jpaQueryFactory) {
		this.basicService = basicService;
		this.authorityRepository = authorityRepository;
		this.activityTypeRepository = activityTypeRepository;
		this.systemRepository = systemRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	/**
	 * 管理員管理(複合式查詢) 取得會員及搜尋條件
	 */
	@Override
	@Transactional
	public QueryMemberQualifyDTO findMemberAllData(QueryMemberQualifyDTO queryDTO) {
		try {
			String keyWord = queryDTO.getKeyWord();
			String status = queryDTO.getStatus();

			List<Member> members = new ArrayList<>();
			QMember theMember = QMember.member;

			// 依定要有會員資格
			QUser theUser = QUser.user;
			List<String> userIds = jpaQueryFactory.select(theUser.username).from(theUser)
					.where(theUser.enabled.eq((byte) 1)).fetch();

			BooleanExpression whereSQL = theMember.memUser.in(userIds);
			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = whereSQL.and(
						theMember.memUser.like("%" + keyWord + "%").or(theMember.memName.like("%" + keyWord + "%")));
			}
			// 依身份
			if (!StringUtils.isBlank(status)) {
				QAuthority theAuthority = QAuthority.authority;
				userIds = jpaQueryFactory.select(theAuthority.user.username).from(theAuthority).where(
						theAuthority.role.authority.eq("manager".equals(status) ? "ROLE_MANAGER" : "ROLE_MEMBER"))
						.fetch();
				whereSQL = whereSQL.and(theMember.memUser.in(userIds));
			}
			if (whereSQL == null) {
				members = jpaQueryFactory.selectFrom(theMember).from(theMember).orderBy(theMember.memTime.desc())
						.fetch();
			} else {
				members = jpaQueryFactory.selectFrom(theMember).from(theMember).where(whereSQL)
						.orderBy(theMember.memTime.desc()).fetch();
			}
			
			log.info("keyWord:{}, status:{}, results:{}", keyWord, status ,members==null ? null :members.size());


			List<MemberDTO> memberDTOs = new ArrayList<>();
			members.forEach(m -> {
				User user = this.findUser(m.getMemUser());
				MemberDTO dto = new MemberDTO();
				dto.setMember(m);
				dto.setUser(user);
				dto.setInform(this.basicService.getAllRoles(user).contains("ROLE_MANAGER") ? "on" : null);
				dto.setBirthDate(DateTool.dateToString(m.getMemBirth()));
				if (this.basicService.getAllRoles(user).contains("ROLE_SURPERMANAGER")) {
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
	 * 取得會員帳密
	 */
	public User findUser(String userNo) {
		User user = this.basicService.findUser(userNo);

		// Authority再查一次(新增或刪除完，直接查mapping不到)
		QAuthority theAuthority = QAuthority.authority;
		List<Authority> authorities = jpaQueryFactory.select(theAuthority).from(theAuthority)
				.where(theAuthority.user.username.eq(user.getUsername())).fetch();
		user.setAuthorities(authorities);

		return user;
	}


	/**
	 * 更新會員帳號權限
	 */
	@Override
	@Transactional
	public void updateMemberAuth(List<MemberDTO> memberDTOs) {
		try {
			memberDTOs.forEach(member -> {
				User user = this.findUser(member.getMember().getMemUser());
				// 會員(沒有超級管理員腳色)
				if (!this.basicService.getAllRoles(user).contains("ROLE_SURPERMANAGER")) {

					if ("on".equals(member.getInform())) {
						if (!this.basicService.getAllRoles(user).contains("ROLE_MANAGER")) {
							Authority authority = new Authority();
							authority.setUser(user);
							authority.setRole(this.basicService.findRole("ROLE_MANAGER"));
							this.authorityRepository.save(authority);
						}
					} else {
						if (this.basicService.getAllRoles(user).contains("ROLE_MANAGER")) {
							QAuthority theAuthority = QAuthority.authority;
							Authority authority = jpaQueryFactory
									.select(theAuthority).from(theAuthority).where(theAuthority.role.authority
											.eq("ROLE_MANAGER").and(theAuthority.user.username.eq(user.getUsername())))
									.fetchFirst();

							this.authorityRepository.delete(authority);
						}
					}
					this.authorityRepository.flush();
				}
			});
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 活動類型及審核(關鍵字查詢)
	 */
	@Override
	public QueryActivityTypeDTO findActivityType(QueryActivityTypeDTO queryDTO) {
		try {
			String keyWord = queryDTO.getKeyWord();

			QActivityType theActivityType = QActivityType.activityType;
			List<ActivityType> activityTypes = new ArrayList<>();

			BooleanExpression whereSQL = null;
			// 依關鍵字
			if (!StringUtils.isBlank(keyWord)) {
				whereSQL = theActivityType.avtTypeName.like("%" + keyWord + "%");
			}

			if (whereSQL == null) {
				activityTypes = jpaQueryFactory.selectFrom(theActivityType).from(theActivityType)
						.orderBy(theActivityType.avtTypeNo.asc()).fetch();
			} else {
				activityTypes = jpaQueryFactory.selectFrom(theActivityType).from(theActivityType).where(whereSQL)
						.orderBy(theActivityType.avtTypeNo.asc()).fetch();
			}

			queryDTO.setActivityTypes(activityTypes);

			return queryDTO;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得活動類型
	 */
	@Override
	public ActivityType getActivityType(int avtTypeNo) {
		try {
			QActivityType theActivityType = QActivityType.activityType;
			ActivityType activityType = jpaQueryFactory.select(theActivityType).from(theActivityType)
					.where(theActivityType.avtTypeNo.eq(avtTypeNo)).fetchFirst();
			return activityType;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 新增或修改活動類型
	 */
	@Override
	@Transactional
	public void saveOrUpdateActivityType(ActivityType activityType) {
		try {

			activityType.setAvtTypePre((byte) 1);
			this.activityTypeRepository.save(activityType);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 刪除活動類型
	 */
	@Override
	@Transactional
	public void deleteActivityType(int avtTypeNo) {
		try {

			this.activityTypeRepository.deleteById(avtTypeNo);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 取得系統設定
	 */
	@Override
	public System getSystem(String sysName) {
		try {

			System theSystem = this.systemRepository.findSystem(sysName);
			return theSystem;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 系統設定更新存檔
	 */
	@Override
	@Transactional
	public void saveSystem(System theSystem) {
		try {

			this.systemRepository.save(theSystem);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

}
