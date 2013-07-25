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
import com.dzebsu.acctrip.models.Place;

public class PlaceDataSource {

	private SQLiteDatabase database;
	private EventAccDbHelper dbHelper;

	private String[] selectedColumns = { EventAccContract.Place._ID, EventAccContract.Place.NAME };

	public PlaceDataSource(Context ctx) {
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
			values.put(EventAccContract.Place.NAME, name);
			return database.insert(EventAccContract.Place.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	public Place update(Long id, String name) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Place.NAME, name);
			String whereClause = EventAccContract.Category._ID + " = ?";
			database.update(EventAccContract.Place.TABLE_NAME, values, whereClause, new String[] { Long.toString(id) });
			return getPlaceById(id);
		} finally {
			close();
		}
	}

	public Place getPlaceById(long id) {
		open();
		try{
			String whereBatch = EventAccContract.Place._ID + " = ?";
			Cursor c = database.query(EventAccContract.Place.TABLE_NAME, selectedColumns, whereBatch,
					new String[] { Long.toString(id) }, null, null, null);
			Place place = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				place = ConvertUtils.cursorToPlace(c);
			}
			c.close();
			return place;
		}finally{
			close();
		}
	}

	public List<Place> getPlaceList() {
		open();
		try {
			List<Place> result = new ArrayList<Place>();
			Cursor c = database.query(EventAccContract.Place.TABLE_NAME, selectedColumns, null, null, null, null,
					EventAccContract.Place._ID + " DESC");
			c.moveToFirst();
			while (!c.isAfterLast()) {
				result.add(ConvertUtils.cursorToPlace(c));
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
			String whereClause = EventAccContract.Place._ID + " = ?";
			return database.delete(EventAccContract.Place.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}
}
