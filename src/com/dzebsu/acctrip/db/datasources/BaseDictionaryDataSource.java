package com.dzebsu.acctrip.db.datasources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.EventAccDbHelper;

public abstract class BaseDictionaryDataSource {

	protected SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	// TODO maybe need use singleton pattern
	protected BaseDictionaryDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

}
