package com.badminton.club.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.badminton.club.dto.ApplyAllDTO;
import com.badminton.club.dto.ApplyDTO;
import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.FriendDTO;
import com.badminton.club.dto.MessageDTO;
import com.badminton.club.dto.OtherDataDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.service.ActivityService;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.MemberService;
/**
 * 活動專區(活動總覽，活動報名) 控制器
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController{
		

	@Autowired
	private ActivityService activityService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BasicService basicService;



	/**
	 * 活動總覽
	 */
	@GetMapping("/search")
	public String activitySearch(Model theModel) {
		try {
			QueryActivityDTO queryActivityDTO=new QueryActivityDTO("","","",1);
			theModel.addAttribute("queryActivity", queryActivityDTO);

			//活動查詢．第1頁，1頁共7筆
			List<Activity> theActivities = activityService.search(queryActivityDTO,queryActivityDTO.getPage(),8);
			this.showResult(theActivities, theModel);	
			this.getPage(queryActivityDTO.getPage(),8,activityService.searchCount(queryActivityDTO), theModel);

			this.showActivityRowList(theModel, theActivities);

			this.breadcrumbAndNavbar(theModel, "activity", "活動專區", "活動總覽");
			this.getAllActivityShowLightBox(theModel);

			return "activity/avt";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt";
		}
	}

	/**
	 * 活動總覽 搜尋
	 */
	@GetMapping("/searchBy")
	public String activitySearchCondition(Model theModel,
			@ModelAttribute("queryActivity") @Valid QueryActivityDTO queryActivityDTO) {
		try {
			if (queryActivityDTO == null) {
				queryActivityDTO = new QueryActivityDTO();
			}
			theModel.addAttribute("queryActivity", queryActivityDTO);

			List<Activity> theActivities = activityService.search(queryActivityDTO,queryActivityDTO.getPage(),8);
			this.showResult(theActivities, theModel);		
			this.getPage(queryActivityDTO.getPage(),8,activityService.searchCount(queryActivityDTO), theModel);

			this.showActivityRowList(theModel, theActivities);

			this.breadcrumbAndNavbar(theModel, "activity", "活動專區", "活動總覽");
			this.getAllActivityShowLightBox(theModel);

			return "activity/avt";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt";
		}
	}

	/**
	 * 活動總覽 詳細內容
	 */
	@GetMapping("/search/detail")
	public String activitySearchDetail(@RequestParam("activityId") int theId, Model theModel) {
		try {

			Activity theActivity = this.activityService.findActivityIncludeMessageBytimeDesc(theId);
			theModel.addAttribute("theActivity", theActivity);
			theModel.addAttribute("advocateSize", theActivity.getAdvocates().size());

			Member member = this.basicService.findMember(getUserId());
			theModel.addAttribute("theMember", member);

			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setAvtNo(theActivity.getAvtNo());
			messageDTO.setStarCount(0);
			theModel.addAttribute("theMessage", messageDTO);

			this.breadcrumbAndNavbar(theModel, "activity", "活動專區", new BreadDTO("活動總覽", "/activity/search"),
					theActivity.getAvtName());
			this.getAllActivityShowLightBox(theModel);

			return "activity/avt-item";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt";
		}
	}
	
	/**
	 * 送出回饋心得
	 */
	@RequestMapping("/search/detail/saveMessage")
	public String saveMessage(Model theModel, //
			@ModelAttribute("theMessage") @Valid MessageDTO theMessage) {
		try {

			List<String> mistakes = this.verifyMessage(theMessage);

			if (!CollectionUtils.isEmpty(mistakes)) {
				this.activitySearchDetail(theMessage.getAvtNo(), theModel);
				theModel.addAttribute("theMessage", theMessage);
				this.showMessage(theModel, mistakes, "error");
				return "activity/avt-item";
			}

			this.activityService.saveActivityMessage(getUserId(), theMessage);

			return "redirect:/activity/search/detail?activityId=" + theMessage.getAvtNo();

		} catch (Exception e) {
			this.activitySearchDetail(theMessage.getAvtNo(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt-item";
		}
	}

	/**
	 * 回饋心得驗證
	 */
	public List<String> verifyMessage(MessageDTO messageDTO) {

		List<String> mistakes = new ArrayList<>();
		if (StringUtils.isBlank(messageDTO.getMessage())) {
			mistakes.add("您還沒填資料喔");
		}
		if (messageDTO.getStarCount() == 0) {
			mistakes.add("您還沒評分唷");
		}
		return mistakes;
	}

	/**
	 * 刪除回饋心得
	 */
	@RequestMapping("/search/detail/deleteMessage")
	public String deleteMessage(Model theModel, //
			@ModelAttribute("avt_no") @Valid int avt_no, //
			@ModelAttribute("msg_no") @Valid int msg_no) {
		try {
			this.activityService.deleteActivityMessage(msg_no);

			return "redirect:/activity/search/detail?activityId=" + avt_no;

		} catch (Exception e) {
			this.activitySearchDetail(avt_no, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt-item";
		}
	}

	/**
	 * 活動總覽 詳細內容 我要報名
	 */
	@GetMapping("/search/detail/apply")
	public String activitySearchDetailApply(@RequestParam("activityId") int theId, Model theModel) {
		try {
			List<SignupAvt> joinActivities = this.memberService.joinActivityData(getUserId(), theId);
			Activity theActivity = this.basicService.findActivity(theId);

			// 若還沒報名過該活動(帶入報名需填寫的資料)
			if (CollectionUtils.isEmpty(joinActivities)) {

				ApplyDTO applyDTO = new ApplyDTO();
				applyDTO.setActivity(theActivity);
				applyDTO.setJoinNumber("1");
				theActivity.getOtherData().forEach(o -> {
					OtherDataDTO other = new OtherDataDTO();
					other.setNo(String.valueOf(o.getOthNo()));
					other.setName(o.getOthName());
					applyDTO.getOtherDatas().add(other);
				});

				theModel.addAttribute("applyDTO", applyDTO);
			} else {
				this.activitySearchDetail(theId, theModel);
				this.showMessage(theModel, "您已經報名此活動", "notice");
				return "activity/avt-item";
			}

			this.breadcrumbAndNavbar(theModel, "activity", "活動專區", new BreadDTO("活動總覽", "/activity/search"),
					new BreadDTO(theActivity.getAvtName(), "/activity/search/detail?activityId=" + theId), "我要報名");
			this.getAllActivityShowLightBox(theModel);

			return "activity_apply/avt-item-apply";
		} catch (Exception e) {
			this.activitySearchDetail(theId, theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity/avt-item";
		}
	}


	/**
	 * 活動報名 確認資料和報名存檔 若有帶入親友填寫 下一頁或送出
	 */
	@RequestMapping("/search/detail/apply/check")
	public String activityApplyVerity(Model theModel, @ModelAttribute("applyDTO") @Valid ApplyDTO applyDTO) {
		try {

			this.getAllActivityShowLightBox(theModel);

			Activity theActivity = this.basicService.findActivity(applyDTO.getActivity().getAvtNo());
			applyDTO.setActivity(theActivity);

			List<String> mistakes = this.verifyApply(applyDTO);

			if (CollectionUtils.isEmpty(mistakes)) {
				// 該活動需要親友資料 及 參與人數須大於1(帶入給其他參與人數填寫的資料)
				if (theActivity.getAvtFrdData() == (byte) 1 && Integer.parseInt(applyDTO.getJoinNumber()) > 1) {
					this.breadcrumbAndNavbar(theModel, "activity", "活動專區", new BreadDTO("活動總覽", "/activity/search"),
							new BreadDTO(theActivity.getAvtName(),
									"/activity/search/detail?activityId=" + theActivity.getAvtNo()),
							"我要報名");

					ApplyAllDTO applyAllDTO = new ApplyAllDTO();
					applyAllDTO.setApplyDTO(applyDTO);

					for (int i = 0; i < Integer.parseInt(applyDTO.getJoinNumber()) - 1; i++) {
						FriendDTO FriendDTO = new FriendDTO();
						theActivity.getOtherData().forEach(o -> {
							OtherDataDTO otherDataDTO = new OtherDataDTO();
							otherDataDTO.setNo(String.valueOf(o.getOthNo()));
							otherDataDTO.setName(o.getOthName());
							FriendDTO.getOtherDatas().add(otherDataDTO);
						});
						applyAllDTO.getFriendDTOs().add(FriendDTO);
					}

					theModel.addAttribute("applyAllDTO", applyAllDTO);

					return "activity_apply/avt-item-friend-data";
				} else {// 參與只有個人 或 該活動不需要親友資料

					// 新增報名資料
					this.activityService.saveSignAvtActivity(getUserId(), applyDTO);

					// 若參加總人數達上限名額，活動狀態改為"已額滿"
					this.basicService.updateActivityStatus(theActivity.getAvtNo());

					return "redirect:/member/activity";
				}
			} else {
				this.activitySearchDetailApply(theActivity.getAvtNo(), theModel);
				theModel.addAttribute("applyDTO", applyDTO);
				this.showMessage(theModel, mistakes, "error");
				return "activity_apply/avt-item-apply";
			}

		} catch (Exception e) {
			this.activitySearchDetailApply(applyDTO.getActivity().getAvtNo(), theModel);
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity_apply/avt-item-apply";
		}
	}

	/**
	 * 活動報名 驗證即存檔報名資料(自己及親友)
	 */
	@RequestMapping("/search/detail/apply/send")
	public String activityApplySendAll(Model theModel, @ModelAttribute("applyAllDTO") @Valid ApplyAllDTO applyAllDTO) {
		try {

			this.getAllActivityShowLightBox(theModel);

			Activity theActivity = this.basicService.findActivity(applyAllDTO.getApplyDTO().getActivity().getAvtNo());
			applyAllDTO.getApplyDTO().setActivity(theActivity);

			List<String> mistakes = this.verifyApplyAll(applyAllDTO);
			if (CollectionUtils.isEmpty(mistakes)) {

				// 新增報名資料
				this.activityService.saveSignAvtActivity(getUserId(), applyAllDTO);

				// 若參加總人數達上限名額，活動狀態改為"已額滿"
				this.basicService.updateActivityStatus(theActivity.getAvtNo());

				return "redirect:/member/activity";

			} else {
				this.activityApplyVerity(theModel, applyAllDTO.getApplyDTO());
				this.showMessage(theModel, mistakes, "error");
				return "activity_apply/avt-item-friend-data";
			}
		} catch (Exception e) {
			this.activityApplyVerity(theModel, applyAllDTO.getApplyDTO());
			this.showMessage(theModel, e.getMessage(), "error");
			return "activity_apply/avt-item-friend-data";
		}
	}

	/**
	 * 報名資料驗證
	 */
	public List<String> verifyApply(ApplyDTO applyDTO) {

		List<String> mistakes = new ArrayList<>();

		if (StringUtils.isBlank(applyDTO.getCheckTake())) {
			mistakes.add("請確認系統帶入您的個人資料");
		}

		applyDTO.getOtherDatas().forEach((o) -> {
			if (StringUtils.isBlank(o.getData())) {
				mistakes.add("請輸入" + o.getName());
			}
		});

		// 報名時，參與人數不止一人
		// "目前活動報名總人數"加上"欲參與人數"大於"活動名額上限"時，秀出警告及活動剩餘名額
		if (Integer.parseInt(applyDTO.getJoinNumber()) > 1) {
			Activity theActivity = this.basicService.findActivity(applyDTO.getActivity().getAvtNo());
			int signupAvtNumber = this.activityService.getActivitySum(applyDTO.getActivity().getAvtNo());
			if (signupAvtNumber + Integer.parseInt(applyDTO.getJoinNumber()) > theActivity.getAvtUpp()) {
				mistakes.add("目前名額剩餘" + (theActivity.getAvtUpp() - signupAvtNumber) + "名");
			}
		}

		return mistakes;

	}

	/**
	 * 報名資料驗證(全部含親友)
	 */
	public List<String> verifyApplyAll(ApplyAllDTO applyAllDTO) {
		List<String> mistakes = new ArrayList<>();

		for (int i = 1; i <= applyAllDTO.getFriendDTOs().size(); i++) {
			FriendDTO friend = applyAllDTO.getFriendDTOs().get(i - 1);
			// 確認姓名
			if (StringUtils.isBlank(friend.getSignupAvt().getSignName())) {
				mistakes.add("第" + i + "親友 請輸入姓名");
			}
			// 性別
			if (StringUtils.isBlank(friend.getSignupAvt().getSignGen())) {
				mistakes.add("第" + i + "親友 請輸入性別");
			}
			// 生日
			if (StringUtils.isBlank(friend.getBirthDate())) {
				mistakes.add("第" + i + "親友 請輸入生日");
			}
			// 身份證字號
			if (StringUtils.isBlank(friend.getSignupAvt().getSignIdn())) {
				mistakes.add("第" + i + "親友 請輸入身份證字號");
			}
			// 手機號碼
			if (StringUtils.isBlank(friend.getSignupAvt().getSignPhone())) {
				mistakes.add("第" + i + "親友 請輸入手機號碼");
			}
			// 電子信箱
			if (StringUtils.isBlank(friend.getSignupAvt().getSignMail())) {
				mistakes.add("第" + i + "親友 請輸入電子信箱");
			}
			// 地址
			if (StringUtils.isBlank(friend.getSignupAvt().getSignAddr())) {
				mistakes.add("第" + i + "親友 請輸入地址");
			}

			// 其他資料
			for (OtherDataDTO oth : friend.getOtherDatas()) {
				if (StringUtils.isBlank(oth.getData())) {
					mistakes.add("第" + i + "親友 請輸入" + oth.getName());
				}
			}

		}

		return mistakes;

	}

}
