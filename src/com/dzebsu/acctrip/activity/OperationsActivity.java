package com.dzebsu.acctrip.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.SimpleDialogListener;
import com.dzebsu.acctrip.operations.OperationListFragment;
import com.dzebsu.acctrip.operations.OperationsPagerAdapter;
import com.dzebsu.acctrip.operations.TabListener;
import com.dzebsu.acctrip.operations.TabUpdateListener;
import com.dzebsu.acctrip.operations.TabUpdater;

public class OperationsActivity extends FragmentActivity implements TabUpdater, SimpleDialogListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setContentView(R.layout.activity_operations);
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setDisplayShowTitleEnabled(false);
			ab.setDisplayShowHomeEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			Tab tab = ab.newTab().setText(R.string.op_tabs_operations).setIcon(R.drawable.operations).setTabListener(
					new TabListener(this));
			ab.addTab(tab);

			tab = ab.newTab().setText(R.string.op_tabs_statistics).setIcon(R.drawable.stats).setTabListener(
					new TabListener(this));
			ab.addTab(tab);

		}
		ViewPager mViewPager = (ViewPager) findViewById(R.id.op_pager);
		FragmentManager fm = getSupportFragmentManager();
		OperationsPagerAdapter pagerAdapter = new OperationsPagerAdapter(fm, getIntent().getExtras());
		if (getIntent().hasExtra(OperationListFragment.INTENT_KEY_NEW_CURRENCY_APPEARED)) {
			getIntent().removeExtra(OperationListFragment.INTENT_KEY_NEW_CURRENCY_APPEARED);
		}
		if (getIntent().hasExtra(OperationListFragment.INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED)) {
			getIntent().removeExtra(OperationListFragment.INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED);
		}
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the
				// corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
			}
		});

	}

	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.operations, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.open_dictionaries:
				onOpenDictionaries(item.getActionView());
				return true;
			case R.id.settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onOpenDictionaries(View view) {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private List<TabUpdateListener> tabs;

	@Override
	public void registerTab(TabUpdateListener tab) {
		if (tabs == null) {
			tabs = new ArrayList<TabUpdateListener>();
		}
		tabs.add(tab);
	}

	@Override
	public void updateTabs() {
		for (TabUpdateListener tab : tabs) {
			tab.update();
		}

	}

	@Override
	public void unregisterTab(TabUpdateListener tab) {
		if (tabs != null) {
			tabs.remove(tab);
		}

	}

	@Override
	public void positiveButtonDialog(Bundle args) {
		updateTabs();

	}

	@Override
	public void negativeButtonDialog(Bundle args) {
		updateTabs();

	}

	@Override
	public void anotherDialogAction(Bundle args) {
		updateTabs();

	}

}
