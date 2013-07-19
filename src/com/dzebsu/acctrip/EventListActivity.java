package com.dzebsu.acctrip;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.models.Event;

public class EventListActivity extends ListActivity {
	
	//Anonymous class wanted this adapter inside itself
	private ArrayAdapter<Event> adapterZ;
	
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
		
		//add filter_Event_Edittext for events names
	    EditText eventsFilter = (EditText) findViewById(R.id.filter_Event_EditText);
	    eventsFilter.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text
	        	EventListActivity.this.adapterZ.getFilter().filter(cs);
	        }
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                int arg3) { }
	        @Override
	        public void afterTextChanged(Editable arg0) {}
	    });
	    //end
	    
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
	        case R.id.delete_all_events_records:
	        	onDeleteAllEventsRecords(item.getActionView());
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	//956 event
	public void onDeleteAllEventsRecords(View view) {
		EventDataSource dataSource = new EventDataSource(this);
		dataSource.deleteAllRecords(956);
	}
	
	public void onNewEvent(View view) {
		Intent intent = new Intent(this, EditEventActivity.class);
		startActivity(intent);
	}
	
	public void onOpenDictionaries(View view) {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);		
	}
//while filtering, id!=position!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void onSelectEvent(long eventId) {
		Intent intent = new Intent(this, OperationListActivity.class);
		intent.putExtra("eventId", eventId);
		startActivity(intent);
	}
	
	private void fillEventList() {
		EventDataSource dataSource = new EventDataSource(this);
		List<Event> events = dataSource.getEventList();
		adapterZ= new EventListViewAdapter(this,  events);
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) findViewById(android.R.id.list);
		//trigger filter to it being applied on resume
		EventListActivity.this.adapterZ.getFilter().filter(((EditText) findViewById(R.id.filter_Event_EditText)).getText());
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
//think itn't needed here		fillEventList();
	}
}
