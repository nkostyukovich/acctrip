/**
 * 
 */
package com.dzebsu.acctrip;

import com.dzebsu.acctrip.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.dzebsu.acctrip.dictionary.*;
import com.dzebsu.acctrip.models.Category;
import com.dzebsu.acctrip.models.Place;


public class DictionaryActivity extends FragmentActivity {

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
			int i=1;
			setContentView(R.layout.activity_dictionary);
			ActionBar ab =getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setDisplayShowTitleEnabled(false);
			ab.setDisplayShowHomeEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			Tab tab = ab.newTab()
		            .setText(R.string.place_tab)
		            .setTabListener(
		            		new TabListener<DictionaryListFragment>(this, "Place", DictionaryListFragment.class));
		    ab.addTab(tab);

		    tab = ab.newTab()
		        .setText(R.string.category_tab)
		        .setTabListener(new TabListener<DictionaryListFragment>(this, "Category", DictionaryListFragment.class));
		    ab.addTab(tab);
		    
		    tab = ab.newTab()
			        .setText(R.string.currency_tab)
			        .setTabListener(new TabListener<DictionaryListFragment>(this, "Currency", DictionaryListFragment.class));
			    ab.addTab(tab);
		}
		ViewPager mViewPager = (ViewPager) findViewById(R.id.dic_pager);
		 FragmentManager fm = getSupportFragmentManager();
		 DictionaryPagerAdapter pagerAdapter = new DictionaryPagerAdapter(fm);
		 mViewPager.setAdapter(pagerAdapter);
		    mViewPager.setOnPageChangeListener(
		            new ViewPager.SimpleOnPageChangeListener() {
		                @Override
		                public void onPageSelected(int position) {
		                    // When swiping between pages, select the
		                    // corresponding tab.
		                    getActionBar().setSelectedNavigationItem(position);
		                }
		            });

	        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dictionary, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
