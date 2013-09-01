package com.dzebsu.acctrip.operations;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.dzebsu.acctrip.R;

public class GraphStatActivity extends Activity {

	public static final String INTENT_KEY_SORT_CATEGORY = "sort_category";

	public static final String INTENT_KEY_EVENT_ID = "event_Id";

	private int group;

	private long eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(new TimeGraph().getIntent(this));
		Intent intent = getIntent();
		eventId = intent.getLongExtra(INTENT_KEY_EVENT_ID, -1);
		group = intent.getIntExtra(INTENT_KEY_SORT_CATEGORY, -1);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(getResources().getStringArray(R.array.statistics_groups)[group]);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
