package com.dzebsu.acctrip;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.EventDataSource;
import com.dzebsu.acctrip.models.Event;

public class MainActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fillEventList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onNewEvent(View view) {
		Intent intent = new Intent(this, EditEventActivity.class);
		startActivity(intent);
	}

	private void fillEventList() {
		EventDataSource dataSource = new EventDataSource(this);
		List<Event> events = dataSource.getEventList();
		ListAdapter adapter = new EventListViewAdapter(this, android.R.layout.simple_list_item_1, events);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		fillEventList();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fillEventList();		
	}
}
