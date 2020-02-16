package com.badminton.club.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.badminton.club.dto.MemberDTO;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.RegisterLoginService;
/**
 * 登入 控制器
 */
@Controller
public class LoginController extends BaseController{
	
	
	@Autowired
	private RegisterLoginService registerService;
	
	@Autowired
	private BasicService basicService;

	/**
	 * 登入畫面
	 */
	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {
		return "login/login";
	}

	
	/**
	 * 註冊畫面
	 */
	@GetMapping("/register")
	public String showRegister(Model theModel) {
		
		theModel.addAttribute("registerDTO", new MemberDTO());
		
		return "login/register_data";
	}
	
	/**
	 * 忘記密碼畫面
	 */
	@GetMapping("/forgotPage")
	public String forgotPassword(Model theModel) {
				
		return "login/forgot_psd";
	}
	
	/**
	 * 忘記密碼及傳送新密碼
	 */
	@RequestMapping("/getNewPassword")
	public String getNewPassword(@RequestParam("user") String user, Model theModel) {
		try {	
			
			User theUser=basicService.findUser(user);
			List<String> mistakes =this.verify(user,theUser);
			
			if(CollectionUtils.isEmpty(mistakes)){
//				registerService.sendMailPassword(theUser);
				registerService.getIdCardForPassword(theUser);
				this.showMessage(theModel, "密碼為您的身分證後六碼","notice");
				return "login/login";
			}else{			
				this.showMessage(theModel,mistakes,"error");
				return "login/forgot_psd";
			}
			
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(),"error");
			return "login/forgot_psd";
		}
	}
	

	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "login/access-denied";
	}
	

	/**
	 * 帳號驗證
	 */
	public List<String> verify(String userNo,User theUser){
		
		List<String> mistakes = new ArrayList<>();
		
		if(StringUtils.isBlank(userNo)){
			mistakes.add("請輸入帳號");
		}else{
			
			if(theUser==null){
				mistakes.add("無此帳號");
			}
		}

		return mistakes;
	}

}
