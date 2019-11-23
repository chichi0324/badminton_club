package com.badminton.club.controller;

import java.net.URLEncoder;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.FooterDTO;
import com.badminton.club.dto.QueryActivityTypeDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.System;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.SuperManagerService;
import com.badminton.club.tools.FileTool;
/**
 * 活動最高管理(社團介紹編輯，活動類型及審核，管理員管理) 控制器
 */
@Controller
@RequestMapping("/superManager")
public class SuperManagerController extends BaseController{


	@Autowired
	private SuperManagerService superManagerService;
	
	
	/**
	 * 社團介紹編輯 關於我們
	 */
	@GetMapping("/clubEdit/aboutUs")
	public String clubEditAboutUs(Model theModel) {
		try {
			System theSystem = this.superManagerService.getSystem("關於我們");
			theModel.addAttribute("theSystem", theSystem);

			this.fuctionList(theModel, "關於我們", new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"),
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"),
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "關於我們");
            this.getFooter(theModel);
			
			return "super_manager/introduce/aboutUs";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/aboutUs";
		}
	}
	
	/**
	 * 社團介紹編輯 關於我們 存檔
	 */
	@RequestMapping("/clubEdit/aboutUs/save")
	public String clubEditAboutUsSave(Model theModel, @ModelAttribute("theSystem") @Valid System theSystem) {
		try {
			
			this.superManagerService.saveSystem(theSystem);
			this.showMessage(theModel, "存檔成功", "notice");
			
			theModel.addAttribute("theSystem", theSystem);			

			this.fuctionList(theModel, "關於我們", new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"),
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"),
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "關於我們");
			this.getFooter(theModel);
			
			return "super_manager/introduce/aboutUs";
		} catch (Exception e) {
			this.clubEditAboutUs(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/aboutUs";
		}
	}

	/**
	 * 社團介紹編輯 聯絡資訊
	 */
	@GetMapping("/clubEdit/connect")
	public String clubEditConnect(Model theModel) {
		try {
			
			System theSystem = this.superManagerService.getSystem("聯絡資訊");
			theModel.addAttribute("theSystem", theSystem);
			
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"), "聯絡資訊",
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"),
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "聯絡資訊");
			this.getFooter(theModel);
			
			return "super_manager/introduce/contact";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/contact";
		}
	}
	
	/**
	 * 社團介紹編輯 聯絡資訊 存檔
	 */
	@RequestMapping("/clubEdit/connect/save")
	public String clubEditConnectSave(Model theModel, @ModelAttribute("theSystem") @Valid System theSystem) {
		try {
			
			this.superManagerService.saveSystem(theSystem);
			this.showMessage(theModel, "存檔成功", "notice");
			
			theModel.addAttribute("theSystem", theSystem);
			
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"), "聯絡資訊",
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"),
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "聯絡資訊");
			this.getFooter(theModel);
			
			return "super_manager/introduce/contact";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/contact";
		}
	}

	/**
	 * 社團介紹編輯 系統電子信箱設定
	 */
	@GetMapping("/clubEdit/email")
	public String clubEditEmail(Model theModel) {
		try {
			
			System theSystem = this.superManagerService.getSystem("系統信箱");
			theModel.addAttribute("theSystem", theSystem);
			
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"),
					new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"), "系統信箱設定",
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "系統信箱設定");
			this.getFooter(theModel);
			
			return "super_manager/introduce/email";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/email";
		}
	}
	
	/**
	 * 社團介紹編輯 系統電子信箱設定 存檔
	 */
	@RequestMapping("/clubEdit/email/save")
	public String clubEditEmailSave(Model theModel, @ModelAttribute("theSystem") @Valid System theSystem) {
		try {
			
			this.superManagerService.saveSystem(theSystem);
			this.showMessage(theModel, "存檔成功", "notice");
			
			theModel.addAttribute("theSystem", theSystem);
			
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"),
					new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"), "系統信箱設定",
					new BreadDTO("line QRCode", "/superManager/clubEdit/qRCode"));
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "系統信箱設定");
			this.getFooter(theModel);
			
			return "super_manager/introduce/email";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/email";
		}
	}

	/**
	 * 社團介紹編輯 line QRCode
	 */
	@GetMapping("/clubEdit/qRCode")
	public String clubEditQRCode(Model theModel) {
		try {
			
			System theSystem = this.superManagerService.getSystem("QrCode");
			theModel.addAttribute("theSystem", theSystem);
			
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"),
					new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"),
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"), "line QRCode");
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "line QRCode");
			this.getFooter(theModel);
			
			return "super_manager/introduce/qrCode";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/introduce/qrCode";
		}
	}
	
	/**
	 * 社團介紹編輯 line QRCode 存檔
	 */
	@RequestMapping("/clubEdit/qRCode/save")
	public String clubEditQRCodeSave(@RequestParam MultipartFile file, Model theModel) {
		try {
			this.fuctionList(theModel, new BreadDTO("關於我們", "/superManager/clubEdit/aboutUs"),
					new BreadDTO("聯絡資訊", "/superManager/clubEdit/connect"),
					new BreadDTO("系統信箱設定", "/superManager/clubEdit/email"), "line QRCode");
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "社團介紹編輯", "line QRCode");
			this.getFooter(theModel);
			
			// 帶入系統資料(QRCode)
			System theSystem = this.superManagerService.getSystem("QrCode");			

			if (!file.getOriginalFilename().isEmpty()) {

				if (theSystem.getSysData()== null) {
					FileTool.upload(file, "/images/");
					theSystem.setSysData("/images/" + file.getOriginalFilename());
					this.superManagerService.saveSystem(theSystem);				
				} else {
					FileTool.modifyAndUpload(file, "/images/",theSystem.getSysData());
				}
				theModel.addAttribute("theSystem", theSystem);

			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");
				this.clubEditQRCode(theModel);
				return "super_manager/introduce/qrCode";
			}

		} catch (Exception e) {
			this.clubEditQRCode(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
		}
		return "super_manager/introduce/qrCode";

	}


	/**
	 * 活動類型及審核
	 */
	@GetMapping("/activityType")
	public String activityType(Model theModel) {
		try {
			QueryActivityTypeDTO queryActivityTypeDTO = superManagerService
					.findActivityType(new QueryActivityTypeDTO());
			this.showResult(queryActivityTypeDTO.getActivityTypes(), theModel);

			theModel.addAttribute("queryDTO", queryActivityTypeDTO);
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "活動類型及審核");
			this.getFooter(theModel);
			
			return "super_manager/activity_type";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/activity_type";
		}
	}

	/**
	 * 活動類型及審核 搜尋
	 */
	@GetMapping("/activityType/search")
	public String activityTypeSearch(Model theModel, @ModelAttribute("queryDTO") @Valid QueryActivityTypeDTO queryDTO) {
		try {
			QueryActivityTypeDTO queryActivityTypeDTO = superManagerService.findActivityType(queryDTO);
			this.showResult(queryActivityTypeDTO.getActivityTypes(), theModel);

			theModel.addAttribute("queryDTO", queryActivityTypeDTO);
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "活動類型及審核");
			this.getFooter(theModel);
			
			return "super_manager/activity_type";
		} catch (Exception e) {
			this.activityType(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/activity_type";
		}
	}

	/**
	 * 活動類型及審核 新增或修改
	 */
	@RequestMapping("/activityType/modify")
	public String activityTypeModify(@RequestParam("typeId") int typeId, @RequestParam("keyWord") String keyWord,
			Model theModel) {

		QueryActivityTypeDTO queryActivityTypeDTO = new QueryActivityTypeDTO();
		queryActivityTypeDTO.setKeyWord(keyWord);

		try {

			if (typeId == -1) {
				queryActivityTypeDTO.setAddOrUpdate("新增");
			} else {
				queryActivityTypeDTO.setAddOrUpdate("修改");
				queryActivityTypeDTO.setActivityType(this.superManagerService.getActivityType(typeId));
			}

			theModel.addAttribute("queryDTO", queryActivityTypeDTO);
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理",
					new BreadDTO("活動類型及審核", "/superManager/activityType"), queryActivityTypeDTO.getAddOrUpdate());
			this.getFooter(theModel);
			
			return "super_manager/activity_type_modify";

		} catch (Exception e) {
			this.activityTypeSearch(theModel, queryActivityTypeDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/activity_type";
		}
	}

	/**
	 * 活動類型及審核 新增或修改 存檔
	 */
	@RequestMapping("/activityType/modify/save")
	public String activityTypeModifySave(Model theModel,
			@ModelAttribute("queryDTO") @Valid QueryActivityTypeDTO queryDTO) {
		try {

			this.superManagerService.saveOrUpdateActivityType(queryDTO.getActivityType());

			return "redirect:/superManager/activityType/search?keyWord="+URLEncoder.encode(queryDTO.getKeyWord(), "utf-8");

			
		} catch (Exception e) {
			if ("新增".equals(queryDTO.getAddOrUpdate())) {
				this.activityTypeModify(-1, queryDTO.getKeyWord(), theModel);
			} else {
				this.activityTypeModify(queryDTO.getActivityType().getAvtTypeNo(), queryDTO.getKeyWord(), theModel);
			}
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/activity_type";
		}
	}

	/**
	 * 活動類型及審核 刪除
	 */
	@RequestMapping("/activityType/delete")
	public String activityTypeDelete(@RequestParam("typeId") int typeId, @RequestParam("keyWord") String keyWord,
			Model theModel) {

		QueryActivityTypeDTO queryActivityTypeDTO = new QueryActivityTypeDTO();
		queryActivityTypeDTO.setKeyWord(keyWord);

		try {

			this.superManagerService.deleteActivityType(typeId);

			return "redirect:/superManager/activityType/search?keyWord="+URLEncoder.encode(keyWord, "utf-8");

		} catch (Exception e) {
			this.activityTypeSearch(theModel, queryActivityTypeDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/activity_type";
		}
	}

	/**
	 * 管理員管理
	 */
	@GetMapping("/adminManagement")
	public String adminManagement(Model theModel) {

		try {
			QueryMemberQualifyDTO queryMemberQualifyDTO = superManagerService
					.findMemberAllData(new QueryMemberQualifyDTO());
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);
			this.getFooter(theModel);
			
			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			this.breadcrumbAndNavbar(theModel, "superManager", "活動最高管理", "管理員管理");

			return "super_manager/qualification";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/qualification";
		}
	}

	/**
	 * 管理員管理 管理員搜尋
	 */
	@GetMapping("/adminManagement/search")
	public String adminSearch(Model theModel, @ModelAttribute("queryDTO") @Valid QueryMemberQualifyDTO queryDTO) {
		try {
			QueryMemberQualifyDTO queryMemberQualifyDTO = superManagerService.findMemberAllData(queryDTO);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);

			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			this.breadcrumbAndNavbar(theModel, "superManager", "活動管理", "社員管理");
			this.getFooter(theModel);
			
			return "super_manager/qualification";
		} catch (Exception e) {
			this.adminManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/qualification";
		}
	}

	/**
	 * 管理員管理 管理員搜尋 批次更新
	 */
	@RequestMapping("/adminManagement/search/modify")
	public String adminSearchModify(Model theModel, @ModelAttribute("queryDTO") @Valid QueryMemberQualifyDTO queryDTO) {
		try {
			this.superManagerService.updateMemberAuth(queryDTO.getMemberDTOs());
			this.showMessage(theModel, "更新成功", "notice");
			this.adminSearch(theModel, queryDTO);
			this.getFooter(theModel);
			
			return "super_manager/qualification";
		} catch (Exception e) {
			this.adminSearch(theModel, queryDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "super_manager/qualification";
		}
	}

}
