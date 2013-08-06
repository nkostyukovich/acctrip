package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class PlaceDataSource extends BaseDictionaryDataSource<Place> {

	private String[] selectedColumns = { EventAccContract.Place._ID, EventAccContract.Place.NAME };

	public PlaceDataSource() {
		super();
	}

	public PlaceDataSource(Context ctx) {
		super(ctx);
	}

	@Override
	public long insertEntity(Place entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Place.NAME, entity.getName());
			return database.insert(EventAccContract.Place.TABLE_NAME, null, values);
		} finally {
			close();
		}
	}

	@Override
	public void updateEntity(Place entity) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(EventAccContract.Place.NAME, entity.getName());
			String whereClause = EventAccContract.Category._ID + " = ?";
			database.update(EventAccContract.Place.TABLE_NAME, values, whereClause, new String[] { entity.getId()
					.toString() });
		} finally {
			close();
		}
	}

	@Override
	public Place getEntityById(long id) {
		open();
		try {
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
		} finally {
			close();
		}
	}

	@Override
	public List<Place> getEntityList() {
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

	@Override
	public void deleteEntity(long id) {
		open();
		try {
			String whereClause = EventAccContract.Place._ID + " = ?";
			database.delete(EventAccContract.Place.TABLE_NAME, whereClause, new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

	@Override
	protected String getEntityAliasId() {
		return EventAccContract.Place.ALIAS_ID;
	}
}
