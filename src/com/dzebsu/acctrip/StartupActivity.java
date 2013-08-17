package com.dzebsu.acctrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.dzebsu.acctrip.settings.SettingsFragment;

public class StartupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent;
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				SettingsFragment.PREF_ALLOW_CURRENT_EVENT_MODE, false)) {
			intent = new Intent(this, EventListActivity.class);
		} else {
			long id = PreferenceManager.getDefaultSharedPreferences(this).getLong(
					SettingsFragment.CURRENT_EVENT_MODE_EVENT_ID, -1);
			if (id == -1) {
				intent = new Intent(this, EventListActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			intent = new Intent(this, OperationListActivity.class);
			intent.putExtra(OperationListActivity.INTENT_KEY_EVENT_ID, id);
		}
		startActivity(intent);
		finish();
	}
}
