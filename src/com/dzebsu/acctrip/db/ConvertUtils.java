package com.dzebsu.acctrip.db;

import android.database.Cursor;

import com.dzebsu.acctrip.date.utils.DateFormatter;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.OperationType;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class ConvertUtils {

	public static Event cursorToEvent(Cursor c) {
		// XXX without info about primaryCurrency
		Event ev = new Event();
		ev.setId(c.getLong(c.getColumnIndex(EventAccContract.Event._ID)));
		ev.setName(c.getString(c.getColumnIndex(EventAccContract.Event.NAME)));
		ev.setDesc(c.getString(c.getColumnIndex(EventAccContract.Event.DESC)));
		ev.setPrimaryCurrency(new Currency(c.getLong(c.getColumnIndex(EventAccContract.Event.PRIMARY_CURRENCY_ID)), c
				.getString(c.getColumnIndex(EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_NAME)), c.getString(c
				.getColumnIndex(EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_CODE))));

		return ev;
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

	public static Operation cursorToOperation(Cursor c) {
		Operation op = new Operation();
		op.setId(c.getLong(c.getColumnIndex(EventAccContract.Operation._ID)));
		op.setDesc(c.getString(c.getColumnIndex(EventAccContract.Operation.DESC)));
		op.setCategory(new Category(c.getLong(c.getColumnIndex(EventAccContract.Category.ALIAS_ID)), c.getString(c
				.getColumnIndex(EventAccContract.Category.ALIAS_NAME))));
		op.setType(c.getString(c.getColumnIndex(EventAccContract.Operation.TYPE)).equals("EXPENSE") ? OperationType.EXPENSE
				: OperationType.INCOME);
		op.setValue(c.getDouble(c.getColumnIndex(EventAccContract.Operation.VALUE)));
		op.setCurrency(new Currency(c.getLong(c.getColumnIndex(EventAccContract.Currency.ALIAS_ID)), c.getString(c
				.getColumnIndex(EventAccContract.Currency.ALIAS_NAME)), c.getString(c
				.getColumnIndex(EventAccContract.Currency.ALIAS_CODE))));
		op.setDate(DateFormatter.convertLongToDate(c.getLong(c.getColumnIndex(EventAccContract.Operation.DATE))));

		op.setEvent(new Event(c.getLong(c.getColumnIndex(EventAccContract.Event.ALIAS_ID)), c.getString(c
				.getColumnIndex(EventAccContract.Event.ALIAS_NAME)), c.getString(c
				.getColumnIndex(EventAccContract.Event.ALIAS_DESC)), new Currency(c.getLong(c
				.getColumnIndex(EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_ID)))));

		op.setPlace(new Place(c.getLong(c.getColumnIndex(EventAccContract.Place.ALIAS_ID)), c.getString(c
				.getColumnIndex(EventAccContract.Place.ALIAS_NAME))));
		return op;
	}

	public static CurrencyPair cursorToCurrencyPair(Cursor c) {
		CurrencyPair cp = new CurrencyPair();
		cp.setEventId(c.getLong(c.getColumnIndex(EventAccContract.CurrencyPair.EVENT_ID)));
		cp.setFirstCurrency(new Currency(c.getLong(c.getColumnIndex(EventAccContract.CurrencyPair.FIRST_CURRENCY_ID)),
				c.getString(c.getColumnIndex(EventAccContract.CurrencyPair.ALIAS_FIRST_NAME)), c.getString(c
						.getColumnIndex(EventAccContract.CurrencyPair.ALIAS_FIRST_CODE))));
		cp.setSecondCurrency(new Currency(
				c.getLong(c.getColumnIndex(EventAccContract.CurrencyPair.SECOND_CURRENCY_ID)), c.getString(c
						.getColumnIndex(EventAccContract.CurrencyPair.ALIAS_SECOND_NAME)), c.getString(c
						.getColumnIndex(EventAccContract.CurrencyPair.ALIAS_SECOND_CODE))));
		cp.setRate(c.getLong(c.getColumnIndex(EventAccContract.CurrencyPair.RATE)));
		return cp;
	}
}
