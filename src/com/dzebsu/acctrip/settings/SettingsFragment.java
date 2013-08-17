package com.dzebsu.acctrip.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.settings.dialogs.BackupOnDeviceDialogPreference;
import com.dzebsu.acctrip.settings.dialogs.BackupViaEmailDialogPreference;
import com.dzebsu.acctrip.settings.dialogs.LanguagePickerListPreference;
import com.dzebsu.acctrip.settings.dialogs.RestoreFromDeviceDialogPreference;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String PREF_ALLOW_CURRENT_EVENT_MODE = "pref_allow_current_event_mode";

	public static final String CURRENT_EVENT_MODE_EVENT_ID = "current_event_mode_event_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		// Setup the initial values
		updateAllEntries(getPreferenceScreen());

	}

	private void updateAllEntries(PreferenceScreen ps) {
		SharedPreferences sharedPreferences = ps.getSharedPreferences();
		for (int i = 0; i < ps.getPreferenceCount(); i++) {
			if (ps.getPreference(i) instanceof PreferenceScreen) {
				updateAllEntries((PreferenceScreen) ps.getPreference(i));
				continue;
			}
			initSummary(sharedPreferences, ps.getPreference(i));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	private void initSummary(SharedPreferences sharedPreferences, Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			for (int i = 0; i < pCat.getPreferenceCount(); i++) {
				initSummary(sharedPreferences, pCat.getPreference(i));
			}
		} else {
			updatePrefSummary(sharedPreferences, p);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Let's do something when a preference value changes
		updatePrefSummary(sharedPreferences, findPreference(key));
	}

	private void updatePrefSummary(SharedPreferences sharedPreferences, Preference p) {
		if (p instanceof CheckBoxPreference) {
			CheckBoxPreference box = (CheckBoxPreference) p;
			long id = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getLong(
					CURRENT_EVENT_MODE_EVENT_ID, -1);
			String f = id != -1 ? new EventDataSource(this.getActivity()).getEventById(id).getName() : "no event";
			String s = box.isChecked() ? getString(R.string.pref_curr_event_mode_summary)
					+ getString(R.string.current_event) + f : getString(R.string.pref_curr_event_mode_summary);
			p.setSummary(s);
		} else if (p instanceof LanguagePickerListPreference) {
			LanguagePickerListPreference picker = (LanguagePickerListPreference) p;
			String s = picker.getEntry().toString();
			p.setSummary(s);
		} else if (p instanceof BackupOnDeviceDialogPreference) {
			BackupOnDeviceDialogPreference condirmDialogPreference = (BackupOnDeviceDialogPreference) p;
			String[] s = sharedPreferences.getString(condirmDialogPreference.getKey(),
					getString(R.string.pref_backup_database_def)).split("@");

			p.setSummary(String.format(getString(R.string.last_backup), s[0], s[1]));

		} else if (p instanceof BackupViaEmailDialogPreference) {
			BackupViaEmailDialogPreference condirmDialogPreference = (BackupViaEmailDialogPreference) p;
			String s = sharedPreferences.getString(condirmDialogPreference.getKey(), getString(R.string.unknown));

			p.setSummary(String.format(getString(R.string.last_backup_half), s));

		} else if (p instanceof RestoreFromDeviceDialogPreference) {
			RestoreFromDeviceDialogPreference condirmDialogPreference = (RestoreFromDeviceDialogPreference) p;
			String s = sharedPreferences.getString(condirmDialogPreference.getKey(), getString(R.string.unknown));

			p.setSummary(String.format(getString(R.string.pref_restore_from_device_summary), Environment
					.getExternalStorageDirectory()
					+ "/" + EventAccDbHelper.DATABASE_NAME, s));

		}
	}
}
