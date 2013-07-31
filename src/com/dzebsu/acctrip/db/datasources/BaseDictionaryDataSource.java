package com.dzebsu.acctrip.db.datasources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.EventAccDbHelper;
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
