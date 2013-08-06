package com.dzebsu.acctrip.date.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.text.format.DateUtils;

public class DateFormatter {

	public static Date convertLongToDate(long value) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(value);
		return cal.getTime();
	}

	public static long convertDateToLong(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal.getTime().getTime();
	}

	public static String formatDate(Context cxt, Date date) {
		return DateUtils.formatDateTime(cxt, convertDateToLong(date), DateUtils.FORMAT_ABBREV_WEEKDAY
				| DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
	}

	public static String formatDateAndTime(Context cxt, Date date) {
		return DateUtils.formatDateTime(cxt, convertDateToLong(date), DateUtils.FORMAT_SHOW_TIME
				| DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_SHOW_WEEKDAY);
	}

	public static String formatTime(Context cxt, Date date) {
		return DateUtils.formatDateTime(cxt, convertDateToLong(date), DateUtils.FORMAT_SHOW_TIME);
	}

}
