package com.nxtlife.efkon.license.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static final String defaultFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String defaultTimeFormat = "HH:mm:ss";
	public static final String defaultDateFormat = "yyyy-MM-dd";
	public static final TimeZone defaultTimeZone = TimeZone.getTimeZone("IST");

	public static String today() {
		DateFormat df = new SimpleDateFormat(defaultFormat);
		df.setTimeZone(defaultTimeZone);
		df.setLenient(false);
		return df.format(new Date());
	}

	public static String getTime(long time) {
		DateFormat df = new SimpleDateFormat(defaultTimeFormat);
		df.setLenient(false);
		return df.format(new Date(time));
	}

	public static String getDate(long time) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		df.setLenient(false);
		return df.format(new Date(time));
	}

	public static String get(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(defaultTimeZone);
		df.setLenient(false);
		return df.format(date);
	}

	public static String setDateFormatForXml(String inputDate) throws ParseException {
		DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat xmlFormat = new SimpleDateFormat("dd/MM");
		Date date = originalFormat.parse(inputDate);
		String formattedDate = xmlFormat.format(date);
		return formattedDate;
	}
}
