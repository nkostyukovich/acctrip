package com.dzebsu.acctrip;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

	// TODO implement database backup with add another one alogpreference
	// here is help
	// http://stackoverflow.com/questions/1995320/how-to-backup-database-file-to-sdcard-on-android
	// http://stackoverflow.com/questions/13502223/backup-restore-sqlite-db-in-android

}
