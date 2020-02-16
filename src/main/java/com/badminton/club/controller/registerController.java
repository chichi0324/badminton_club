package com.badminton.club.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.badminton.club.dto.MemberDTO;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.RegisterLoginService;
import com.badminton.club.tools.FileTool;
/**
 * 註冊
 */
@Controller
@RequestMapping(value = "/signIn", method = RequestMethod.POST)
public class registerController extends BaseController{
	

	@Autowired
	private RegisterLoginService registerService;
	
	@Autowired
	private BasicService basicService;

    //上傳圖片(大頭貼)路徑
	private String imagePath;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	/**
	 * 註冊驗證  及
	 * 帶入下一頁(大頭貼)
	 */
	@RequestMapping("/register_next")
	public String register(Model theModel, @ModelAttribute("registerDTO") @Valid MemberDTO dto) {
		try {

			List<String> mistakes=this.verify(dto);

			if (CollectionUtils.isEmpty(mistakes)) {
				theModel.addAttribute("registerDTO", dto);
				return "login/register_image";
			} else {
				this.showMessage(theModel, mistakes,"error");
				return "login/register_data";
			}

		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(),"error");
			return "login/register_data";
		}
	}

	/**
	 * 上傳圖片
	 */
	@RequestMapping("/uploadPicture")
	public String uploadPicture(@RequestParam MultipartFile file, Model theModel) throws IOException {
		try {
			if (!file.getOriginalFilename().isEmpty()) {
				FileTool.upload(file,"/images/member/");
				this.setImagePath("/images/member/" + file.getOriginalFilename());
			} else {
				this.showMessage(theModel, "上傳圖片失敗","error");
				return "login/register_image";
			}

			return "login/register_image";

		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(),"error");
			return "login/register_image";
		}
	}

	/**
	 * 註冊及存檔會員資料
	 */
	@RequestMapping("/register_Success")
	public String registerSuccess(Model theModel, @ModelAttribute("registerDTO") @Valid MemberDTO dto)
			throws IOException {
		try {
			User user = basicService.findUser(dto.getUser().getUsername());
			if (user == null) {
				registerService.registerAndSave(dto, this.getImagePath());
				this.showMessage(theModel, "您已註冊成功，待管理員授權後尚可登入。","notice");
			}else{
				return "redirect:/showMyLoginPage";
			}

			
			return "login/login";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(),"error");
			return "login/register_image";
		}
	}
	
	/**
	 * 註冊資料驗證
	 */
	public List<String> verify(MemberDTO dto){
		
		List<String> mistakes = new ArrayList<>();
		// 帳號
		if (StringUtils.isBlank(dto.getUser().getUsername())) {
			mistakes.add("請輸入帳號");
		} else {
			if (!(dto.getUser().getUsername().trim().length() >= 6 && dto.getUser().getUsername().trim().length()<=10)) {
				mistakes.add("帳號需為6~10碼");
			}
			if ((dto.getUser().getUsername().trim().length() >= 6 && dto.getUser().getUsername().trim().length()<=10) && 
					!dto.getUser().getUsername().trim().matches("[a-zA-Z0-9]{6,10}")) {
				mistakes.add("帳號必須包含英文或數字");
			}
			User user = basicService.findUser(dto.getUser().getUsername());
			if (user != null) {
				mistakes.add("此帳號已經使用過了");
			}

		}
		// 密碼
		if (StringUtils.isBlank(dto.getUser().getPassword())) {
			mistakes.add("請輸入密碼");
		} else {
			if (!(dto.getUser().getPassword().trim().length() >= 6 && dto.getUser().getPassword().trim().length()<=10)) {
				mistakes.add("密碼需為6~10碼");
			}
			if ((dto.getUser().getPassword().trim().length() >= 6 && dto.getUser().getPassword().trim().length()<=10) && 
					!dto.getUser().getPassword().trim().matches("[a-zA-Z0-9]{6,10}")) {
				mistakes.add("密碼必須包含英文或數字");
			}
		}
		// 確認密碼
		if (StringUtils.isBlank(dto.getCkeckPwd())) {
			mistakes.add("請輸入確認密碼");
		} else {
			if (!(dto.getCkeckPwd().trim().length() >= 6 && dto.getCkeckPwd().trim().length()<=10)) {
				mistakes.add("確認密碼需為6~10碼");
			}
			if ((dto.getCkeckPwd().trim().length() >= 6 && dto.getCkeckPwd().trim().length()<=10) && 
					!dto.getCkeckPwd().equals(dto.getUser().getPassword())) {
				mistakes.add("密碼和確認密碼不一致");
			}
			if (!dto.getCkeckPwd().trim().matches("[a-zA-Z0-9]{6,10}")) {
				mistakes.add("確認密碼必須包含英文和數字");
			}
		}
		// 確認姓名
		if (StringUtils.isBlank(dto.getMember().getMemName())) {
			mistakes.add("請輸入姓名");
		}
		// 身份證字號
		if (StringUtils.isBlank(dto.getMember().getMemIdn())) {
			mistakes.add("請輸入身份證字號");
		}else{
			if(dto.getMember().getMemIdn().length()!=10){
				mistakes.add("身份證字號為10碼");
			}
		}
		// 手機號碼
		if (StringUtils.isBlank(dto.getMember().getMemPhone())) {
			mistakes.add("請輸入手機號碼");
		}
		// 電子信箱
		if (StringUtils.isBlank(dto.getMember().getMemMail())) {
			mistakes.add("請輸入電子信箱");
		}
		// 地址
		if (StringUtils.isBlank(dto.getMember().getMemAddr())) {
			mistakes.add("請輸入地址");
		}
		return mistakes;
	}

}
