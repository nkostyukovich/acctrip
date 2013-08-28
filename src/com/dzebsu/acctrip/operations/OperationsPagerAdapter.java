package com.dzebsu.acctrip.operations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OperationsPagerAdapter extends FragmentPagerAdapter {

	private static final int TABS_COUNT = 2;

	private final Bundle intent;

	public OperationsPagerAdapter(FragmentManager fm, Bundle intent) {
		super(fm);
		this.intent = intent;
	}

	@Override
	public Fragment getItem(int tab) {
		switch (tab) {
			case 0:
				OperationListFragment fr = new OperationListFragment();
				fr.setArguments(intent);
				return fr;
			case 1:
				OperationListFragment fr2 = new OperationListFragment();
				fr2.setArguments(intent);
				return fr2;
			default:
				break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TABS_COUNT;
	}

}
