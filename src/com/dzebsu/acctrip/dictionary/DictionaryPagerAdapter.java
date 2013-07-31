package com.dzebsu.acctrip.dictionary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class DictionaryPagerAdapter extends FragmentPagerAdapter {

	public DictionaryPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
		switch (arg0) {
			case 0:
				DictionaryListFragment<Place> placeFrag = new DictionaryListFragment<Place>();
				placeFrag.setDataSource(new PlaceDataSource());
				data.putSerializable("class", Place.class);
				placeFrag.setArguments(data);
				return placeFrag;
			case 1:
				DictionaryListFragment<Category> catFrag = new DictionaryListFragment<Category>();
				catFrag.setDataSource(new CategoryDataSource());
				data.putSerializable("class", Category.class);
				catFrag.setArguments(data);
				return catFrag;
			case 2:
				DictionaryListFragment<Currency> curFrag = new DictionaryListFragment<Currency>();
				curFrag.setDataSource(new CurrencyDataSource());
				data.putSerializable("class", Currency.class);
				curFrag.setArguments(data);
				return curFrag;
			default:
				break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return DictionaryType.values().length;
	}

}
