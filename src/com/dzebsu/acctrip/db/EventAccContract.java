package com.dzebsu.acctrip.db;

import android.provider.BaseColumns;

public class EventAccContract {

	private EventAccContract() {
	}

	public static abstract class Event implements BaseColumns {

		public static final String TABLE_NAME = "event";

		public static final String NAME = "name";

		public static final String DESC = "description";

		public static final String PRIMARY_CURRENCY_ID = "prim_curr_id";

		public static final String ALIAS_PRIMARY_CURRENCY_ID = "event_prim_curr_id";

		public static final String ALIAS_PRIMARY_CURRENCY_NAME = "event_prim_curr_name";

		public static final String ALIAS_PRIMARY_CURRENCY_CODE = "event_prim_curr_code";

		public static final String ALIAS_ID = "event_id";

		public static final String ALIAS_DESC = "event_desc";

		public static final String ALIAS_NAME = "event_name";

	}

	public static abstract class Category implements BaseColumns {

		public static final String TABLE_NAME = "category";

		public static final String NAME = "name";

		public static final String ALIAS_ID = "cat_id";

		public static final String ALIAS_NAME = "cat_name";
	}

	public static abstract class Currency implements BaseColumns {

		public static final String TABLE_NAME = "currency";

		public static final String NAME = "name";

		public static final String CODE = "code";

		public static final String ALIAS_ID = "cur_id";

		public static final String ALIAS_CODE = "cur_code";

		public static final String ALIAS_NAME = "cur_name";
	}

	public static abstract class Place implements BaseColumns {

		public static final String TABLE_NAME = "place";

		public static final String NAME = "name";

		public static final String ALIAS_ID = "place_id";

		public static final String ALIAS_NAME = "place_name";
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

	public static abstract class CurrencyPair implements BaseColumns {

		public static final String TABLE_NAME = "currency_pair";

		public static final String EVENT_ID = "eventId";

		public static final String FIRST_CURRENCY_ID = "first_curr_id";

		public static final String SECOND_CURRENCY_ID = "second_curr_id";

		public static final String ALIAS_FIRST_CODE = "first_cur_code";

		public static final String ALIAS_FIRST_NAME = "first_cur_name";

		public static final String ALIAS_SECOND_CODE = "second_cur_code";

		public static final String ALIAS_SECOND_NAME = "second_cur_name";

		public static final String RATE = "rate";
	}

}
