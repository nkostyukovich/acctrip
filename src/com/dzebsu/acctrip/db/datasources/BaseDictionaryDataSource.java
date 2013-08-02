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
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;

public abstract class BaseDictionaryDataSource<T extends BaseDictionary> implements IDictionaryDataSource<T> {

	protected SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	// TODO maybe need use singleton pattern
	protected BaseDictionaryDataSource() {
	}

	protected BaseDictionaryDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	protected abstract String getEntityAliasId();

	public List<Operation> getOperationListByEntityId(long id) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(OperationDataSource.getSelectConjunctionTableQuery() + " where "
					+ getEntityAliasId() + " = " + id + " order by " + EventAccContract.Operation._ID + " desc", null);
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

	@Override
	public void setContext(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

}
