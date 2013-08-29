package com.dzebsu.acctrip.operations;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.dzebsu.acctrip.R;

public class TabListener implements ActionBar.TabListener {

	private final Activity hostActivity;

	public TabListener(Activity activity) {
		hostActivity = activity;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
		ViewPager mViewPager = (ViewPager) hostActivity.findViewById(R.id.op_pager);
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab.
	}

}
