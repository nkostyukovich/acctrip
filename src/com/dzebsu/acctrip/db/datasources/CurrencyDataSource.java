package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class CurrencyDataSource extends BaseDictionaryDataSource<Currency> {

	private String[] selectedColumns = { EventAccContract.Currency._ID, EventAccContract.Currency.NAME,
			EventAccContract.Currency.CODE };

	public CurrencyDataSource() {
		super();
	}

	public CurrencyDataSource(Context ctx) {
		super(ctx);
	}

	@Override
	public long insertEntity(Currency entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Currency.NAME, entity.getName());
			values.put(EventAccContract.Currency.CODE, entity.getCode());
			return database.insert(EventAccContract.Currency.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	@Override
	public void updateEntity(Currency entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Currency.NAME, entity.getName());
			values.put(EventAccContract.Currency.CODE, entity.getCode());
			String whereClause = EventAccContract.Currency._ID + " = ?";
			database.update(EventAccContract.Currency.TABLE_NAME, values, whereClause, new String[] { entity.getId()
					.toString() });
		} finally {
			close();
		}
	}

	@Override
	public Currency getEntityById(long id) {
		open();
		try {
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
		} finally {
			close();
		}
	}

	@Override
	public List<Currency> getEntityList() {
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

	@Override
	public void deleteEntity(long id) {
		open();
		try {
			String whereClause = EventAccContract.Currency._ID + " = ?";
			database.delete(EventAccContract.Currency.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

	@Override
	protected String getEntityAliasId() {

		return EventAccContract.Currency.ALIAS_ID;
	}

}
