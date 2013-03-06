package com.dzebsu.acctrip.db;

import android.provider.BaseColumns;

public class EventAccContract {
	
	private EventAccContract() {		
	}
	
	public static abstract class Event implements BaseColumns {		
		public static final String TABLE_NAME = "event";
		public static final String NAME = "name";
		public static final String DESC = "description";		
	} 
	
	public static abstract class Category implements BaseColumns {		
		public static final String TABLE_NAME = "category";
		public static final String NAME = "name";			
	} 
	
	public static abstract class Currency implements BaseColumns {		
		public static final String TABLE_NAME = "currency";
		public static final String NAME = "name";
		public static final String CODE = "code";		
	} 
	
	public static abstract class Place implements BaseColumns {		
		public static final String TABLE_NAME = "place";
		public static final String NAME = "name";
	} 

	public static abstract class Operation implements BaseColumns {		
		public static final String TABLE_NAME = "operation";
		public static final String CATEGORY_ID = "categoryId";
		public static final String DESC = "description";
		public static final String TYPE = "type";
		public static final String VALUE = "value";
		public static final String CURRENCY_ID = "currencyId";
		public static final String DATE = "date";
		public static final String EVENT_ID = "eventId";
		public static final String PLACE_ID = "placeId";
	}	

}
