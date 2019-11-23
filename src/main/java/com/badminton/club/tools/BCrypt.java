package com.badminton.club.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * BCrypt加密及驗證
 */
public class BCrypt {
	/**
	 * 和已存在的BCrypt密碼驗證是否相同
	 */
	public static boolean checkOriginalPassword(String password,String bcryptPassword){
		String encodePassword=bcryptPassword.replace("{bcrypt}", "");
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean deCodePassword = passwordEncoder.matches(password,encodePassword);
		return deCodePassword;
	}
	
	/**
	 * 取得加密密碼
	 */
	public static String getDecodePassword(String password){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return "{bcrypt}"+hashedPassword;
	}
	
	public static void main(String[] args) {
		String password = "1qazxc";
		System.out.println("密碼:"+password);
		
		String enCode=getDecodePassword(password);
		System.out.println("加密:"+enCode);

		System.out.println("比對:"+checkOriginalPassword(password,enCode));

	}
}
