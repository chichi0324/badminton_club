package com.badminton.club.service.impl;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.ActivityRepository;
import com.badminton.club.dao.ActivityTypeRepository;
import com.badminton.club.dao.MemberRepository;
import com.badminton.club.dao.RoleRepository;
import com.badminton.club.dao.SignupAvtRepository;
import com.badminton.club.dao.SystemRepository;
import com.badminton.club.dao.UserRepository;
import com.badminton.club.dto.FooterDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.ActivityType;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.Role;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.System;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;

/**
 * 共用(活動，會員，管理員，超級管理員) 服務
 */
@Service("BasicService")
public class BasicServiceImpl implements BasicService {
	
	private static final Logger log = LoggerFactory.getLogger(BasicServiceImpl.class);
	
	private SystemRepository systemRepository;
	private ActivityRepository activityRepository;
	private ActivityTypeRepository activityTypeRepository;
	private MemberRepository memberRepository;
	private UserRepository userRepository;
	private SignupAvtRepository signupAvtRepository;
	private RoleRepository roleRepository;
	
	@Autowired
	public BasicServiceImpl(SystemRepository systemRepository, ActivityRepository activityRepository,
			ActivityTypeRepository activityTypeRepository, MemberRepository memberRepository,
			UserRepository userRepository, SignupAvtRepository signupAvtRepository, RoleRepository roleRepository) {
		this.systemRepository = systemRepository;
		this.activityRepository = activityRepository;
		this.activityTypeRepository = activityTypeRepository;
		this.memberRepository = memberRepository;
		this.userRepository = userRepository;
		this.signupAvtRepository = signupAvtRepository;
		this.roleRepository = roleRepository;
	}
	
	/**
	 * 底下footer資料
	 */
	@Override
	public FooterDTO getFooter() {
		System systemAboutUs=this.systemRepository.findSystem("關於我們");
		System systemContact=this.systemRepository.findSystem("聯絡資訊");
		System systemQrCode=this.systemRepository.findSystem("QrCode");
		
		FooterDTO footerDTO=new FooterDTO();
		footerDTO.setAboutUs(systemAboutUs.getSysCon());
		footerDTO.setContact(systemContact.getSysCon());
		footerDTO.setLineQrCode(systemQrCode.getSysData());
		return footerDTO;
	}


	/**
	 * 取得所有活動類型
	 */
	@Override
	public List<ActivityType> getAllActivityType() {
		List<ActivityType> activityTypes=this.activityTypeRepository.findAll();
		return activityTypes;
	}
	
	/**
	 * 幻燈片顯示所有活動 (審核通過，非草稿，活動狀態為'報名中') 照活動起始日排序(新-->舊)
	 */
	@Override
	public List<Activity> showLightBox() {
		try {
			return this.activityRepository.showActivityLightBox();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 取得"會員帳密"
	 */
	@Override
	public User findUser(String userNo) {
		try {
			Optional<User> userResult = this.userRepository.findById(userNo);
			User user = userResult.isPresent() ? userResult.get() : null;
			return user;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 取得"會員"資料
	 */
	@Override
	public Member findMember(String userNo) {
		try {
			Optional<Member> memberResult = this.memberRepository.findById(userNo);
			Member member = memberResult.isPresent() ? memberResult.get() : null;
			return member;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得"活動"資料
	 */
	@Override
	public Activity findActivity(int avtNo) {
		try {
			Optional<Activity> activityResult = this.activityRepository.findById(avtNo);
			Activity activity = activityResult.isPresent() ? activityResult.get() : null;
			return activity;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 查詢報名資料
	 */
	@Override
	public SignupAvt findSignupAvt(int signNo) {
		try {
			Optional<SignupAvt> signupAvtResult = signupAvtRepository.findById(signNo);
			SignupAvt signupAvt = signupAvtResult.isPresent() ? signupAvtResult.get() : null;
			return signupAvt;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 取得該會員擁有的所有腳色
	 */
	@Override
	public List<String> getAllRoles(User user) {

		List<String> roles = new ArrayList<>();
		user.getAuthorities().forEach(auth -> {
			roles.add(auth.getRole().getAuthority());
		});

		return roles;
	}
	
	/**
	 * 取得角色
	 */
	@Override
	public Role findRole(String authority) {
		Role role = this.roleRepository.findRolefromAuthority(authority);
		return role;
	}
	
	/**
	 * 是否有權限編輯活動報名人員清單(只有活動舉辦人和超級管理員可以)
	 */
	@Override
	public boolean isEditSignAvtEnable(String userName, int avtNo) {
		List<String> roles=this.getAllRoles(this.findUser(userName));
		Activity activity=this.findActivity(avtNo);
		
		if(roles.contains("ROLE_SURPERMANAGER") || userName.equals(activity.getMember().getMemUser())){
			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * 驗證活動限制名額是否額滿，若額滿更新活動狀態(改為"已額滿")，或其他狀態．
	 */
	@Override
	@Transactional
	public void updateActivityStatus(int theId) {
		try {
			Activity activity = this.findActivity(theId);
			int sum=signupAvtRepository.getActivitySum(theId);
			Date nowDate=new Date();
			// 活動名額上限 小於或等於 報名人員清單總數，活動狀態更改為"已額滿"
			if (activity.getAvtUpp() <= sum) {
				activity.setAvtStat("已額滿");
				
			}else if(activity.getAvtUpp() > sum && nowDate.before(activity.getAvtCutDate()) && nowDate.before(activity.getAvtDateS())){
				activity.setAvtStat("報名中");
			
			}else if(nowDate.after(activity.getAvtCutDate()) && nowDate.before(activity.getAvtDateS())){
				activity.setAvtStat("已截止");
			
			}else if(nowDate.after(activity.getAvtDateS())){
				activity.setAvtStat("已結束");
			}
			activityRepository.save(activity);
			
			log.info("Activity Status:{}, ", activity.getAvtStat());

		} catch (Exception e) {
			throw e;
		}
	}

}
