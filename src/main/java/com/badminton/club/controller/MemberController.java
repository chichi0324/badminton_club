package com.badminton.club.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.badminton.club.dto.BreadDTO;
import com.badminton.club.dto.FooterDTO;
import com.badminton.club.dto.MemberDTO;
import com.badminton.club.dto.MessageDTO;
import com.badminton.club.dto.QueryActivityDTO;
import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.SignupAvt;
import com.badminton.club.service.ActivityService;
import com.badminton.club.service.BasicService;
import com.badminton.club.service.MemberService;
import com.badminton.club.tools.BCrypt;
import com.badminton.club.tools.FileTool;
/**
 * 社員專區(社員資料修改，我的活動) 控制器
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{
	

	@Autowired
	private ActivityService activityService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BasicService basicService;



	/**
	 * 社員資料修改(社員資料畫面)
	 */
	@GetMapping("/modify")
	public String modify(Model theModel) {
		try {
			this.fuctionList(theModel, "社員資料", new BreadDTO("更新大頭貼", "/member/modifySck"),
					new BreadDTO("密碼修改", "/member/modifyPsd"));
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "社員資料");
			this.getAllActivityShowLightBox(theModel);

			// 帶入會員資料
			MemberDTO memberDTO = this.getMemberData();

			theModel.addAttribute("memberDTO", memberDTO);

			return "member/mem";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "member/mem";
		}
	}

	/**
	 * 社員資料修改(大頭貼畫面)
	 */
	@GetMapping("/modifySck")
	public String modifySticker(Model theModel) {
		try {
			this.fuctionList(theModel, new BreadDTO("社員資料", "/member/modify"), "更新大頭貼",
					new BreadDTO("密碼修改", "/member/modifyPsd"));
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "更新大頭貼");
			this.getAllActivityShowLightBox(theModel);

			// 帶入會員資料
			MemberDTO memberDTO = this.getMemberData();

			theModel.addAttribute("memberDTO", memberDTO);

			return "member/mem_sticker";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "member/mem_sticker";
		}
	}

	/**
	 * 社員資料修改(密碼畫面)
	 */
	@GetMapping("/modifyPsd")
	public String modifyPassword(Model theModel) {
		try {
			this.fuctionList(theModel, new BreadDTO("社員資料", "/member/modify"),
					new BreadDTO("更新大頭貼", "/member/modifySck"), "密碼修改");
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "密碼修改");
			this.getAllActivityShowLightBox(theModel);

			// 帶入會員資料
			MemberDTO memberDTO = this.getMemberData();

			theModel.addAttribute("memberDTO", memberDTO);

			return "member/mem_psd";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "member/mem_psd";
		}
	}

	/**
	 * 修改(資料)
	 */
	@RequestMapping("/doModify")
	public String doModify(Model theModel, @ModelAttribute("memberDTO") @Valid MemberDTO dto) {
		try {
			this.fuctionList(theModel, "社員資料", new BreadDTO("更新大頭貼", "/member/modifySck"),
					new BreadDTO("密碼修改", "/member/modifyPsd"));
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "社員資料");
			this.getAllActivityShowLightBox(theModel);

			// 驗證會員資料
			List<String> mistakes = this.verifyData(dto);
			if (CollectionUtils.isEmpty(mistakes)) {
				// 修改會員資料

				MemberDTO memberDTO = this.memberService.updateMemberData(getUserId(), dto);
				theModel.addAttribute("memberDTO", memberDTO);
				this.showMessage(theModel, "修改成功", "notice");
			} else {
				this.showMessage(theModel, mistakes, "error");
			}

		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
		}
		return "member/mem";

	}

	/**
	 * 修改(大頭貼)
	 */
	@RequestMapping("/doModifySck")
	public String doModifySticker(@RequestParam MultipartFile file, Model theModel) {
		try {
			this.fuctionList(theModel, new BreadDTO("社員資料", "/member/modify"), "更新大頭貼",
					new BreadDTO("密碼修改", "/member/modifyPsd"));
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "更新大頭貼");
			this.getAllActivityShowLightBox(theModel);

			// 帶入會員資料
			MemberDTO memberDTO = this.getMemberData();

			if (!file.getOriginalFilename().isEmpty()) {

				if (memberDTO.getMember().getMemImg() == null) {
					FileTool.upload(file, "/images/member/");
					memberDTO.getMember().setMemImg("/images/member/" + file.getOriginalFilename());
					this.memberService.updateMemberSticker(memberDTO.getMember());
					theModel.addAttribute("memberDTO", memberDTO);
				} else {
					FileTool.modifyAndUpload(file, "/images/member/",memberDTO.getMember().getMemImg());
				}

			} else {
				this.showMessage(theModel, "上傳圖片失敗", "error");
				return "login/register_image";
			}

		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
		}
		return "member/mem_sticker";

	}

	/**
	 * 修改(密碼)
	 */
	@RequestMapping("/doModifyPsd")
	public String doModifyPsd(Model theModel, @ModelAttribute("memberDTO") @Valid MemberDTO newDTO) {
		try {
			this.fuctionList(theModel, new BreadDTO("社員資料", "/member/modify"),
					new BreadDTO("更新大頭貼", "/member/modifySck"), "密碼修改");
			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "社員資料修改", "密碼修改");
			this.getAllActivityShowLightBox(theModel);

			MemberDTO oldDTO = this.getMemberData();

			// 驗證會員資料
			List<String> mistakes = this.verifyPsd(newDTO, oldDTO);
			if (CollectionUtils.isEmpty(mistakes)) {
				oldDTO = this.memberService.updatePassword(newDTO, oldDTO);
				theModel.addAttribute("memberDTO", oldDTO);
				this.showMessage(theModel, "修改成功", "notice");
			} else {
				this.showMessage(theModel, mistakes, "error");
			}

		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
		}
		return "member/mem_psd";

	}

	/**
	 * 會員驗證(資料)
	 */
	public List<String> verifyData(MemberDTO dto) {

		List<String> mistakes = new ArrayList<>();

		// 確認姓名
		if (StringUtils.isBlank(dto.getMember().getMemName())) {
			mistakes.add("請輸入姓名");
		}
		// 身份證字號
		if (StringUtils.isBlank(dto.getMember().getMemIdn())) {
			mistakes.add("請輸入身份證字號");
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

	/**
	 * 會員驗證(密碼)
	 */
	public List<String> verifyPsd(MemberDTO newDTO, MemberDTO oldDTO) {

		List<String> mistakes = new ArrayList<>();

		// 原密碼
		if (StringUtils.isBlank(newDTO.getUser().getPassword())) {
			mistakes.add("請輸入原密碼");
		} else {
			if (newDTO.getUser().getPassword().trim().length() != 6) {
				mistakes.add("原密碼需為6碼");
			}
			if (!newDTO.getUser().getPassword().trim().matches("[a-zA-Z0-9]{6}")) {
				mistakes.add("原密碼必須包含英文和數字");
			}
			if (!BCrypt.checkOriginalPassword(newDTO.getUser().getPassword().trim(), oldDTO.getUser().getPassword())) {
				mistakes.add("原密碼錯誤");
			}
		}
		// 新密碼
		if (StringUtils.isBlank(newDTO.getCkeckPwd())) {
			mistakes.add("請輸入新密碼");
		} else {
			if (newDTO.getCkeckPwd().trim().length() != 6) {
				mistakes.add("新密碼需為6碼");
			}
			if (!newDTO.getCkeckPwd().trim().matches("[a-zA-Z0-9]{6}")) {
				mistakes.add("新密碼必須包含英文和數字");
			}
			if (BCrypt.checkOriginalPassword(newDTO.getCkeckPwd().trim(), oldDTO.getUser().getPassword())) {
				mistakes.add("原密碼和新密碼不可以一樣");
			}
		}
		return mistakes;
	}


	/**
	 * 我的活動畫面
	 */
	@GetMapping("/activity")
	public String activity(Model theModel) {
		try {
			QueryActivityDTO queryActivityDTO = new QueryActivityDTO();

			List<SignupAvt> theSignupAvts = memberService.searchJoinActivity(getUserId(), queryActivityDTO);
			theModel.addAttribute("theSignupAvts", theSignupAvts);
			this.showResult(theSignupAvts, theModel);

			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "我的活動");
			this.getAllActivityShowLightBox(theModel);

			theModel.addAttribute("queryActivity", queryActivityDTO);

			return "member/mem_avt";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			return "member/mem_psd";
		}
	}

	/**
	 * 我的活動(依條件搜尋)
	 */
	@GetMapping("/activitySearch")
	public String activitySearch(Model theModel,
			@ModelAttribute("queryActivity") @Valid QueryActivityDTO queryActivityDTO) {
		try {
			if (queryActivityDTO == null) {
				queryActivityDTO = new QueryActivityDTO();
			}

			List<SignupAvt> theSignupAvts = memberService.searchJoinActivity(getUserId(), queryActivityDTO);
			theModel.addAttribute("theSignupAvts", theSignupAvts);
			this.showResult(theSignupAvts, theModel);

			this.breadcrumbAndNavbar(theModel, "member", "社員專區", "我的活動");
			this.getAllActivityShowLightBox(theModel);

			return "member/mem_avt";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			this.activity(theModel);
			return "member/mem_avt";
		}
	}

	/**
	 * 我的活動 詳細資料
	 */
	@GetMapping("/activitySearch/detail")
	public String activityDetail(@RequestParam("activityId") int theId, Model theModel) {
		try {
			Activity theActivity = this.activityService.findActivityIncludeMessageBytimeDesc(theId);
			theModel.addAttribute("theActivity", theActivity);
			theModel.addAttribute("advocateSize", theActivity.getAdvocates().size());

			List<SignupAvt> joinDatas = memberService.joinActivityData(getUserId(), theId);
			theModel.addAttribute("joinDatas", joinDatas);

			Member member = this.basicService.findMember(getUserId());
			theModel.addAttribute("theMember", member);

			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setAvtNo(theActivity.getAvtNo());
			messageDTO.setStarCount(0);
			theModel.addAttribute("theMessage", messageDTO);

			this.breadcrumbAndNavbar(theModel, "member", "社員專區", new BreadDTO("我的活動", "/member/activity"), "詳細資料");
			this.getAllActivityShowLightBox(theModel);

			return "activity/avt-item";
		} catch (Exception e) {
			this.showMessage(theModel, e.getMessage(), "error");
			this.activity(theModel);
			return "member/mem_avt";
		}
	}


}
