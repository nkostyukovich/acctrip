package com.dzebsu.acctrip.db.datasources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.OperationType;

public class OperationDataSource {

	private SQLiteDatabase database;

	private EventAccDbHelper dbHelper;

	private final static String SELECT_OP_QUERY = "select op." + EventAccContract.Operation._ID + " "
			+ EventAccContract.Operation._ID + ", " + "cat." + EventAccContract.Category._ID + " "
			+ EventAccContract.Category.ALIAS_ID + ", " + "cat." + EventAccContract.Category.NAME + " "
			+ EventAccContract.Category.ALIAS_NAME + ", " + "op." + EventAccContract.Operation.DESC + " "
			+ EventAccContract.Operation.DESC + ", " + "op." + EventAccContract.Operation.TYPE + " "
			+ EventAccContract.Operation.TYPE + ", " + "op." + EventAccContract.Operation.VALUE + " "
			+ EventAccContract.Operation.VALUE + ", " + "cur." + EventAccContract.Currency._ID + " "
			+ EventAccContract.Currency.ALIAS_ID + ", " + "cur." + EventAccContract.Currency.CODE + " "
			+ EventAccContract.Currency.ALIAS_CODE + ", " +

			"cur." + EventAccContract.Currency.NAME + " " + EventAccContract.Currency.ALIAS_NAME + ", " + "op."
			+ EventAccContract.Operation.DATE + " " + EventAccContract.Operation.DATE + ", " + "ev."
			+ EventAccContract.Event._ID + " " + EventAccContract.Event.ALIAS_ID + ", " + "pl."
			+ EventAccContract.Place._ID + " " + EventAccContract.Place.ALIAS_ID + ", " + "pl."
			+ EventAccContract.Place.NAME + " " + EventAccContract.Place.ALIAS_NAME
			+ " from operation op left join event ev on (op.eventId = ev._id) "
			+ " left join category cat on (op.categoryId = cat._id) "
			+ " left join currency cur on (op.currencyId = cur._id) " + " left join place pl on (op.placeId = pl._id) ";

	private Context ctx;

	public static String getSelectConjunctionTableQuery() {
		return SELECT_OP_QUERY;
	}

	public OperationDataSource(Context ctx) {
		this.ctx = ctx;
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public long insert(Date date, String desc, double value, OperationType type, long eventId, long catId, long curId,
			long placeId) {
		open();
		try {
			ContentValues values = new ContentValues();
			if (catId != -1) {
				values.put(EventAccContract.Operation.CATEGORY_ID, catId);
			} else {
				values.putNull(EventAccContract.Operation.CATEGORY_ID);
			}
			if (curId != -1) {
				values.put(EventAccContract.Operation.CURRENCY_ID, curId);
			} else {
				values.putNull(EventAccContract.Operation.CURRENCY_ID);
			}
			if (date != null) {
				values.put(EventAccContract.Operation.DATE, ConvertUtils.convertDateToLong(date));
			} else {
				values.putNull(EventAccContract.Operation.DATE);
			}
			values.put(EventAccContract.Operation.DESC, desc);
			values.put(EventAccContract.Operation.EVENT_ID, eventId);
			if (placeId != -1) {
				values.put(EventAccContract.Operation.PLACE_ID, placeId);
			} else {
				values.putNull(EventAccContract.Operation.PLACE_ID);
			}

			values.put(EventAccContract.Operation.TYPE, type.getLabel(ctx));
			values.put(EventAccContract.Operation.VALUE, value);
			long ir = database.insert(EventAccContract.Operation.TABLE_NAME, null, values);
			return ir;
		} finally {
			close();
		}
	}

	public Operation update(Long id, Date date, String desc, double value, OperationType type, long eventId,
			long catId, long curId, long placeId) {
		open();
		try {
			ContentValues values = new ContentValues();
			if (catId != -1) {
				values.put(EventAccContract.Operation.CATEGORY_ID, catId);
			} else {
				values.putNull(EventAccContract.Operation.CATEGORY_ID);
			}
			if (curId != -1) {
				values.put(EventAccContract.Operation.CURRENCY_ID, curId);
			} else {
				values.putNull(EventAccContract.Operation.CURRENCY_ID);
			}
			if (date != null) {
				values.put(EventAccContract.Operation.DATE, ConvertUtils.convertDateToLong(date));
			} else {
				values.putNull(EventAccContract.Operation.DATE);
			}
			values.put(EventAccContract.Operation.DESC, desc);
			values.put(EventAccContract.Operation.EVENT_ID, eventId);
			if (placeId != -1) {
				values.put(EventAccContract.Operation.PLACE_ID, placeId);
			} else {
				values.putNull(EventAccContract.Operation.PLACE_ID);
			}

			values.put(EventAccContract.Operation.TYPE, type.getLabel(ctx));
			values.put(EventAccContract.Operation.VALUE, value);
			String whereClause = EventAccContract.Operation._ID + " = ?";
			database.update(EventAccContract.Operation.TABLE_NAME, values, whereClause, new String[] { Long
					.toString(id) });
			return getOperationById(id);
		} finally {
			close();
		}
	}

	public long deleteByEventId(long id) {
		open();
		try {
			String whereClause = "eventId = ?";
			return database.delete(EventAccContract.Operation.TABLE_NAME, whereClause,
					new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

	public long deleteById(long id) {
		open();
		try {
			String whereClause = "_ID = ?";
			return database.delete(EventAccContract.Operation.TABLE_NAME, whereClause,
					new String[] { Long.toString(id) });
		} finally {
			close();
		}
	}

	public Operation getOperationById(long id) {
		open();
		try {
			String whereBatch = "where op." + EventAccContract.Operation._ID + " = " + id;
			Cursor c = database.rawQuery(SELECT_OP_QUERY + whereBatch, null);
			Operation op = null;
			if (c.getCount() > 0) {
				c.moveToFirst();
				op = ConvertUtils.cursorToOperation(c);
			}
			c.close();
			return op;
		} finally {
			close();
		}
	}

	public long getCountByEventId(long eventId) {
		open();
		try {
			Cursor c = database.rawQuery("select count(eventId) evcount from " + EventAccContract.Operation.TABLE_NAME
					+ " where eventId=?", new String[] { Long.toString(eventId) });
			c.moveToFirst();
			long cnt = Long.parseLong(c.getString(c.getColumnIndex("evcount")));
			c.close();
			return cnt;
		} finally {
			close();
		}
	}

	public double getSumByEventId(long eventId) {
		open();
		try {
			Cursor c = database.rawQuery("select sum(value) evsum from " + EventAccContract.Operation.TABLE_NAME
					+ " where eventId=?", new String[] { Long.toString(eventId) });
			c.moveToFirst();
			double cnt = c.getDouble(c.getColumnIndex("evsum"));
			c.close();
			return cnt;
		} finally {
			close();
		}
	}

	public Operation getLastOperationByEventId(long eventId) {
		List<Operation> ops = getOperationListByEventId(eventId);
		return !ops.isEmpty() ? ops.get(0) : null;
	}

	public List<Operation> getOperationListByEventId(long eventId) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where ev._Id = " + eventId + " order by "
					+ EventAccContract.Operation._ID + " desc", null);
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

	public List<Operation> getOperationListByPlaceId(long placeId) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where pl._Id = " + placeId + " order by "
					+ EventAccContract.Operation._ID + " desc", null);
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

	public List<Operation> getOperationListByCategoryId(long categoryId) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where cat._Id = " + categoryId + " order by "
					+ EventAccContract.Operation._ID + " desc", null);
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

	public List<Operation> getOperationListByCurrencyId(long currencyId) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where cur._Id = " + currencyId + " order by "
					+ EventAccContract.Operation._ID + " desc", null);
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

}
