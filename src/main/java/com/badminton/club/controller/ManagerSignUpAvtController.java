package com.badminton.club.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.QueryActivitySignDTO;
import com.badminton.club.dto.QueryMemberQualifyDTO;
import com.badminton.club.dto.SignAvtDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.entity.User;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerService;
import com.badminton.club.service.ManagerSignUpAvtService;
import com.badminton.club.tools.FileTool;
/**
 * 活動管理(我的活動管理-->報名人員清冊部分) 控制器
 */
@Controller
@RequestMapping("/manager/activityManagement/search/detail/signAvtData")
public class ManagerSignUpAvtController  extends BaseController{

	@Autowired
	private ManagerSignUpAvtService managerSignUpAvtService;
	
	@Autowired
	private ManagerService managerService;

	@Autowired
	private BasicService basicService;
	

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊
	 */
	@RequestMapping("")
	public String activityManagementDetailSidnUpData(@RequestParam("activityId") int avtNo,
			@RequestParam("check") int check, Model theModel) {
		try {
			
			//是否有權限編輯活動報名人員清單(只有活動舉辦人和超級管理員可以)
			boolean isEditSignAvt=this.basicService.isEditSignAvtEnable(getUserId(), avtNo);
			theModel.addAttribute("isEditSignAvt", isEditSignAvt);
			
			QueryActivitySignDTO queryDTO = this.managerSignUpAvtService.findActivitySignData(new QueryActivitySignDTO(), avtNo);
			queryDTO.setCheck(check);

			theModel.addAttribute("queryDTO", queryDTO);
			this.showResult(queryDTO.getSignAvtDTOs(), theModel);
			
			//帶入新增需傳入參數
			SignAvtDTO addDTO=new SignAvtDTO();
			addDTO.setCheck(check);
			addDTO.setActivity(queryDTO.getActivity());
			theModel.addAttribute("addDTO", addDTO);
			
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					queryDTO.getCheck() == 1 ? new BreadDTO("活動審核", "/manager/activityReview"): new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					queryDTO.getCheck() == 1 ? new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
									       : new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
					,"報名人員清冊");//



			return "manager/activity/signAvtData";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋
	 */
	@GetMapping("/search")
	public String activityManagementDetailSidnUpDataSearch(
			@ModelAttribute("queryDTO") @Valid QueryActivitySignDTO queryDTO, Model theModel) {
		try {
			//是否有權限編輯活動報名人員清單(只有活動舉辦人和超級管理員可以)
			boolean isEditSignAvt=this.basicService.isEditSignAvtEnable(getUserId(), queryDTO.getActivity().getAvtNo());
			theModel.addAttribute("isEditSignAvt", isEditSignAvt);
			
			queryDTO = this.managerSignUpAvtService.findActivitySignData(queryDTO, queryDTO.getActivity().getAvtNo());

			theModel.addAttribute("queryDTO", queryDTO);
			this.showResult(queryDTO.getSignAvtDTOs(), theModel);
			
			//帶入新增需傳入參數
			SignAvtDTO addDTO=new SignAvtDTO();
			addDTO.setCheck(queryDTO.getCheck());
			addDTO.setActivity(queryDTO.getActivity());
			theModel.addAttribute("addDTO", addDTO);
			
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					queryDTO.getCheck() == 1 ? new BreadDTO("活動審核", "/manager/activityReview")
							: new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					queryDTO.getCheck() == 1 ? new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
							: new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ queryDTO.getActivity().getAvtNo()),
					"報名人員清冊");//
			

			return "manager/activity/signAvtData";
		} catch (Exception e) {
			this.activityManagementDetailSidnUpData(queryDTO.getActivity().getAvtNo(), queryDTO.getCheck(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 匯出csv
	 */
	@GetMapping("/csv")
	public String activityManagementDetailSidnUpDataCsv(@RequestParam("check") int check, //
			@RequestParam("keyWord") String keyWord, //
			@RequestParam("avtNo") int avtNo, //
			Model theModel, //
			HttpServletRequest request, //
			HttpServletResponse response) {

		QueryActivitySignDTO queryDTO = new QueryActivitySignDTO();
		queryDTO.setCheck(check);
		queryDTO.setKeyWord(keyWord);
		queryDTO.setActivity(new Activity());
		queryDTO.getActivity().setAvtNo(avtNo);

		try {
			queryDTO = this.managerSignUpAvtService.generateSignAvtCSV(queryDTO);
			FileTool.downLoadFile(request, response, FileTool.resource_prefix("/images/manager/activity"),queryDTO.getActivity().getAvtName().replaceAll(" ", "-").replaceAll("/", "") + "報名人員清冊.csv");

			this.activityManagementDetailSidnUpDataSearch(queryDTO, theModel);

			return "manager/activity/signAvtData";
		} catch (Exception e) {
			this.activityManagementDetailSidnUpDataSearch(queryDTO, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}
	

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 新增人員(畫面)
	 */
	@RequestMapping("/add")
	public String activityManagementDetailSidnUpDataAdd(Model theModel,
			@ModelAttribute("addDTO") @Valid SignAvtDTO queryDTO) {//

		queryDTO.setAddOrEdit("新增人員");

		try {

			this.managerSignUpAvtService.editSignAvt(queryDTO);

			theModel.addAttribute("queryDTO", queryDTO);
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					queryDTO.getCheck() == 1 ? new BreadDTO("活動審核", "/manager/activityReview")
							: new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					queryDTO.getCheck() == 1 ? new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
							: new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ queryDTO.getActivity().getAvtNo()),//
					new BreadDTO("報名人員清冊",
							"/manager/activityManagement/search/detail/signAvtData?activityId="
									+ queryDTO.getActivity().getAvtNo() + "&check=" + queryDTO.getCheck()), //
					"新增人員");//
			

			return "manager/activity/signEdit";
		} catch (Exception e) {
			this.activityManagementDetailSidnUpData(queryDTO.getActivity().getAvtNo(), queryDTO.getCheck(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}
	
	
	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 新增人員(畫面) 會員資料(畫面)
	 */
	@GetMapping("/add/member")
	public String activityManagementDetailSidnAvtAddMember(//
			@RequestParam("check") int check, //
			@RequestParam("avtNo") int avtNo, Model theModel) {//

		QueryMemberQualifyDTO dto=new QueryMemberQualifyDTO();
		dto.setActivity(this.basicService.findActivity(avtNo));
		dto.setCheck(check);
		dto.setStatus("yes");//只能查有資格的會員
		
		try {

			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(dto,0,0);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);

			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					check == 1 ? new BreadDTO("活動審核", "/manager/activityReview")
							: new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					dto.getCheck() == 1 ? new BreadDTO(dto.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ dto.getActivity().getAvtNo())//
							: new BreadDTO(dto.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ dto.getActivity().getAvtNo()),//
					new BreadDTO("報名人員清冊",
							"/manager/activityManagement/search/detail/signAvtData?activityId="
									+ avtNo + "&check=" + check), //
					"新增人員(會員資料查詢)");//
			

			return "manager/activity/signEdit_member";
		} catch (Exception e) {
			//帶入新增需傳入參數
			SignAvtDTO addDTO=new SignAvtDTO();
			addDTO.setCheck(check);
			addDTO.setActivity(dto.getActivity());
			this.activityManagementDetailSidnUpDataAdd(theModel, addDTO);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/signEdit";
		}
	}
	
	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 新增人員(畫面) 會員資料(畫面) 搜尋
	 */
	@RequestMapping("/add/member/search")
	public String activityManagementDetailSidnAvtAddMemberSearch(
			@ModelAttribute("queryDTO") @Valid QueryMemberQualifyDTO queryDTO, Model theModel) {//

		try {
			queryDTO.setActivity(this.basicService.findActivity(queryDTO.getActivity().getAvtNo()));
			queryDTO.setStatus("yes");//只能查有資格的會員
			QueryMemberQualifyDTO queryMemberQualifyDTO = managerService.findMemberAllData(queryDTO,0,0);
			this.showResult(queryMemberQualifyDTO.getMemberDTOs(), theModel);

			theModel.addAttribute("queryDTO", queryMemberQualifyDTO);
			
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					queryDTO.getCheck() == 1 ? new BreadDTO("活動審核", "/manager/activityReview")
							: new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					queryDTO.getCheck() == 1 ? new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
							: new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ queryDTO.getActivity().getAvtNo()),//
					new BreadDTO("報名人員清冊",
							"/manager/activityManagement/search/detail/signAvtData?activityId="
									+ queryDTO.getActivity().getAvtNo() + "&check=" + queryDTO.getCheck()), //
					"新增人員(會員資料查詢)");//
			

			return "manager/activity/signEdit_member";
		} catch (Exception e) {
			this.activityManagementDetailSidnAvtAddMember(queryDTO.getCheck(), queryDTO.getActivity().getAvtNo(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/signEdit_member";
		}
	}
	
	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 新增人員(畫面) 會員資料(畫面) 搜尋 帶入新增人員畫面
	 */
	@RequestMapping("/add/member/bring")
	public String activityManagementDetailSidnAvtAddMemberBring(//
			@RequestParam("check") int check, //
			@RequestParam("avtNo") int avtNo, //
			@RequestParam("userName") String userName, //
			Model theModel) {//

		try {
			
			//帶入會員資料至新增報名人員資料
			SignAvtDTO addDTO=new SignAvtDTO();
			addDTO.setCheck(check);
			addDTO.setActivity(this.basicService.findActivity(avtNo));
			addDTO.setMember(this.basicService.findMember(userName));
			this.managerSignUpAvtService.loadMemberToSignAvt(addDTO);
			
			this.activityManagementDetailSidnUpDataAdd(theModel,addDTO);

			return "manager/activity/signEdit";
		} catch (Exception e) {
			this.activityManagementDetailSidnAvtAddMember(check, avtNo, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/signEdit_member";
		}
	}

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 修改(畫面)
	 */
	@RequestMapping("/modify")
	public String activityManagementDetailSidnUpDataModify(@RequestParam("signId") int signId, //
			@RequestParam("check") int check, //
			@RequestParam("keyWord") String keyWord, //
			@RequestParam("avtNo") int avtNo, //
			Model theModel) {//

		SignAvtDTO queryDTO = new SignAvtDTO();
		queryDTO.setAddOrEdit("修改資料");
		queryDTO.setCheck(check);
		queryDTO.setKeyWord(keyWord);
		queryDTO.setActivity(new Activity());
		queryDTO.getActivity().setAvtNo(avtNo);
		queryDTO.setSignupAvt(new SignupAvt());
		queryDTO.getSignupAvt().setSignNo(signId);

		try {
			this.managerSignUpAvtService.editSignAvt(queryDTO);

			theModel.addAttribute("queryDTO", queryDTO);
			this.breadcrumbAndNavbar(theModel, "manager", //
					"活動管理", //
					queryDTO.getCheck() == 1 ? new BreadDTO("活動審核", "/manager/activityReview")
							: new BreadDTO("我的活動管理", "/manager/activityManagement"), //
					queryDTO.getCheck() == 1 ? new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityReview/search/detail?activityId="+ queryDTO.getActivity().getAvtNo())//
							: new BreadDTO(queryDTO.getActivity().getAvtName(),"/manager/activityManagement/search/detail?activityId="+ queryDTO.getActivity().getAvtNo()),//
					new BreadDTO("報名人員清冊",
							"/manager/activityManagement/search/detail/signAvtData?activityId="
									+ queryDTO.getActivity().getAvtNo() + "&check=" + check), //
					"修改");//
			

			return "manager/activity/signEdit";
		} catch (Exception e) {
			this.activityManagementDetailSidnUpData(avtNo, check, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 新增修改(存檔)
	 */
	@RequestMapping("/save")
	public String activityManagementDetailSidnUpDataSave(Model theModel,
			@ModelAttribute("queryDTO") @Valid SignAvtDTO queryDTO) {//

		queryDTO.setActivity(this.basicService.findActivity(queryDTO.getActivity().getAvtNo()));

		QueryActivitySignDTO signQueryDTO = new QueryActivitySignDTO();
		signQueryDTO.setKeyWord(queryDTO.getKeyWord());
		signQueryDTO.setCheck(queryDTO.getCheck());
		signQueryDTO.setActivity(queryDTO.getActivity());

		try {
			
			List<String> mistakes = this.verifySignAvt(queryDTO);
			if (CollectionUtils.isEmpty(mistakes)) {

				this.managerSignUpAvtService.saveEditSignAvt(queryDTO);

				return "redirect:/manager/activityManagement/search/detail/signAvtData/search?keyWord="
						+ URLEncoder.encode(queryDTO.getKeyWord(), "utf-8") + "&activity.avtNo="
						+ queryDTO.getActivity().getAvtNo() + "&check=" + queryDTO.getCheck();

			} else {
				this.showMessage(theModel, mistakes, "error");
				if ("新增人員".equals(queryDTO.getAddOrEdit())) {
					this.activityManagementDetailSidnUpDataAdd(theModel,queryDTO);
				} else {
					this.activityManagementDetailSidnUpDataModify(queryDTO.getSignupAvt().getSignNo(),
							queryDTO.getCheck(), queryDTO.getKeyWord(), queryDTO.getActivity().getAvtNo(), theModel);
				}
				theModel.addAttribute("queryDTO", queryDTO);
				return "manager/activity/signEdit";
			}

		} catch (Exception e) {
			this.activityManagementDetailSidnUpData(queryDTO.getActivity().getAvtNo(), queryDTO.getCheck(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}

	/**
	 * 報名資料(新增/修改)驗證
	 */
	public List<String> verifySignAvt(SignAvtDTO queryDTO) {

		List<String> mistakes = new ArrayList<>();
		if ("新增人員".equals(queryDTO.getAddOrEdit())) {
			User user=null;
			//若該活動需要親友資料，驗證是否填寫親友所屬會員帳號
			if(queryDTO.getActivity().getAvtFrdData() == (byte) 1){
				user = this.basicService.findUser(queryDTO.getSignupAvt().getMember().getMemUser());
				if (user == null) {
					mistakes.add("會員親友：無此會員");
				}
				if (user != null && user.getEnabled() == (byte) 0) {
					mistakes.add("會員親友：此會員無權限");
				}
			}else{
				//若該活動不須親友資料，會員帳號為必填
				if(StringUtils.isBlank(queryDTO.getMember().getMemUser())) {
					mistakes.add("請輸入會員帳號");
				}
			}	
			
			if(!StringUtils.isBlank(queryDTO.getMember().getMemUser())) {
				user = this.basicService.findUser(queryDTO.getMember().getMemUser());
				if (user == null) {
					mistakes.add("無此會員帳號");
				}
			}

			if (!CollectionUtils.isEmpty(this.managerSignUpAvtService.findSignupAvt(queryDTO.getMember().getMemUser(),queryDTO.getSignupAvt().getSignName(),
					queryDTO.getSignupAvt().getSignIdn(), queryDTO.getActivity().getAvtNo()))) {
				mistakes.add("請勿重複新增報名資料");
			}

		}

		return mistakes;

	}


	/**
	 * 我的活動管理 列表查詢 查看活動(詳細資料) 
	 * 報名人員清冊 搜尋 刪除
	 */
	@RequestMapping("/delete")
	public String activityManagementDetailSidnUpDataDelete(@RequestParam("signId") int signId, //
			@RequestParam("check") int check, //
			@RequestParam("keyWord") String keyWord, //
			@RequestParam("avtNo") int avtNo, //
			Model theModel) {//

		QueryActivitySignDTO queryDTO = new QueryActivitySignDTO();
		queryDTO.setCheck(check);
		queryDTO.setKeyWord(keyWord);
		queryDTO.setActivity(new Activity());
		queryDTO.getActivity().setAvtNo(avtNo);

		try {

			this.managerSignUpAvtService.deleteSignAvt(signId);

			return "redirect:/manager/activityManagement/search/detail/signAvtData/search?keyWord="
					+ URLEncoder.encode(queryDTO.getKeyWord(), "utf-8") + "&activity.avtNo="
					+ queryDTO.getActivity().getAvtNo() + "&check=" + queryDTO.getCheck();

		} catch (Exception e) {
			this.activityManagementDetailSidnUpDataSearch(queryDTO, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "manager/activity/all";
		}
	}



}
