package com.badminton.club.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.QueryActivityCheckDTO;
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
		try {
			QueryActivityCheckDTO queryActivityCheckDTO = managerService
					.findActivityAllData(new QueryActivityCheckDTO(), this.getUserId());
			this.showResult(queryActivityCheckDTO.getActivityDTOs(), theModel);

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
					this.getUserId());
			this.showResult(queryActivityCheckDTO.getActivityDTOs(), theModel);

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
		try {
			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(new QueryMemberQualifyDTO());
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);

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
			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(queryDTO);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);

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

			queryMemberQualifyDTO = managerService.generateCSV(queryMemberQualifyDTO);
			FileTool.downLoadFile(request, response, FileTool.resource_prefix("/images/manager/member"),"會員清冊.csv");

			this.memberSearch(theModel, queryMemberQualifyDTO);
			

			return "manager/member";
		} catch (Exception e) {
			this.memberSearch(theModel, queryMemberQualifyDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/member";
		}
	}

}
