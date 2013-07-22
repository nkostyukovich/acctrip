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

	private String[] selectedColumns = { EventAccContract.Event._ID, EventAccContract.Event.NAME, EventAccContract.Event.DESC };

	public EventDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}
	
	public long insert(String name, String desc) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Event.NAME, name);
			values.put(EventAccContract.Event.DESC, desc);		 
			return database.insert(EventAccContract.Event.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}
	
	
	public Event update(Long id, String name, String desc) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Event.NAME, name);
			values.put(EventAccContract.Event.DESC, desc);
			String whereClause = EventAccContract.Event._ID + " = ?";
			database.update(EventAccContract.Event.TABLE_NAME, values, whereClause, new String[] { Long.toString(id) });
			return getEventById(id);
		} finally {
			close();
		}
	}

	public Event getEventById(long id) {
		open();
		try{
			String whereBatch = EventAccContract.Event._ID + " = ?";
			Cursor c = database.query(EventAccContract.Event.TABLE_NAME, selectedColumns, whereBatch, new String[] { Long.toString(id) }, null, null, null);
			Event event = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				event = ConvertUtils.cursorToEvent(c);
			}
			c.close();
			return event;
		}finally{
			close();
		}
	}

	public List<Event> getEventList() {
		open();
		try {
			List<Event> result = new ArrayList<Event>();
			Cursor c = database.query(EventAccContract.Event.TABLE_NAME, selectedColumns, null, null, null, null, EventAccContract.Event._ID + " DESC");
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

}
