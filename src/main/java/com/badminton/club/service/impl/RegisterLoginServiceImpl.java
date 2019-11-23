package com.badminton.club.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badminton.club.dao.AuthorityRepository;
import com.badminton.club.dao.MemberRepository;
import com.badminton.club.dao.RoleRepository;
import com.badminton.club.dao.SystemRepository;
import com.badminton.club.dao.UserRepository;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.entity.Authority;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.Role;
import com.badminton.club.entity.System;
import com.badminton.club.entity.User;
import com.badminton.club.service.RegisterLoginService;
import com.badminton.club.tools.BCrypt;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.MailTool;
/**
 * 登入及註冊 服務
 */
@Service("RegisterLoginService")
public class RegisterLoginServiceImpl implements RegisterLoginService {
	
	private static final Logger log = LoggerFactory.getLogger(RegisterLoginServiceImpl.class);

	private UserRepository userRepository;
	private MemberRepository memberRepository;
	private AuthorityRepository authorityRepository;
	private RoleRepository roleRepository;
	private SystemRepository systemRepository;

	@Autowired
	public RegisterLoginServiceImpl(UserRepository userRepository, MemberRepository memberRepository,
			AuthorityRepository authorityRepository, RoleRepository roleRepository, SystemRepository systemRepository) {
		this.userRepository = userRepository;
		this.memberRepository = memberRepository;
		this.authorityRepository = authorityRepository;
		this.roleRepository = roleRepository;
		this.systemRepository = systemRepository;
	}


	/**
	 * 存檔註冊會員資料
	 */
	@Override
	@Transactional
	public void registerAndSave(MemberDTO dto, String imagePath){
		try {
			dto.getUser().setEnabled((byte) 0);
			dto.getUser().setPassword(BCrypt.getDecodePassword(dto.getUser().getPassword()));

			dto.getMember().setMemUser(dto.getUser().getUsername());
			dto.getMember().setMemInfo("on".equals(dto.getInform()) ? (byte) 1 : (byte) 0);
			dto.getMember().setMemBirth(DateTool.getDate(dto.getBirthDate()));
			if (imagePath != null) {
				dto.getMember().setMemImg(imagePath);
			}

			// 新增會員
			this.memberRepository.save(dto.getMember());
			// 新增使用者
			this.userRepository.save(dto.getUser());

			Authority authority = new Authority();
			authority.setUser(dto.getUser());

			Optional<Role> result = this.roleRepository.findById("ROLE_MEMBER"); // 搜尋[會員權限]
			Role role = null;
			if (result.isPresent()) {
				role = result.get();
			}
			authority.setRole(role);

			// 新增權限
			this.authorityRepository.save(authority);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * 忘記密碼(寄email通知)
	 */
	@Override
	public void sendMailPassword(User user) {
		try {
			String newPassword=this.getGeneratePassword();
			user.setPassword(BCrypt.getDecodePassword(newPassword));
			this.userRepository.save(user);
			
			Optional<Member> memberResult = this.memberRepository.findById(user.getUsername());
			Member member = memberResult.isPresent() ? memberResult.get() : null;

			System systemMail = this.systemRepository.findSystem("系統信箱");

			MailTool.transferPasseord(newPassword, member, systemMail.getSysData(), systemMail.getSysCon());
		
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 產生亂數密碼
	 * (第2及第4為小寫英文，其他為數字，共6碼)
	 */
	public String getGeneratePassword() {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < 6; i++) {	 
	      if (i == 1 || i==3) { // 放小寫英文
	        sb.append(((char) ((Math.random() * 26) + 97)));
	      } else {// 放數字
	        sb.append((int) (Math.random() * 10));
	      }
	    }
		return sb.toString();	
	}

}
