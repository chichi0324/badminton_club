package com.badminton.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.entity.System;
import com.badminton.club.service.BasicService;

/**
 * 社團介紹(關於我們,聯絡資訊,line QRCode) 控制器
 */
@Controller
@RequestMapping("/introduce")
public class introduceController extends BaseController{
	
	@Autowired
	private BasicService basicService;

	/**
	 * 關於我們
	 */
	@GetMapping("/aboutUs")
	public String aboutUs(Model theModel) {
		try {
			System theSystem = this.basicService.getIntroduceSystem("關於我們");
			theModel.addAttribute("theSystem", theSystem);

			this.breadcrumbAndNavbar(theModel, "introduce", "社團介紹", "關於我們");
			this.getAllActivityShowLightBox(theModel);
			
			return "introduce/aboutUs";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "introduce/aboutUs";
		}
	}
	
	/**
	 * 聯絡資訊
	 */
	@GetMapping("/contact")
	public String contact(Model theModel) {
		try {
			System theSystem = this.basicService.getIntroduceSystem("聯絡資訊");
			theModel.addAttribute("theSystem", theSystem);

			this.breadcrumbAndNavbar(theModel, "introduce", "社團介紹", "聯絡資訊");
			this.getAllActivityShowLightBox(theModel);
			
			return "introduce/contact";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "introduce/contact";
		}
	}
	
	/**
	 * line QRCode
	 */
	@GetMapping("/qrCode")
	public String qrCode(Model theModel) {
		try {
			System theSystem = this.basicService.getIntroduceSystem("QrCode");
			theModel.addAttribute("theSystem", theSystem);

			this.breadcrumbAndNavbar(theModel, "introduce", "社團介紹", "line QRCode");
			this.getAllActivityShowLightBox(theModel);
			
			return "introduce/qrCode";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "introduce/qrCode";
		}
	}
}
