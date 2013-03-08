package com.dzebsu.acctrip.db;

import android.database.Cursor;

import com.dzebsu.acctrip.models.Event;

public class ConvertUtils {
	
	public static Event cursorToEvent(Cursor c) {
        long id = c.getLong(c.getColumnIndex(EventAccContract.Event._ID));
        String name = c.getString(c.getColumnIndex(EventAccContract.Event.NAME));
        String desc = c.getString(c.getColumnIndex(EventAccContract.Event.DESC));        
        return new Event(id, name, desc);

	}

}
