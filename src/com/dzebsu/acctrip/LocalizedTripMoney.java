package com.dzebsu.acctrip;

import java.util.Locale;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

public class LocalizedTripMoney extends Application {

	private SharedPreferences preferences;

	private Locale locale;

	private String lang;

	@Override
	public void onCreate() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		lang = preferences.getString("pref_lang", getString(R.string.default_lang));
		if (lang.equals(getString(R.string.default_lang))) return;
		if (lang.equals(getString(R.string.default_lang))) {
			lang = getResources().getConfiguration().locale.getCountry();
		}
		locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (lang.equals(getString(R.string.default_lang))) return;
		if (lang.equals(getResources().getConfiguration().locale.getCountry())) return;
		locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);
	}
}
