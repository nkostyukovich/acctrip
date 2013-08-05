package com.dzebsu.acctrip.settings.dialogs;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.dzebsu.acctrip.settings.SettingsActivity;

public class LanguageListPreference extends ListPreference {

	private Context cxt;

	public LanguageListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		cxt = context;
		this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				setLocale((String) newValue);
				return true;
			}
		});
	}

	public void setLocale(String lang) {

		Locale myLocale = new Locale(lang);
		Resources res = cxt.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);

		// Intent intent = new Intent(cxt, EventListActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// ((Activity) cxt).startActivity(intent);
		// TODO notify that everything changed
		((Activity) cxt).finish();
		Intent refresh = new Intent(cxt, SettingsActivity.class);
		cxt.startActivity(refresh);
	}
}
