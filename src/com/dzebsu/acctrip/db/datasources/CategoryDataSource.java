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
import com.dzebsu.acctrip.models.Category;

public class CategoryDataSource {

	private SQLiteDatabase database;
	private EventAccDbHelper dbHelper;

	private String[] selectedColumns = { EventAccContract.Category._ID, EventAccContract.Category.NAME };

	public CategoryDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}
	
	public long insert(String name) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Category.NAME, name);
			return database.insert(EventAccContract.Category.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	public Category update(Long id, String name) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Category.NAME, name);
			String whereClause = EventAccContract.Category._ID + " = ?";
			database.update(EventAccContract.Category.TABLE_NAME, values, whereClause, new String[] { Long.toString(id) });
			return getCategoryById(id);
		} finally {
			close();
		}
	}

	public Category getCategoryById(long id) {
		String whereBatch = EventAccContract.Category._ID + " = ?";
		Cursor c = database.query(EventAccContract.Category.TABLE_NAME, selectedColumns, whereBatch, new String[] { Long.toString(id) }, null, null, null);
		Category category = null;
		if (c.getCount() > 0) {
			c.moveToFirst();
			category = ConvertUtils.cursorToCategory(c);
		}
		c.close();
		return category;
	}

	public List<Category> getCategoryList() {
		open();
		try {
			List<Category> result = new ArrayList<Category>();
			Cursor c = database.query(EventAccContract.Category.TABLE_NAME, selectedColumns, null, null, null, null, EventAccContract.Category._ID + " DESC");
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToCategory(c));
				c.moveToNext();
			}
			c.close();
			return result;
		} finally {
			close();
		}
	}

}
