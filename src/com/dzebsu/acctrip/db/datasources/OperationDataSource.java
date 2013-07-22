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

public class OperationDataSource {

	private SQLiteDatabase database;
	private EventAccDbHelper dbHelper;

	private final static String SELECT_OP_QUERY = "select op. " + EventAccContract.Operation._ID + " " + EventAccContract.Operation._ID + ", " +
			 "cat." + EventAccContract.Category._ID + " " + EventAccContract.Category.ALIAS_ID + ", " +
			 "cat." + EventAccContract.Category.NAME + " " + EventAccContract.Category.ALIAS_NAME + ", " +
			 "op. " + EventAccContract.Operation.DESC + " " + EventAccContract.Operation.DESC + ", " +
			 "op. " + EventAccContract.Operation.TYPE + " " + EventAccContract.Operation.TYPE + ", " +
			 "op. " + EventAccContract.Operation.VALUE + " " + EventAccContract.Operation.VALUE + ", " +
			 "cur." + EventAccContract.Currency._ID + " " + EventAccContract.Currency.ALIAS_ID + ", " +
			 "cur." + EventAccContract.Currency.CODE + " " + EventAccContract.Currency.ALIAS_CODE + ", " +
			 "op. " + EventAccContract.Operation.DATE + " " + EventAccContract.Operation.DATE + ", " +
			 "ev." + EventAccContract.Event._ID + " " + EventAccContract.Event.ALIAS_ID + ", " +
			 "pl." + EventAccContract.Place._ID + " " + EventAccContract.Place.ALIAS_ID + ", " +
			 "pl." + EventAccContract.Place.NAME + " " + EventAccContract.Place.ALIAS_NAME + 
			 " from operation op join event ev on (op.eventId = ev._id) " +
			 " join category cat on (op.categoryId = cat._id) " +
			 " join currency cur on (op.currencyId = cur._id) " + 
			 " left join place pl on (op.placeId = pl._id) ";
 

	public OperationDataSource(Context ctx) {
		dbHelper = new EventAccDbHelper(ctx);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}
	
//	public long insert(String name, String desc) {
//		open();
//		try {
//			ContentValues values = new ContentValues();
//			values.put(EventAccContract.Event.NAME, name);
//			values.put(EventAccContract.Event.DESC, desc);		 
//			return database.insert(EventAccContract.Event.TABLE_NAME, null, values);
//		} finally {
//			close();
//		}
//	}

//	public Event update(Long id, String name, String desc) {
//		open();
//		try {
//			ContentValues values = new ContentValues();
//			values.put(EventAccContract.Event.NAME, name);
//			values.put(EventAccContract.Event.DESC, desc);
//			String whereClause = EventAccContract.Event._ID + " = ?";
//			database.update(EventAccContract.Event.TABLE_NAME, values, whereClause, new String[] { Long.toString(id) });
//			return getOperationById(id);
//		} finally {
//			close();
//		}
//	}

	public Operation getOperationById(long id) {
		String whereBatch = EventAccContract.Event._ID + " = " + id;
		Cursor c = database.rawQuery(SELECT_OP_QUERY + whereBatch, null);
		Operation op = null;
		if (c.getCount() > 0) {
			c.moveToFirst();
			op = ConvertUtils.cursorToOperation(c);
		}
		c.close();
		return op;
	}

	public List<Operation> getOperationList(long eventId) {
		open();
		try {
			List<Operation> result = new ArrayList<Operation>();
			Cursor c = database.rawQuery(SELECT_OP_QUERY + " where ev._Id = " + eventId + " " +
										 " order by " + EventAccContract.Operation._ID + " desc", null);		 
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
