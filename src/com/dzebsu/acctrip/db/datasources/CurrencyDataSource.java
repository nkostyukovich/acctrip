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
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class CurrencyDataSource {

	private SQLiteDatabase database;
	private EventAccDbHelper dbHelper;

	private String[] selectedColumns = { EventAccContract.Currency._ID, EventAccContract.Currency.NAME,
			EventAccContract.Currency.CODE };

	public CurrencyDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public long insert(String name, String code) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Currency.NAME, name);
			values.put(EventAccContract.Currency.CODE, code);
			return database.insert(EventAccContract.Currency.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	public Currency update(Long id, String name, String code) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Currency.NAME, name);
			values.put(EventAccContract.Currency.CODE, code);
			String whereClause = EventAccContract.Currency._ID + " = ?";
			database.update(EventAccContract.Currency.TABLE_NAME, values, whereClause,
					new String[] { Long.toString(id) });
			return getCurrencyById(id);
		} finally {
			close();
		}
	}

	public Currency getCurrencyById(long id) {
		open();
		try{
			String whereBatch = EventAccContract.Currency._ID + " = ?";
			Cursor c = database.query(EventAccContract.Currency.TABLE_NAME, selectedColumns, whereBatch,
					new String[] { Long.toString(id) }, null, null, null);
			Currency cur = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				cur = ConvertUtils.cursorToCurrency(c);
			}
			c.close();
			return cur;
		}finally{
			close();
		}
	}

	public List<Currency> getCurrencyList() {
		open();
		try {
			List<Currency> result = new ArrayList<Currency>();
			Cursor c = database.query(EventAccContract.Currency.TABLE_NAME, selectedColumns, null, null, null, null,
					EventAccContract.Currency._ID + " DESC");
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToCurrency(c));
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
		try {
			String whereClause = EventAccContract.Currency._ID + " = ?";
			return database.delete(EventAccContract.Currency.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

}
