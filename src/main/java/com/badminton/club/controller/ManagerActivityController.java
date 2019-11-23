package com.badminton.club.controller;

import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.badminton.club.dto.ActivityDTO;
import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.FooterDTO;
import com.badminton.club.dto.QueryActivityHoldDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.AvtPreview;
import com.badminton.club.service.ActivityService;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerActivityService;
import com.badminton.club.service.ManagerService;
import com.badminton.club.tools.DateTool;
import com.badminton.club.tools.FileTool;

/**
 * 活動管理(我的活動管理) 控制器
 */
@Controller
@RequestMapping("/manager/activityManagement")
public class ManagerActivityController extends BaseController{

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ManagerActivityService managerActivityService;

	@Autowired
	private BasicService basicService;



	/**
	 * 我的活動管理 列表
	 */
	@GetMapping("")
	public String activityManagement(Model theModel) {
		try {
			QueryActivityHoldDTO queryActivityHoldDTO = managerActivityService
					.findAllActivityHold(new QueryActivityHoldDTO(), this.getUserId());
			this.showResult(queryActivityHoldDTO.getActivityDTOs(), theModel);

			theModel.addAttribute("queryDTO", queryActivityHoldDTO);

			// 活動類型(下拉式選單)
			theModel.addAttribute("activitieTypes", basicService.getAllActivityType());
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "我的活動管理");
			this.getFooter(theModel);

			return "manager/activity/all";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 列表查詢(依條件查詢)
	 */
	@GetMapping("/Search")
	public String activityManagementSearch(Model theModel,
			@ModelAttribute("queryDTO") @Valid QueryActivityHoldDTO queryDTO) {
		try {
			QueryActivityHoldDTO queryActivityHoldDTO = managerActivityService.findAllActivityHold(queryDTO,
					this.getUserId());
			this.showResult(queryActivityHoldDTO.getActivityDTOs(), theModel);

			theModel.addAttribute("queryDTO", queryActivityHoldDTO);

			// 活動類型(下拉式選單)
			theModel.addAttribute("activitieTypes", basicService.getAllActivityType());
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", "我的活動管理");
			this.getFooter(theModel);

			return "manager/activity/all";
		} catch (Exception e) {
			this.activityManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料)
	 */
	@GetMapping("/search/detail")
	public String activityManagementSearchDetail(@RequestParam("activityId") int avtNo, Model theModel) {
		try {
			Activity theActivity = this.activityService.findActivityIncludeMessageBytimeDesc(avtNo);
			this.getActivityDetail(theActivity, theModel);

			theModel.addAttribute("check", 0);// 是否從活動審核入口進去(1:true,0;false)
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", new BreadDTO("我的活動管理", "/manager/activityManagement"),
					theActivity.getAvtName());
			this.getFooter(theModel);

			return "activity/avt-item";
		} catch (Exception e) {
			this.activityManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 新增
	 */
	@RequestMapping("/new")
	public String activityManagementAdd(Model theModel) {
		try {
			// 刪除新增活動時，沒刪成功的暫存活動圖片
			this.deleteTempImg(theModel);

			// 活動類型(下拉式選單)
			theModel.addAttribute("activitieTypes", basicService.getAllActivityType());
			ActivityDTO activityDTO = new ActivityDTO();
			activityDTO.getOtherDatas().add("");
			activityDTO.setAddOrEdit("新增活動");
			theModel.addAttribute("activityDTO", activityDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", new BreadDTO("我的活動管理", "/manager/activityManagement"),
					activityDTO.getAddOrEdit());
			this.getFooter(theModel);

			return "manager/activity/edit";
		} catch (Exception e) {
			this.activityManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 修改
	 */
	@GetMapping("/modify")
	public String activityManagementUpdate(@RequestParam("activityId") int avtNo, //
			@RequestParam("edit") int edit, Model theModel) {
		try {
			// 活動類型(下拉式選單)
			theModel.addAttribute("activitieTypes", basicService.getAllActivityType());
			ActivityDTO activityDTO = this.managerActivityService.getActivityDTO(avtNo);
			if (edit == 1) {
				activityDTO.setAddOrEdit("草稿修改");
			} else {
				activityDTO.setAddOrEdit("修改活動");
			}
			theModel.addAttribute("activityDTO", activityDTO);
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", new BreadDTO("我的活動管理", "/manager/activityManagement"),
					activityDTO.getAddOrEdit());
			this.getFooter(theModel);

			return "manager/activity/edit";
		} catch (Exception e) {
			this.activityManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 新增/修改 下一步
	 */
	@RequestMapping("/editNext")
	public String activityManagementEditNext(@ModelAttribute("activityDTO") @Valid ActivityDTO activityDTO,
			Model theModel, ServletRequest request, ServletResponse response) {
		try {
			List<String> mistakes = this.verifyActivity(activityDTO);
			if (!CollectionUtils.isEmpty(mistakes)) {
				this.showMessage(theModel, mistakes, "error");
				// 活動類型(下拉式選單)
				theModel.addAttribute("activitieTypes", basicService.getAllActivityType());
				theModel.addAttribute("activityDTO", activityDTO);
				
				this.breadcrumbAndNavbar(theModel, "manager", "活動管理",
						new BreadDTO("我的活動管理", "/manager/activityManagement"), activityDTO.getAddOrEdit());
				this.getFooter(theModel);
				return "manager/activity/edit";
			}
			this.managerActivityService.getActivityAdvocateDTO(activityDTO, this.getUserId());

			// 【活動資料帶入 session】
			HttpSession session = this.getHttpSession(theModel, request, response);
			session.setAttribute("activityDTO", activityDTO);

			theModel.addAttribute("activityDTO", activityDTO);
			// 活動宣傳圖片數量
			theModel.addAttribute("advocate_count", activityDTO.getActivity().getAdvocates().size());
			this.breadcrumbAndNavbar(theModel, "manager", "活動管理", new BreadDTO("我的活動管理", "/manager/activityManagement"),
					activityDTO.getAddOrEdit(), "活動圖片");
			this.getFooter(theModel);

			return "manager/activity/edit_next";
		} catch (Exception e) {
			this.activityManagement(theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 活動資料驗證
	 * 
	 * @throws ParseException
	 */
	public List<String> verifyActivity(ActivityDTO activityDTO) {
		
		List<String> mistakes = new ArrayList<>();
		Date activityDateS = DateTool.getDate(activityDTO.getActivityDateS());
		Date activityDateE = DateTool.getDate(activityDTO.getActivityDateE());
		Date activityCutDate = DateTool.getDate(activityDTO.getAvtCutDate());
		

		if (activityDTO.getActivity().getActivityType().getAvtTypeNo() == 0) {
			mistakes.add("請輸入活動類型");
		}
		if (activityDateS.after(activityDateE)) {
			mistakes.add("活動起日不可大於活動訖日");
		}
		if (activityCutDate.after(activityDateE)) {
			mistakes.add("活動截止日不可大於活動訖日");
		}
		
		return mistakes;
	}

	// ======================================================================
	// ----------------------------活動圖片處理-------------------------------
	// ======================================================================

	/**
	 * 我的活動管理 新增/修改 下一步 活動主題圖片存檔
	 */
	@RequestMapping("/editNext/saveImg")
	public String activityManagementEditNextSaveImg(@RequestParam MultipartFile file, Model theModel,
			ServletRequest request, ServletResponse response) {

		// 【取得 session上一頁活動資料】
		HttpSession session = this.getHttpSession(theModel, request, response);
		ActivityDTO PreviousActivityDTO = (ActivityDTO) session.getAttribute("activityDTO");
		try {

			if (!file.getOriginalFilename().isEmpty()) {
				FileTool.modifyAndUpload(file, "/images/activity/", file.getOriginalFilename());
			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");
			}
			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			return "manager/activity/edit_next";
		} catch (Exception e) {
			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/edit_next";
		}

	}

	/**
	 * 我的活動管理 新增/修改 下一步 活動宣傳圖片存檔
	 */
	@RequestMapping("/editNext/saveAdv")
	public String activityManagementEditNextsaveAdv(@RequestParam MultipartFile file, Model theModel,
			ServletRequest request, ServletResponse response) {

		// 【取得 session上一頁活動資料】
		HttpSession session = this.getHttpSession(theModel, request, response);
		ActivityDTO PreviousActivityDTO = (ActivityDTO) session.getAttribute("activityDTO");
		try {
			// 圖片存檔
			if (!file.getOriginalFilename().isEmpty()) {
				String dir = "";
				if (PreviousActivityDTO.getActivity().getAvtNo() < 1) {
					dir = "avt_" + this.getUserId() + "/";
				} else {
					dir = "avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/";
				}

				FileTool.uploadDir("/images/activity/");
				FileTool.modifyAndUpload(file, "/images/activity/" + dir, file.getOriginalFilename());

			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");

			}

			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			return "manager/activity/edit_next";
		} catch (Exception e) {
			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/edit_next";
		}

	}

	/**
	 * 我的活動管理 新增/修改 下一步 活動宣傳圖片刪除
	 */
	@RequestMapping("/editNext/deleteImgAdv")
	public String activityManagementEditNextDeleteImgAdv(@RequestParam MultipartFile file, Model theModel,
			ServletRequest request, ServletResponse response) {

		// 【取得 session上一頁活動資料】
		HttpSession session = this.getHttpSession(theModel, request, response);
		ActivityDTO PreviousActivityDTO = (ActivityDTO) session.getAttribute("activityDTO");
		try {
			// 圖片刪除
			if (!file.getOriginalFilename().isEmpty()) {
				FileTool.deleteFile(file.getOriginalFilename());
				if(PreviousActivityDTO.getActivity().getAvtNo()>0) {
					FileTool.deleteDirectory("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/");
				}else {
					FileTool.deleteDirectory("/images/activity/avt_" + this.getUserId() + "/");
				}
				// 若db存在一併刪除
				this.managerActivityService.deleteAdvocate(file.getOriginalFilename());
			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");

			}

			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			return "manager/activity/edit_next";
		} catch (Exception e) {
			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/edit_next";
		}

	}

	/**
	 * 我的活動管理 新增/修改 下一步 活動宣傳圖片刪除(修改的情況)
	 */
	@RequestMapping("/editNext/deleteImgModify")
	@ResponseBody
	public String activityManagementEditNextDeleteImgModify(@RequestParam("fileName") String fileName, Model theModel, ServletRequest request,
			ServletResponse response) {

		// 【取得 session上一頁活動資料】
		HttpSession session = this.getHttpSession(theModel, request, response);
		ActivityDTO PreviousActivityDTO = (ActivityDTO) session.getAttribute("activityDTO");

		try {
			// 圖片刪除
			if (!StringUtils.isBlank(fileName)) {
				FileTool.deleteFile(fileName);
				if(PreviousActivityDTO.getActivity().getAvtNo()>0) {
					FileTool.deleteDirectory("/images/activity/avt_" + PreviousActivityDTO.getActivity().getAvtNo() + "/");
				}else {
					FileTool.deleteDirectory("/images/activity/avt_" + this.getUserId() + "/");
				}
				// 若db存在一併刪除
				this.managerActivityService.deleteAdvocate(fileName);
			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");

			}

			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			return "manager/activity/edit_next";

		} catch (Exception e) {
			this.activityManagementEditNext(PreviousActivityDTO, theModel, request, response);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/edit_next";
		}
	}

	// ======================================================================

	/**
	 * 我的活動管理 新增/修改 下一步 存檔
	 */
	@RequestMapping("/editNext/save")
	public String activityManagementEditNextSave(@ModelAttribute("activityDTO") @Valid ActivityDTO activityDTO,
			Model theModel, ServletRequest request, ServletResponse response) {
		try {
			// 【取得 session上一頁活動資料】
			HttpSession session = this.getHttpSession(theModel, request, response);
			ActivityDTO PreviousActivityDTO = (ActivityDTO) session.getAttribute("activityDTO");
			session.removeAttribute("activityDTO");

			int avtNo = this.managerActivityService.saveActivity(PreviousActivityDTO, activityDTO, this.getUserId());

			if (PreviousActivityDTO.getActivity().getAvtEdit() == (byte) 1) {
				return "redirect:/manager/activityManagement";
			} else {
				return "redirect:/manager/activityManagement/search/detail?activityId=" + avtNo;
			}

		} catch (Exception e) {
			this.activityManagementEditNext(activityDTO, theModel, request, response);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/edit_next";
		}
	}

	/**
	 * 我的活動管理 列表查詢 刪除活動
	 */
	@RequestMapping("/search/delete")
	public String activityManagementSearchDelete(@RequestParam("activityId") int avtNo, //
			@RequestParam("keyWord") String keyWord, //
			@RequestParam("type") String type, //
			@RequestParam("checkStatus") String checkStatus, //
			@RequestParam("signUpStatus") String signUpStatus, //
			Model theModel) {

		QueryActivityHoldDTO queryDTO = new QueryActivityHoldDTO();
		queryDTO.setKeyWord(keyWord);
		queryDTO.setType(type);
		queryDTO.setCheckStatus(checkStatus);
		queryDTO.setSignUpStatus(signUpStatus);

		try {

			this.managerActivityService.deleteActivity(avtNo);

			return "redirect:/manager/activityManagement/Search?keyWord=" + URLEncoder.encode(keyWord, "utf-8")
					+ "&type=" + type + "&checkStatus=" + checkStatus + "&signUpStatus=" + signUpStatus;

		} catch (Exception e) {
			this.activityManagementSearch(theModel, queryDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

}
