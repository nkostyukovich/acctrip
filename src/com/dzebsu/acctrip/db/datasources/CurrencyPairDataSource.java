package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.models.CurrencyPair;

public class CurrencyPairDataSource {

	private SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	private final static String SELECT_OP_QUERY = "select c_pairs." + EventAccContract.CurrencyPair.EVENT_ID + " "
			+ EventAccContract.CurrencyPair.EVENT_ID + ", " + "c_pairs."
			+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " " + EventAccContract.CurrencyPair.SECOND_CURRENCY_ID
			+ ", " + "c_pairs." + EventAccContract.CurrencyPair.RATE + " " + EventAccContract.CurrencyPair.RATE + ", "
			+ "second_cur." + EventAccContract.Currency.NAME + " " + EventAccContract.CurrencyPair.ALIAS_SECOND_NAME
			+ ", " + "second_cur." + EventAccContract.Currency.CODE
			+ " "
			+ EventAccContract.CurrencyPair.ALIAS_SECOND_CODE
			/*
			 * + "ev." + EventAccContract.Event._ID + " " +
			 * EventAccContract.Event.ALIAS_ID + ", " + "ev." +
			 * EventAccContract.Event.NAME + " " +
			 * EventAccContract.Event.ALIAS_NAME + ", " + "ev." +
			 * EventAccContract.Event.DESC + " " +
			 * EventAccContract.Event.ALIAS_DESC + ", ev." +
			 * EventAccContract.Event.PRIMARY_CURRENCY_ID + " " +
			 * EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_ID
			 */
			+ " from currency_pair c_pairs "
			// +"left join "
			// + EventAccContract.Event.TABLE_NAME + " ev on (c_pairs." +
			// EventAccContract.CurrencyPair.EVENT_ID + " = ev._id) "
			+ "left join " + EventAccContract.Currency.TABLE_NAME + " second_cur on (c_pairs."
			+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " = second_cur._id) ";

	public static String getSelectConjunctionTableQuery() {
		return SELECT_OP_QUERY;
	}

	public CurrencyPairDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public long insert(long eventId, long secondCurrencyId) {
		return insert(eventId, secondCurrencyId, 1.00);
	}

	public long insert(long eventId, long secondCurrencyId, double rate) {
		open();
		try {
			ContentValues values = new ContentValues();

			values.put(EventAccContract.CurrencyPair.EVENT_ID, eventId);
			values.put(EventAccContract.CurrencyPair.SECOND_CURRENCY_ID, secondCurrencyId);
			values.put(EventAccContract.CurrencyPair.RATE, rate);

			long ir = database.insert(EventAccContract.CurrencyPair.TABLE_NAME, null, values);
			return ir;
		} finally {
			close();
		}
	}

	public CurrencyPair update(long eventId, long secondCurrencyId, double rate) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.CurrencyPair.EVENT_ID, eventId);
			values.put(EventAccContract.CurrencyPair.SECOND_CURRENCY_ID, secondCurrencyId);
			values.put(EventAccContract.CurrencyPair.RATE, rate);
			String whereClause = EventAccContract.CurrencyPair.EVENT_ID + " = ? AND "
					+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " = ?";
			database.update(EventAccContract.CurrencyPair.TABLE_NAME, values, whereClause, new String[] {
					Long.toString(eventId), Long.toString(secondCurrencyId) });
			return getCurrencyPairByValues(eventId, secondCurrencyId);
		} finally {
			close();
		}
	}

	public void updateRatesBunch(List<CurrencyPair> cps, double rates[]) {
		for (int i = 0; i < cps.size(); i++) {
			CurrencyPair cp = cps.get(i);
			update(cp.getEventId(), cp.getSecondCurrency().getId(), rates[i]);
		}
	}

	public long deleteByEventId(long id) {
		open();
		try {
			String whereClause = EventAccContract.CurrencyPair.EVENT_ID + " = ?";
			return database.delete(EventAccContract.CurrencyPair.TABLE_NAME, whereClause, new String[] { Long
					.toString(id) });
		} finally {
			close();
		}
	}

	public long deleteCurrencyPair(CurrencyPair cp) {
		return deleteEntry(cp.getEventId(), cp.getSecondCurrency().getId());
	}

	public long deleteByValues(long eventId, long secondCurrencyId) {
		return deleteEntry(eventId, secondCurrencyId);
	}

	private long deleteEntry(long eventId, long secondCurrencyId) {
		open();
		try {
			String whereClause = EventAccContract.CurrencyPair.EVENT_ID + " = ? AND "

			+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " = ?";
			return database.delete(EventAccContract.CurrencyPair.TABLE_NAME, whereClause, new String[] {
					Long.toString(eventId), Long.toString(secondCurrencyId) });
		} finally {
			close();
		}
	}

	public CurrencyPair getCurrencyPairByValues(long eventId, long secondCurrencyId) {
		open();
		try {
			String whereBatch = "where " + EventAccContract.CurrencyPair.EVENT_ID + " = ? AND "

			+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " = ?";
			Cursor c = database.rawQuery(SELECT_OP_QUERY + whereBatch, new String[] { Long.toString(eventId),
					Long.toString(secondCurrencyId) });
			CurrencyPair cp = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				cp = ConvertUtils.cursorToCurrencyPair(c);
			}
			c.close();
			return cp;
		} finally {
			close();
		}
	}

	public long getCountByEventId(long eventId) {
		open();
		try {
			Cursor c = database.rawQuery("select count(eventId) evcount from "
					+ EventAccContract.CurrencyPair.TABLE_NAME + " where eventId=?", new String[] { Long
					.toString(eventId) });
			c.moveToFirst();
			long cnt = Long.parseLong(c.getString(c.getColumnIndex("evcount")));
			c.close();
			return cnt;
		} finally {
			close();
		}
	}

	public List<CurrencyPair> getCurrencyPairListByEventId(long eventId) {
		open();
		try {
			List<CurrencyPair> result = new ArrayList<CurrencyPair>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where " + EventAccContract.CurrencyPair.EVENT_ID + " = "
					+ eventId, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToCurrencyPair(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public void updateRatesBunchToOneValueByEventId(Long eventId, double value) {
		double rates[];
		List<CurrencyPair> cps = getCurrencyPairListByEventId(eventId);
		rates = new double[cps.size()];
		Arrays.fill(rates, value);
		updateRatesBunch(cps, rates);

	}

	public void updateRatesBunchToDefaultValueByEventId(Long eventId) {
		updateRatesBunchToOneValueByEventId(eventId, 1.00);
	}
}
