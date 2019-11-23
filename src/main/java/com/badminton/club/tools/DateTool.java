package com.badminton.club.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 日期轉檔工具
 */
public class DateTool {
	/**
	 * String 轉 util.Date
	 */
	public static Date getDate(String dateStr){
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	/**
	 * util.Date 轉 String
	 */
	public static String dateToString(Date toDate){
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(toDate);
		return str;
	}
	/**
	 * sql.timestamp 轉 String
	 */
	public static String timestampToString(Timestamp timestamp){
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sdf.format(timestamp);
		return str;
	}
	
	/**
	 * String 轉 sql.timestamp
	 */
	public static Timestamp DateLocalTotimestamp(String dateLocal){
		dateLocal=dateLocal.substring(0,10)+" "+dateLocal.substring(11,16)+":00";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts = Timestamp.valueOf(dateLocal);
		return ts;
	}
	/**
	 * sql.timestamp 轉 String
	 */
	public static String timestampToStringForActivity(Timestamp timestamp){
		String dateTime=timestampToString(timestamp);
		dateTime=dateTime.substring(0,10)+"T"+dateTime.substring(11,16);
		return dateTime;		
	}
}
