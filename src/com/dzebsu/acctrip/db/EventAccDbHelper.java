package com.dzebsu.acctrip.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventAccDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 6;

	public static final String DATABASE_NAME = "EventAcc.db";

	private static final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventAccContract.Event.TABLE_NAME + " ("
			+ EventAccContract.Event._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EventAccContract.Event.NAME
			+ " TEXT NOT NULL, " + EventAccContract.Event.DESC + " TEXT, " + EventAccContract.Event.PRIMARY_CURRENCY_ID
			+ " INTEGER NOT NULL, FOREIGN KEY (" + EventAccContract.Event.PRIMARY_CURRENCY_ID + ") REFERENCES "
			+ EventAccContract.Currency.TABLE_NAME + " (" + EventAccContract.Currency._ID + ") " + " )";

	private static final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + EventAccContract.Category.TABLE_NAME
			+ " (" + EventAccContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EventAccContract.Category.NAME + " TEXT NOT NULL " + " )";

	private static final String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE " + EventAccContract.Currency.TABLE_NAME
			+ " (" + EventAccContract.Currency._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EventAccContract.Currency.NAME + " TEXT NOT NULL, " + EventAccContract.Currency.CODE + " TEXT NOT NULL "
			+ " )";

	private static final String SQL_CREATE_PLACE_TABLE = "CREATE TABLE " + EventAccContract.Place.TABLE_NAME + " ("
			+ EventAccContract.Place._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EventAccContract.Place.NAME
			+ " TEXT NOT NULL " + " )";

	private static final String SQL_CREATE_OPERATION_TABLE = "CREATE TABLE " + EventAccContract.Operation.TABLE_NAME
			+ " (" + EventAccContract.Operation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EventAccContract.Operation.CATEGORY_ID + " INTEGER NOT NULL, " + EventAccContract.Operation.DESC
			+ " TEXT, " + EventAccContract.Operation.TYPE + " INTEGER NOT NULL, " + EventAccContract.Operation.VALUE
			+ " REAL NOT NULL, " + EventAccContract.Operation.CURRENCY_ID + " INTEGER NOT NULL, "
			+ EventAccContract.Operation.DATE + " LONG NOT NULL, " + EventAccContract.Operation.EVENT_ID
			+ " INTEGER NOT NULL, " + EventAccContract.Operation.PLACE_ID + " INTEGER NOT NULL, " + "FOREIGN KEY ("
			+ EventAccContract.Operation.CATEGORY_ID + ") REFERENCES " + EventAccContract.Category.TABLE_NAME + " ("
			+ EventAccContract.Category._ID + "), " + "FOREIGN KEY (" + EventAccContract.Operation.CURRENCY_ID
			+ ") REFERENCES " + EventAccContract.Currency.TABLE_NAME + " (" + EventAccContract.Currency._ID + ") "
			+ "FOREIGN KEY (" + EventAccContract.Operation.EVENT_ID + ") REFERENCES "
			+ EventAccContract.Event.TABLE_NAME + " (" + EventAccContract.Event._ID + ") " + "FOREIGN KEY ("
			+ EventAccContract.Operation.PLACE_ID + ") REFERENCES " + EventAccContract.Place.TABLE_NAME + " ("
			+ EventAccContract.Place._ID + ") " + " )";

	private static final String SQL_CREATE_CUREENCY_PAIR_TABLE = "CREATE TABLE "
			+ EventAccContract.CurrencyPair.TABLE_NAME + " (" + EventAccContract.CurrencyPair.EVENT_ID
			+ " INTEGER NOT NULL, " + EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + " INTEGER NOT NULL, "
			+ EventAccContract.CurrencyPair.RATE + " REAL NOT NULL, PRIMARY KEY ("
			+ EventAccContract.CurrencyPair.EVENT_ID + ", " + EventAccContract.CurrencyPair.SECOND_CURRENCY_ID
			+ "), FOREIGN KEY (" + EventAccContract.CurrencyPair.EVENT_ID + ") REFERENCES "
			+ EventAccContract.Event.TABLE_NAME + " (" + EventAccContract.Event._ID + "),  FOREIGN KEY ("
			+ EventAccContract.CurrencyPair.SECOND_CURRENCY_ID + ") REFERENCES " + EventAccContract.Currency.TABLE_NAME
			+ " (" + EventAccContract.Currency._ID + ")" + " )";

	public EventAccDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_EVENT_TABLE);
		db.execSQL(SQL_CREATE_CATEGORY_TABLE);
		db.execSQL(SQL_CREATE_CURRENCY_TABLE);
		db.execSQL(SQL_CREATE_PLACE_TABLE);
		db.execSQL(SQL_CREATE_OPERATION_TABLE);
		db.execSQL(SQL_CREATE_CUREENCY_PAIR_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) reCreateAllTables(db);
	}

	public void reCreateAllTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.Event.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.Category.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.Currency.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.Place.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.Operation.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EventAccContract.CurrencyPair.TABLE_NAME);
		onCreate(db);
	}
}
