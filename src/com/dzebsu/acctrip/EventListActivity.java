package com.dzebsu.acctrip;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.models.Event;

public class EventListActivity extends ListActivity {
	
	//Anonymous class wanted this adapter inside itself
	private ArrayAdapter<Event> adapterZ;
	//for restoring list scroll position
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {	    
		super.onRestoreInstanceState(state);
		mListState = state.getParcelable(LIST_STATE);
	}
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
	    SearchView eventsFilter = (SearchView) findViewById(R.id.event_SearchView);
	    eventsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				EventListActivity.this.adapterZ.getFilter().filter(newText);
				return true;
			}
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
	        case R.id.recreate_all_tables:
	        	reCreateAllTables(item.getActionView());
	    		return true;
	        case R.id.create_1000_records:
	            create1000records(item.getActionView());
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void create1000records(View view){
		EventDataSource dataSource = new EventDataSource(this);
		for(int i=0; i<1000; i++)
			dataSource.insert("Record #"+Integer.toString(i), "Day before yesterday I saw a rabbit, and yesterday a deer, and today, you. I thought what I'd do was, I'd pretend I was one of those deaf-mutes.");
	}
	
	public void reCreateAllTables(View view) {
		
		EventAccDbHelper dbHelper=new EventAccDbHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.reCreateAllTables(db);
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
		EventListActivity.this.adapterZ.getFilter().filter(((SearchView) findViewById(R.id.event_SearchView)).getQuery());
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
		
//think itn't needed here
		fillEventList();
		if (mListState != null)
			((ListView) findViewById(android.R.id.list)).onRestoreInstanceState(mListState);
	    mListState = null;
	}
	//scroll position saving
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    mListState = ((ListView) findViewById(android.R.id.list)).onSaveInstanceState();
	    state.putParcelable(LIST_STATE, mListState);
	}
}
