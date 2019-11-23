package com.badminton.club.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 信件傳送 服務
 */
public class MailService {

	private static final Logger log = LoggerFactory.getLogger(MailService.class);

	// 設定傳送郵件:至收信人的Email信箱,Email主旨,Email內容
	public static void sendMail(String to, String subject, String messageText, String systemMail,
			String systemMailPassword) {

		try {
			// 設定使用SSL連線至 Gmail smtp Server
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			// ●設定 gmail 的帳號 & 密碼 (將藉由你的Gmail來傳送Email)
			// ●須將myGmail的【安全性較低的應用程式存取權】打開
			final String myGmail = systemMail;
			final String myGmail_password = systemMailPassword;
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(myGmail, myGmail_password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myGmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// 設定信中的主旨
			message.setSubject(subject);
			// 設定信中的內容
			message.setText(messageText);

			Transport.send(message);
			log.info("傳送狀態:{},至收信人的Email信箱:{},Email主旨:{}", "成功", to, subject);

		} catch (MessagingException e) {
			log.debug("傳送狀態:{}", e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {

		String to = "xujiaqi38@gmail.com";

		String subject = "密碼通知";

		String ch_name = "peter1";
		String passRandom = "111";
		String messageText = "Hello! " + ch_name + " 請謹記此密碼: " + passRandom + "\n" + " (已經啟用)";

		MailService mailService = new MailService();
		mailService.sendMail(to, subject, messageText, "hsuj1047@gmail.com", "clan380clue746");

	}

}
