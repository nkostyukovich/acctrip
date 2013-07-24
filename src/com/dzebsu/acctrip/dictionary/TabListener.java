package com.dzebsu.acctrip.dictionary;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.models.Place;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class TabListener<T> implements ActionBar.TabListener {
	private Fragment fragment;
	private final Activity hostActivity;
	private final String fragmentTag;
	private final Class<T> fragmentClass;

	public TabListener(Activity activity, String tag, Class<T> clz) {
		hostActivity = activity;
		fragmentTag = tag;
		fragmentClass = clz;
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
