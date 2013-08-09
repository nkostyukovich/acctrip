package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.models.Event;

public class EventDataSource {

	private SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	private Context ctx;

	private final static String SELECT_OP_QUERY = "select ev." + EventAccContract.Event._ID + " "
			+ EventAccContract.Event._ID + ", " + "ev." + EventAccContract.Event.NAME + " "
			+ EventAccContract.Event.NAME + ", " + "ev." + EventAccContract.Event.DESC + " "
			+ EventAccContract.Event.DESC + ", " + "ev." + EventAccContract.Event.PRIMARY_CURRENCY_ID + " "
			+ EventAccContract.Event.PRIMARY_CURRENCY_ID + ", " + "curr." + EventAccContract.Currency.NAME + " "
			+ EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_NAME + ", " + "curr." + EventAccContract.Currency.CODE
			+ " " + EventAccContract.Event.ALIAS_PRIMARY_CURRENCY_CODE + " from " + EventAccContract.Event.TABLE_NAME
			+ " ev left join " + EventAccContract.Currency.TABLE_NAME + " curr on ( ev."
			+ EventAccContract.Event.PRIMARY_CURRENCY_ID + "=curr." + EventAccContract.Currency._ID + ") ";

	private String[] selectedColumns = { EventAccContract.Event._ID, EventAccContract.Event.NAME,
			EventAccContract.Event.DESC, EventAccContract.Event.PRIMARY_CURRENCY_ID };

	public EventDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
		this.ctx = ctx;
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public long insert(String name, String desc, long primaryCurrencyId) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Event.NAME, name);
			values.put(EventAccContract.Event.DESC, desc);
			values.put(EventAccContract.Event.PRIMARY_CURRENCY_ID, primaryCurrencyId);
			return database.insert(EventAccContract.Event.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	public Event update(Long id, String name, String desc, long primaryCurrencyId) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Event.NAME, name);
			values.put(EventAccContract.Event.DESC, desc);
			values.put(EventAccContract.Event.PRIMARY_CURRENCY_ID, primaryCurrencyId);
			String whereClause = EventAccContract.Event._ID + " = ?";
			database.update(EventAccContract.Event.TABLE_NAME, values, whereClause, new String[] { Long.toString(id) });
			return getEventById(id);
		} finally {
			close();
		}
	}

	public Event getEventById(long id) {
		open();
		try {
			String whereBatch = " where ev." + EventAccContract.Event._ID + " = ?";
			Cursor c = database.rawQuery(SELECT_OP_QUERY + whereBatch, new String[] { Long.toString(id) });
			Event event = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				event = ConvertUtils.cursorToEvent(c);
			}
			c.close();
			return event;
		} finally {
			close();
		}
	}

	public List<Event> getEventList() {
		open();
		try {
			List<Event> result = new ArrayList<Event>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " order by " + EventAccContract.Event._ID + " DESC", null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToEvent(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

	public long delete(Long id) {
		open();
		long c = -1;
		try {
			String whereClause = EventAccContract.Event._ID + " = ?";
			c = database.delete(EventAccContract.Event.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
		new OperationDataSource(ctx).deleteByEventId(id);
		return c;
	}

}
