package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.models.dictionaries.Category;

public class CategoryDataSource extends BaseDictionaryDataSource<Category> {

	private String[] selectedColumns = { EventAccContract.Category._ID, EventAccContract.Category.NAME };

	public CategoryDataSource() {
		super();
	}

	public CategoryDataSource(Context ctx) {
		super(ctx);
	}

	@Override
	public long insertEntity(Category entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Category.NAME, entity.getName());
			return database.insert(EventAccContract.Category.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	@Override
	public void updateEntity(Category entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Category.NAME, entity.getName());
			String whereClause = EventAccContract.Category._ID + " = ?";
			database.update(EventAccContract.Category.TABLE_NAME, values, whereClause, new String[] { entity.getId()
					.toString() });
		} finally {
			close();
		}
	}

	@Override
	public Category getEntityById(long id) {
		open();
		try {
			String whereBatch = EventAccContract.Category._ID + " = ?";
			Cursor c = database.query(EventAccContract.Category.TABLE_NAME, selectedColumns, whereBatch,
					new String[] { Long.toString(id) }, null, null, null);
			Category category = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				category = ConvertUtils.cursorToCategory(c);
			}
			c.close();
			return category;
		} finally {
			close();
		}
	}

	@Override
	public List<Category> getEntityList() {
		open();
		try {
			List<Category> result = new ArrayList<Category>();
			Cursor c = database.query(EventAccContract.Category.TABLE_NAME, selectedColumns, null, null, null, null,
					EventAccContract.Category._ID + " DESC");
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

	@Override
	public void deleteEntity(long id) {
		open();
		try {
			String whereClause = EventAccContract.Category._ID + " = ?";
			database.delete(EventAccContract.Category.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

	@Override
	protected String getEntityAliasId() {

		return EventAccContract.Category.ALIAS_ID;
	}

}
