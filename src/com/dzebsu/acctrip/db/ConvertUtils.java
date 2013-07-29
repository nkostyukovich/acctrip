package com.dzebsu.acctrip.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.database.Cursor;

import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.OperationType;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class ConvertUtils {

	public static Event cursorToEvent(Cursor c) {
		long id = c.getLong(c.getColumnIndex(EventAccContract.Event._ID));
		String name = c.getString(c.getColumnIndex(EventAccContract.Event.NAME));
		String desc = c.getString(c.getColumnIndex(EventAccContract.Event.DESC));
		return new Event(id, name, desc);
	}

	public static Category cursorToCategory(Cursor c) {
		long id = c.getLong(c.getColumnIndex(EventAccContract.Category._ID));
		String name = c.getString(c.getColumnIndex(EventAccContract.Event.NAME));
		return new Category(id, name);
	}

	public static Currency cursorToCurrency(Cursor c) {
		long id = c.getLong(c.getColumnIndex(EventAccContract.Currency._ID));
		String name = c.getString(c.getColumnIndex(EventAccContract.Currency.NAME));
		String code = c.getString(c.getColumnIndex(EventAccContract.Currency.CODE));
		return new Currency(id, name, code);
	}

	public static Place cursorToPlace(Cursor c) {
		long id = c.getLong(c.getColumnIndex(EventAccContract.Place._ID));
		String name = c.getString(c.getColumnIndex(EventAccContract.Place.NAME));
		return new Place(id, name);
	}

	@SuppressWarnings("unused")
	public static Operation cursorToOperation(Cursor c) {
		Operation op = new Operation();
		op.setId(c.getLong(c.getColumnIndex(EventAccContract.Operation._ID)));
		
		op.setDesc(c.getString(c.getColumnIndex(EventAccContract.Operation.DESC)));
		
		
		
		
		if(!c.isNull(c.getColumnIndex(EventAccContract.Category.ALIAS_ID))){
		op.setCategory(new Category(c.getLong(c.getColumnIndex(EventAccContract.Category.ALIAS_ID)), c.getString(c
				.getColumnIndex(EventAccContract.Category.ALIAS_NAME))));}
		else{
			op.setCategory(null);
		}
		
		op.setType(c.getString(c.getColumnIndex(EventAccContract.Operation.TYPE)).equals("EXPENSE")?OperationType.EXPENSE:OperationType.INCOME);
		
		op.setValue(c.getDouble(c.getColumnIndex(EventAccContract.Operation.VALUE)));
		if(!c.isNull(c.getColumnIndex(EventAccContract.Currency.ALIAS_ID))){
		op.setCurrency(new Currency(c.getLong(c.getColumnIndex(EventAccContract.Currency.ALIAS_ID)), c
				.getString(c.getColumnIndex(EventAccContract.Currency.ALIAS_NAME)), c
				.getString(c.getColumnIndex(EventAccContract.Currency.ALIAS_CODE))));}
		else{
			op.setCurrency(null);
		}
		
		if(!c.isNull(c.getColumnIndex(EventAccContract.Operation.DATE))){
		op.setDate(convertLongToDate(c.getLong(c.getColumnIndex(EventAccContract.Operation.DATE))));}
		else{
			op.setDate(null);
		}
		
		op.setEvent(new Event(c.getLong(c.getColumnIndex(EventAccContract.Event.ALIAS_ID))));
		
		if (!c.isNull(c.getColumnIndex(EventAccContract.Place.ALIAS_ID))) {
			op.setPlace(new Place(c.getLong(c.getColumnIndex(EventAccContract.Place.ALIAS_ID)), c.getString(c
					.getColumnIndex(EventAccContract.Place.ALIAS_NAME))));
			
		}else{
			op.setPlace(null);
		}
		return op;
	}

	private static Date convertLongToDate(long value) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(value);
		return cal.getTime();
	}

	public static long convertDateToLong(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal.getTime().getTime();
	}
}
