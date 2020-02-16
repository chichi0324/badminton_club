package com.badminton.club.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.AvtPreview;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.ManagerService;
import com.badminton.club.service.MemberService;
import com.badminton.club.tools.FileTool;

public class BaseController {
	
	@Autowired
	private BasicService basicService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ManagerService managerService;
	
	/**
	 * 幻燈片所有活動
	 * 及 活動類型(下拉式選單)
	 * 及 底下footer
	 */
	public void getAllActivityShowLightBox(Model theModel) {
		List<Activity> theActivities = basicService.showLightBox();
		if (CollectionUtils.isEmpty(theActivities)) {
			theModel.addAttribute("activities_size", 0);
		} else {
			theModel.addAttribute("activities_size", theActivities.size());
		}
		theModel.addAttribute("activities_show", theActivities);
		//活動類型(下拉式選單)
		theModel.addAttribute("activitieTypes", basicService.getAllActivityType());

	}
	
	/**
	 * 取得所有管理員(下拉式選單用)
	 */
	public void getAllManager(Model theModel) {
		theModel.addAttribute("allManager", basicService.getAllManager());
	}

	/**
	 * 取得目前會員帳號
	 */
	public String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
	
	/**
	 * 帶入目前會員資料
	 */
	public MemberDTO getMemberData() {

		MemberDTO memberDTO = this.memberService.findMemberData(getUserId());
		return memberDTO;
	}
	
	/**
	 * 分頁(當前頁,總頁數)
	 * indexPage:當頁
	 * countOnePage:一頁筆數
	 * totalCount:查詢出來總筆數
	 */
	public void getPage(int indexPage,int countOnePage,int totalCount,Model theModel) {
		
		BigDecimal b1 = new BigDecimal(totalCount);
	    BigDecimal b2 = new BigDecimal(countOnePage);
	    int totalPage = ((b1.divide(b2,0,BigDecimal.ROUND_UP)).toBigInteger()).intValue();
		
		theModel.addAttribute("indexPage", indexPage);
		theModel.addAttribute("totalPage", totalPage);
	}
	
	/**
	 * 取得HpptSessoion
	 */
	public HttpSession getHttpSession(Model theModel, ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		return session;
	}

	/**
	 * 刪除新增活動時，沒刪成功的暫存活動圖片
	 */
	public void deleteTempImg(Model theModel) {
		FileTool.deleteFile("/images/activity/avt_" + this.getUserId() + ".jpg");
		for (int i = 1; i <= 5; i++) {
			FileTool.deleteFile("/images/activity/avt_" + this.getUserId() + "/adv_" + i + ".jpg");
		}
		FileTool.deleteDirectory("/images/activity/avt_" + this.getUserId() + "/");
	}


	/**
	 * 活動畫面需要資料(管理員功能查看活動畫面)
	 */
	public void getActivityDetail(Activity theActivity, Model theModel) {
		theModel.addAttribute("theActivity", theActivity);
		theModel.addAttribute("advocateSize", theActivity.getAdvocates().size());

		// 已審核過管理員
		List<AvtPreview> avtPreviews = this.managerService.findActivityPassed(theActivity.getAvtNo());
		if (CollectionUtils.isEmpty(avtPreviews)) {
			theModel.addAttribute("passedManager", "無");
		} else {
			StringBuilder passedManager = new StringBuilder();
			avtPreviews.forEach(avtPre -> {
				passedManager.append(avtPre.getMember().getMemName() + "，");
			});
			passedManager.append("...");
			theModel.addAttribute("passedManager", passedManager);
		}

		// 除了基本會員資料，需要其他資料
		if (CollectionUtils.isEmpty(theActivity.getOtherData())) {
			theModel.addAttribute("otherDatas", null);
		} else {
			StringBuilder otherDatas = new StringBuilder();
			theActivity.getOtherData().forEach(o -> {
				otherDatas.append(o.getOthName() + "，");
			});
			otherDatas.append("...");
			theModel.addAttribute("otherDatas", otherDatas);
		}

	}
	/**
	 * ============================麵包屑============================
	 */
	public void breadcrumbAndNavbar(Model theModel, String focus, String first, String second) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(new BreadDTO(second));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}

	public void breadcrumbAndNavbar(Model theModel, String focus, String first, BreadDTO secondDTO,
			String third) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(secondDTO);
		breadcrumbs.add(new BreadDTO(third));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}
	
	public void breadcrumbAndNavbar(Model theModel, String focus, String first, String second, String third) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(new BreadDTO(second));
		breadcrumbs.add(new BreadDTO(third));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}
	
	public void breadcrumbAndNavbar(Model theModel, String focus, String first, BreadDTO secondDTO,
			String third,String fourth) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(secondDTO);
		breadcrumbs.add(new BreadDTO(third));
		breadcrumbs.add(new BreadDTO(fourth));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}

	public void breadcrumbAndNavbar(Model theModel, String focus, String first, BreadDTO secondDTO,
			BreadDTO thirdDTO, String fourth) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(secondDTO);
		breadcrumbs.add(thirdDTO);
		breadcrumbs.add(new BreadDTO(fourth));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}
	
	public void breadcrumbAndNavbar(Model theModel, String focus, String first, BreadDTO secondDTO,
			BreadDTO thirdDTO, BreadDTO firthDTO,String fifth) {
		theModel.addAttribute("focus", focus);// 導覽列鎖定

		List<BreadDTO> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new BreadDTO(first));
		breadcrumbs.add(secondDTO);
		breadcrumbs.add(thirdDTO);
		breadcrumbs.add(firthDTO);
		breadcrumbs.add(new BreadDTO(fifth));
		theModel.addAttribute("breadcrumbs", breadcrumbs);
	}


	/**
	 * ============================頁面功能列============================
	 */
	public void fuctionList(Model theModel, String first, BreadDTO secondDTO, BreadDTO thirdDTO) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(new BreadDTO(first));
		funcs.add(secondDTO);
		funcs.add(thirdDTO);
		theModel.addAttribute("fuctions", funcs);
	}

	public void fuctionList(Model theModel, BreadDTO firstDTO, String second, BreadDTO thirdDTO) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(firstDTO);
		funcs.add(new BreadDTO(second));
		funcs.add(thirdDTO);
		theModel.addAttribute("fuctions", funcs);
	}

	public void fuctionList(Model theModel, BreadDTO firstDTO, BreadDTO secondDTO, String third) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(firstDTO);
		funcs.add(secondDTO);
		funcs.add(new BreadDTO(third));
		theModel.addAttribute("fuctions", funcs);
	}
	
	public void fuctionList(Model theModel, String first, BreadDTO secondDTO, BreadDTO thirdDTO, BreadDTO firthDTO) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(new BreadDTO(first));
		funcs.add(secondDTO);
		funcs.add(thirdDTO);
		funcs.add(firthDTO);
		theModel.addAttribute("fuctions", funcs);
	}

	public void fuctionList(Model theModel, BreadDTO firstDTO, String second, BreadDTO thirdDTO, BreadDTO firthDTO) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(firstDTO);
		funcs.add(new BreadDTO(second));
		funcs.add(thirdDTO);
		funcs.add(firthDTO);
		theModel.addAttribute("fuctions", funcs);
	}

	public void fuctionList(Model theModel, BreadDTO firstDTO, BreadDTO secondDTO, String third, BreadDTO firthDTO) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(firstDTO);
		funcs.add(secondDTO);
		funcs.add(new BreadDTO(third));
		funcs.add(firthDTO);
		theModel.addAttribute("fuctions", funcs);
	}
	
	public void fuctionList(Model theModel, BreadDTO firstDTO, BreadDTO secondDTO, BreadDTO thirdDTO, String firth) {

		List<BreadDTO> funcs = new ArrayList<>();
		funcs.add(firstDTO);
		funcs.add(secondDTO);
		funcs.add(thirdDTO);
		funcs.add(new BreadDTO(firth));
		theModel.addAttribute("fuctions", funcs);
	}
	
	/**
	 * ============================活動搜尋版面============================
	 */
	
	/**
	 * 需顯示每四筆包含在一個row裡面(每四筆一個List)
	 */
	public void showActivityRowList(Model theModel, List<Activity> theActivities) {
		if (!CollectionUtils.isEmpty(theActivities)) {

			int a = theActivities.size() / 4;
			int b = theActivities.size() % 4;

			List<List<Activity>> theActivitiesList = new ArrayList<List<Activity>>();
			List<Activity> ativities = new ArrayList<>();
			for (int i = 1; i <= theActivities.size(); i++) {
				ativities.add(theActivities.get(i - 1));
				if (i % 4 == 0 || (i / 4 == a && i % 4 == b)) {
					theActivitiesList.add(ativities);
					ativities = new ArrayList<>();
				}
			}
			theModel.addAttribute("ActivitiesList", theActivitiesList);
		}
	}
	
	/**
	 * 搜尋結果有資料為true(顯示搜尋結果) 反之false(顯示查無資料)
	 */
	public void showResult(List<?> list, Model theModel) {
		if (CollectionUtils.isEmpty(list)) {
			theModel.addAttribute("show_search_result", false);
		} else {
			theModel.addAttribute("show_search_result", true);
		}
	}
	
	
	/**
	 * ============================彈跳視窗訊息============================
	 */
	/**
	 * 單筆訊息
	 */
	public void showMessage(Model theModel,String e,String message_type){
		List<String> messages = new ArrayList<>();
		messages.add(e);
		theModel.addAttribute("messages", messages);
		if("error".equals(message_type)){//錯誤訊息
			theModel.addAttribute("message_type", "error");
		}else{//通知訊息
			theModel.addAttribute("message_type", "notice");
		}
	}
	
	/**
	 * 多筆訊息
	 */
	public void showMessage(Model theModel,List<String> messages,String message_type){
		theModel.addAttribute("messages", messages);
		if("error".equals(message_type)){//錯誤訊息
			theModel.addAttribute("message_type", "error");
		}else{//通知訊息
			theModel.addAttribute("message_type", "notice");
		}
	}
}
