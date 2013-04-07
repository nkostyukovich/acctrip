package com.dzebsu.acctrip;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class DictionaryActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
 
	private TabHost tabHost;
	private Map<String, TabInfo> mapTabInfo;
	private TabInfo mLastTab = null;

	private static final String TAG_CATEGORIES = "TagCat";
	private static final String TAG_CURRENCIES = "TagCur";
	private static final String TAG_PLACES = "TagPla";

	private class TabInfo {
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", tabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dictionary, menu);
		return true;
	}

	private void initialiseTabHost(Bundle args) {
		mapTabInfo = new HashMap<String, TabInfo>();
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		TabInfo tabInfo = null;
		DictionaryActivity.addTab(this, this.tabHost, this.tabHost.newTabSpec(TAG_CATEGORIES).setIndicator(getResources().getString(R.string.lbl_tab_category)),
				(tabInfo = new TabInfo(TAG_CATEGORIES, CategoryListFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		DictionaryActivity.addTab(this, this.tabHost, this.tabHost.newTabSpec(TAG_CURRENCIES).setIndicator(getResources().getString(R.string.lbl_tab_currency)),
				(tabInfo = new TabInfo(TAG_CURRENCIES, CurrencyListFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		DictionaryActivity.addTab(this, this.tabHost, this.tabHost.newTabSpec(TAG_PLACES).setIndicator(getResources().getString(R.string.lbl_tab_place)),
				(tabInfo = new TabInfo(TAG_PLACES, PlaceListFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		this.onTabChanged(TAG_CATEGORIES);
		//
		tabHost.setOnTabChangedListener(this);
	}

	private static void addTab(DictionaryActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec);
	}

	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this, newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}

}
