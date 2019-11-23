package com.badminton.club.tools;

import com.badminton.club.entity.Activity;
import com.badminton.club.entity.Member;
import com.badminton.club.entity.User;
import com.badminton.club.service.MailService;
/**
 * 信件內容傳送工具
 */
public class MailTool {

	/**
	 * 傳送gmail信件及其內容
	 */
	public static void transferPasseord(String newPassword, Member member, String systemMail, String systemMailPassword) {

		String messageText = "您好! 親愛的 " + member.getMemName() + " ，\n您的新密碼為: " + newPassword
				+ "。\n若有需求可以登入再修改。\n\n\n" + " 基分撲蝶會活動網站  敬啟  \n\n(此為系統自動通知信，請勿直接回信！)";

		MailService.sendMail(member.getMemMail(), //
				"忘記密碼通知", //
				messageText, //
				systemMail, //
				systemMailPassword);//
	}
	
	/**
	 * 傳送gmail活動通知信(新刊登活動)
	 */
	public static void transferActivityInfo(Member member,Activity activity, String systemMail, String systemMailPassword) {

		String messageText = "您好! 親愛的 " + member.getMemName() //
		+ " ，\n這是我們的新活動: " + activity.getAvtName()//
		+ " ，\n http://localhost:8080/badminton/activity/search/detail?activityId=" + activity.getAvtNo()//
		+ " ，\n 希望您能來一起參與。" //
				+ "\n\n\n" + " 基分撲蝶會活動網站  敬啟  \n\n(此為系統自動通知信，請勿直接回信！)";//

		MailService.sendMail(member.getMemMail(), //
				"活動通知："+activity.getAvtName(), //
				messageText, //
				systemMail, //
				systemMailPassword);//
	}
	
	/**
	 * 傳送gmail活動通知信(當天活動開始及提醒填寫留言)
	 */
	public static void transferActivityStartMessage(Member member,Activity activity, String systemMail, String systemMailPassword) {

		String messageText = "您好! 親愛的 " + member.getMemName() //
		+ " ，\n 提醒您今天參與活動: " + activity.getAvtName()//
		+ " ，\n 即將開始囉。 "//
		+ " \n\n 活動結束後也要麻煩您抽空填寫留言及評分唷！" //
		+ " \n http://localhost:8080/badminton/activity/search/detail?activityId=" + activity.getAvtNo()//

				+ "\n\n\n" + " 基分撲蝶會活動網站  敬啟  \n\n(此為系統自動通知信，請勿直接回信！)";//

		MailService.sendMail(member.getMemMail(), //
				"活動開始通知："+activity.getAvtName(), //
				messageText, //
				systemMail, //
				systemMailPassword);//
	}
}
