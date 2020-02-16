package com.badminton.club.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.QueryActivityCheckDTO;
import com.badminton.club.dto.QueryActivityHoldDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.service.ActivityService;
import com.badminton.club.service.ManagerService;
import com.badminton.club.tools.FileTool;
/**
 * 活動管理(活動審核，社員管理) 控制器
 */
@Controller
@RequestMapping("/manager")
public class ManagerController  extends BaseController{


	@Autowired
	private ActivityService activityService;

	@Autowired
	private ManagerService managerService;




	/**
	 * 活動審核
	 */
	@GetMapping("/activityReview")
	public String activityReview(Model theModel) {
		QueryActivityCheckDTO queryDTO=new QueryActivityCheckDTO("","","",1);
		try {
			//活動審核查詢，第1頁，1頁共7筆
			QueryActivityCheckDTO queryActivityCheckDTO = managerService
					.findActivityAllData(queryDTO, this.getUserId(),queryDTO.getPage(),7);
			this.showResult(queryActivityCheckDTO.getActivityDTOs(), theModel);			
			this.getPage(queryDTO.getPage(),7,managerService.searchCheckCount(queryDTO, this.getUserId()), theModel);
			
			//取得目前會員帳號(下拉式選單)			 
			this.getAllManager(theModel);

			theModel.addAttribute("queryDTO", queryActivityCheckDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "活動審核");
			

			return "manager/check";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/check";
		}
	}

	/**
	 * 活動審核 活動搜尋
	 */
	@GetMapping("/activityReview/search")
	public String activityReviewSearch(Model theModel,
			@ModelAttribute("queryDTO") @Valid QueryActivityCheckDTO queryDTO) {
		try {
			QueryActivityCheckDTO queryActivityCheckDTO = managerService.findActivityAllData(queryDTO,
					this.getUserId(),queryDTO.getPage(),7);
			this.showResult(queryActivityCheckDTO.getActivityDTOs(), theModel);
			this.getPage(queryDTO.getPage(),7,managerService.searchCheckCount(queryDTO, this.getUserId()), theModel);

			//取得目前會員帳號(下拉式選單)				 
			this.getAllManager(theModel);

			theModel.addAttribute("queryDTO", queryActivityCheckDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "活動審核");
			

			return "manager/check";
		} catch (Exception e) {
			this.activityReview(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/check";
		}
	}
	
	/**
	 * 活動審核 活動搜尋 匯出csv
	 */
	@GetMapping("/activityReview/search/csv")
	public String activityReviewSearchCsv(@RequestParam("keyWord") String keyWord, //
			@RequestParam("statusPass") String statusPass, //
			@RequestParam("holder") String holder, //
			Model theModel, //
			HttpServletRequest request, //
			HttpServletResponse response) {
		
		QueryActivityCheckDTO queryActivityCheckDTO = new QueryActivityCheckDTO();
		queryActivityCheckDTO.setKeyWord(keyWord);
		queryActivityCheckDTO.setStatusPass(statusPass);
		queryActivityCheckDTO.setHolder(holder);
		
		try {
			
			queryActivityCheckDTO = managerService.generateCheckCSV(queryActivityCheckDTO,
					this.getUserId());
			FileTool.downLoadFile(request, response, FileTool.resource_prefix("/images/manager/check"),"活動審核清冊.csv");
			this.activityReviewSearch(theModel, queryActivityCheckDTO);

		
			return "manager/check";
		} catch (Exception e) {
			this.activityReview(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/check";
		}
	}

	/**
	 * 活動審核 活動搜尋 批次更新
	 */
	@RequestMapping("/activityReview/search/modify")
	public String activitySearchModify(Model theModel,
			@ModelAttribute("queryDTO") @Valid QueryActivityCheckDTO queryDTO) {
		try {
			this.managerService.updateActivity(queryDTO.getActivityDTOs(), this.getUserId());
			this.showMessage(theModel, "更新成功", "notice");
			this.activityReviewSearch(theModel, queryDTO);
			

			return "manager/check";
		} catch (Exception e) {
			this.activityReviewSearch(theModel, queryDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/check";
		}
	}

	/**
	 * 活動審核 活動搜尋 詳細資料
	 */
	@GetMapping("/activityReview/search/detail")
	public String activitySearchDetail(@RequestParam("activityId") int avtNo, Model theModel) {
		try {			
			Activity theActivity = this.activityService.findActivityIncludeMessageBytimeDesc(avtNo);
			this.getActivityDetail(theActivity, theModel);

			theModel.addAttribute("check", 1);// 是否從活動審核入口進去(1:true,0;false)
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", new BreadDTO("活動審核", "/manager/activityReview"),
					theActivity.getAvtName());
			

			return "activity/avt-item";
		} catch (Exception e) {
			this.activityReview(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/check";
		}
	}

	/**
	 * 社員管理
	 */
	@GetMapping("/memberManagement")
	public String memberManagement(Model theModel) {
		QueryMemberQualifyDTO queryDTO=new QueryMemberQualifyDTO("","",1);
		try {
			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(queryDTO,queryDTO.getPage(),7);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);
			this.getPage(queryDTO.getPage(),7,managerService.searchMemberAllCount(queryDTO), theModel);

			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "社員管理");
			

			return "manager/member";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}

	/**
	 * 社員管理 社員搜尋
	 */
	@GetMapping("/memberManagement/search")
	public String memberSearch(Model theModel, @ModelAttribute("queryDTO") @Valid QueryMemberQualifyDTO queryDTO) {
		try {
			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(queryDTO,queryDTO.getPage(),7);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);
			this.getPage(queryDTO.getPage(),7,managerService.searchMemberAllCount(queryDTO), theModel);

			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "社員管理");
			

			return "manager/member";
		} catch (Exception e) {
			this.memberManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}

	/**
	 * 社員管理 社員搜尋 批次更新
	 */
	@RequestMapping("/memberManagement/search/modify")
	public String memberSearchModify(Model theModel,
			@ModelAttribute("queryDTO") @Valid QueryMemberQualifyDTO queryDTO) {
		try {
			this.managerService.updateMemberAuth(queryDTO.getMemberDTOs());
			this.showMessage(theModel, "更新成功", "notice");
			this.memberSearch(theModel, queryDTO);
			

			return "manager/member";
		} catch (Exception e) {
			this.memberSearch(theModel, queryDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}

	/**
	 * 社員管理 社員搜尋 匯出csv檔
	 */
	@GetMapping("/memberManagement/search/csv")
	public String memberSearchCsv(@RequestParam("keyWord") String keyWord, //
			@RequestParam("status") String status, //
			Model theModel, //
			HttpServletRequest request, //
			HttpServletResponse response) {

		QueryMemberQualifyDTO queryMemberQualifyDTO = new QueryMemberQualifyDTO();
		queryMemberQualifyDTO.setKeyWord(keyWord);
		queryMemberQualifyDTO.setStatus(status);

		try {

			queryMemberQualifyDTO = managerService.generateMemberCSV(queryMemberQualifyDTO);
			FileTool.downLoadFile(request, response, FileTool.resource_prefix("/images/manager/member"),"會員清冊.csv");

			this.memberSearch(theModel, queryMemberQualifyDTO);
			

			return "manager/member";
		} catch (Exception e) {
			this.memberSearch(theModel, queryMemberQualifyDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}
	
	/**
	 * 社員管理 社員搜尋 刪除會員
	 */
	@RequestMapping("/memberManagement/search/delete")
	public String memberSearchDelete(Model theModel,@RequestParam("userId") String userId,@RequestParam("keyWord") String keyWord, //
			@RequestParam("status") String status,@RequestParam("page") int page) {
		QueryMemberQualifyDTO queryDTO = new QueryMemberQualifyDTO();
		queryDTO.setKeyWord(keyWord);
		queryDTO.setStatus(status);
		queryDTO.setPage(page);
		try {
			this.managerService.deleteMember(userId);
			this.memberSearch(theModel, queryDTO);
			

			return "manager/member";
		} catch (Exception e) {
			this.memberSearch(theModel, queryDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}

}
