package com.dzebsu.acctrip;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.models.Event;

public class EventListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		fillEventList();
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onSelectEvent(id);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.open_dictionaries:
	            onOpenDictionaries(item.getActionView());
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onNewEvent(View view) {
		Intent intent = new Intent(this, EditEventActivity.class);
		startActivity(intent);
	}
	
	public void onOpenDictionaries(View view) {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);		
	}

	public void onSelectEvent(long eventId) {
		Intent intent = new Intent(this, OperationListActivity.class);
		intent.putExtra("eventId", eventId);
		startActivity(intent);
	}

	private void fillEventList() {
		EventDataSource dataSource = new EventDataSource(this);
		List<Event> events = dataSource.getEventList();
		ListAdapter adapter = new EventListViewAdapter(this,  events);
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
