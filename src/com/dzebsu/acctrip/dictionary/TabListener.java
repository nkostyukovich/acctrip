package com.dzebsu.acctrip.dictionary;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.dzebsu.acctrip.R;

public class TabListener implements ActionBar.TabListener {

	private Fragment fragment;

	private final Activity hostActivity;

	public TabListener(Activity activity) {
		hostActivity = activity;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
		ViewPager mViewPager = (ViewPager) hostActivity.findViewById(R.id.dic_pager);
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(fragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab.
	}

}
