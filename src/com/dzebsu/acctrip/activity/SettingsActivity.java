package com.dzebsu.acctrip.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.settings.SettingsFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getActionBar().setTitle(R.string.settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
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
