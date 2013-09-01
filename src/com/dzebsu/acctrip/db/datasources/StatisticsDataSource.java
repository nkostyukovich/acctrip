package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.models.Operation;

public class StatisticsDataSource {

	private SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	private final static String SELECT_OP_QUERY = "select op." + EventAccContract.Operation._ID + " "
			+ EventAccContract.Operation._ID + ", " + "cat." + EventAccContract.Category._ID + " "
			+ EventAccContract.Category.ALIAS_ID + ", " + "cat." + EventAccContract.Category.NAME + " "
			+ EventAccContract.Category.ALIAS_NAME + ", " + "op." + EventAccContract.Operation.DESC + " "
			+ EventAccContract.Operation.DESC + ", " + "op." + EventAccContract.Operation.TYPE + " "
			+ EventAccContract.Operation.TYPE + ", " + "op." + EventAccContract.Operation.VALUE + " "
			+ EventAccContract.Operation.VALUE + ", " + "cur." + EventAccContract.Currency._ID + " "
			+ EventAccContract.Currency.ALIAS_ID + ", " + "cur." + EventAccContract.Currency.CODE + " "
			+ EventAccContract.Currency.ALIAS_CODE + ", " +

			"cur." + EventAccContract.Currency.NAME + " " + EventAccContract.Currency.ALIAS_NAME + ", " + "op."
			+ EventAccContract.Operation.DATE + " " + EventAccContract.Operation.DATE + ", " + "ev."
			+ EventAccContract.Event._ID + " " + EventAccContract.Event.ALIAS_ID + ", " + "ev."
			+ EventAccContract.Event.NAME + " " + EventAccContract.Event.ALIAS_NAME + ", " + "ev."
			+ EventAccContract.Event.DESC + " " + EventAccContract.Event.ALIAS_DESC + ", " + "ev."
			+ EventAccContract.Event.PRIMARY_CURRENCY_ID + " " + EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_ID
			+ ", " + "pl." + EventAccContract.Place._ID + " " + EventAccContract.Place.ALIAS_ID + ", " + "pl."
			+ EventAccContract.Place.NAME + " " + EventAccContract.Place.ALIAS_NAME
			+ " from operation op left join event ev on (op.eventId = ev._id) "
			+ " left join category cat on (op.categoryId = cat._id) "
			+ " left join currency cur on (op.currencyId = cur._id) " + " left join place pl on (op.placeId = pl._id) ";

	private Context ctx;

	public static String getSelectConjunctionTableQuery() {
		return SELECT_OP_QUERY;
	}

	public StatisticsDataSource(Context ctx) {
		this.ctx = ctx;
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public List<Operation> getFilteredOperationList(StatisticsQueryParams params) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + params.getWhereQuery(ctx) + " order by "
					+ EventAccContract.Operation._ID + " desc", null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToOperation(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public List<Long> getEventCurrencies(long eventId) {
		open();
		try {
			List<Long> result = new ArrayList<Long>();
			Cursor c = database.rawQuery("select " + EventAccContract.Operation.CURRENCY_ID + " from "
					+ EventAccContract.Operation.TABLE_NAME + " where " + EventAccContract.Operation.EVENT_ID + "="
					+ eventId + " group by " + EventAccContract.Operation.CURRENCY_ID, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToLong(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public List<Long> getEventPlaces(long eventId) {
		open();
		try {
			List<Long> result = new ArrayList<Long>();
			Cursor c = database.rawQuery("select " + EventAccContract.Operation.PLACE_ID + " from "
					+ EventAccContract.Operation.TABLE_NAME + " where " + EventAccContract.Operation.EVENT_ID + "="
					+ eventId + " group by " + EventAccContract.Operation.PLACE_ID, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToLong(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public List<Long> getEventCategories(long eventId) {
		open();
		try {
			List<Long> result = new ArrayList<Long>();
			Cursor c = database.rawQuery("select " + EventAccContract.Operation.CATEGORY_ID + " from "
					+ EventAccContract.Operation.TABLE_NAME + " where " + EventAccContract.Operation.EVENT_ID + "="
					+ eventId + " group by " + EventAccContract.Operation.CATEGORY_ID, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToLong(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public List<String> getEventDays(long eventId) {
		open();
		try {
			List<String> result = new ArrayList<String>();
			Cursor c = database.rawQuery("select strftime('%d-%m-%Y'," + EventAccContract.Operation.DATE
					+ "/1000,'unixepoch') from " + EventAccContract.Operation.TABLE_NAME + " where "
					+ EventAccContract.Operation.EVENT_ID + "=" + eventId + " group by strftime('%d-%m-%Y',"
					+ EventAccContract.Operation.DATE + "/1000,'unixepoch')", null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToString(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

}
