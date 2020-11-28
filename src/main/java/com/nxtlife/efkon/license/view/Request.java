package com.nxtlife.efkon.license.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nxtlife.efkon.license.util.DateUtil;
import com.nxtlife.efkon.license.util.LongObfuscator;

public interface Request {

	default Long unmask(final Long number) {
		return number != null ? LongObfuscator.INSTANCE.unobfuscate(number) : null;
	}

	default Date parseDate(String string, String dateFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		return sdf.parse(string);
	}

	default Date parseDate(String string) throws ParseException {
		return parseDate(string, DateUtil.defaultFormat);
	}
}