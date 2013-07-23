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
import android.view.Menu;
import android.view.MenuItem;
import com.dzebsu.acctrip.dictionary.*;
public class DictionaryActivity extends Activity {

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
			ActionBar ab =getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setDisplayShowTitleEnabled(false);
			ab.setDisplayShowHomeEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			Tab tab = ab.newTab()
		            .setText(R.string.place_tab)
		            .setTabListener(new TabListener<PlaceListFragment>(
		                    this, "Place", PlaceListFragment.class));
		    ab.addTab(tab);

		    tab = ab.newTab()
		        .setText(R.string.category_tab)
		        .setTabListener(new TabListener<CategoryListFragment>(
		                this, "Category", CategoryListFragment.class));
		    ab.addTab(tab);
		    
		    tab = ab.newTab()
			        .setText(R.string.currency_tab)
			        .setTabListener(new TabListener<CurrencyListFragment>(
			                this, "Currency", CurrencyListFragment.class));
			    ab.addTab(tab);
		}
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
