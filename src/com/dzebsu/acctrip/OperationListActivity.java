package com.dzebsu.acctrip;

import java.util.List;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.models.Event;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OperationListActivity extends ListActivity {
	
	private long eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_operation_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation_list, menu);
		return true;
	}

	public void onNewOperation(View view) {
//		Intent intent = new Intent(this, EditOperationActivity.class);
//		startActivity(intent);
	}

	private void fillOperationList() {
		EventDataSource dataSource = new EventDataSource(this);
		List<Event> events = dataSource.getEventList();
		ListAdapter adapter = new EventListViewAdapter(this, android.R.layout.simple_list_item_1, events);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);		
	}

}
