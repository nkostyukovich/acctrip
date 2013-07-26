package com.dzebsu.acctrip;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dzebsu.acctrip.adapters.OperationsListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

public class OperationListActivity extends Activity {

	private long eventId;
	// Anonymous class wanted this adapter inside itself
	private OperationsListViewAdapter adapterZ;
	// for restoring list scroll position
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	private boolean dataChanged=false;

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		mListState = state.getParcelable(LIST_STATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventId = getIntent().getLongExtra("eventId", 0);

		setContentView(R.layout.activity_operation_list);
		ListView listView = (ListView) findViewById(R.id.op_list);
		listView.addHeaderView(createListHeader());
		fillOperationList();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// onNewOperation(id);
			}
		});

		((Button) findViewById(R.id.op_new_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNewOperation();
				
			}
		});
		// add filter_Event_Edittext for events names
		SearchView eventsFilter = (SearchView) findViewById(R.id.uni_op_searchView);
		eventsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				OperationListActivity.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});
		// end
		setupActionBar();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, EventListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.delete_event:
			onDeleteEvent(item.getActionView());
			return true;
		case R.id.event_edit:
			onEventEdit(item.getActionView());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onEventEdit(View actionView) {
		Event ev=new EventDataSource(this).getEventById(eventId);
		Intent intent = new Intent(this, EditEventActivity.class);
		intent.putExtra("edit", 0);
		intent.putExtra("id", eventId);
		intent.putExtra("eventName", ev.getName());
		intent.putExtra("eventDesc", ev.getDesc());
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	public void onDeleteEvent(View view) {
		//TODO delete all operations.. optional delete all places, currencies? and check for places and others about being used by another events
		new AlertDialog.Builder(this).setMessage("Delete this Event?").setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						EventDataSource dataSource = new EventDataSource(OperationListActivity.this);
						dataSource.delete(eventId);
						Intent intent = new Intent(OperationListActivity.this, EventListActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("toast", R.string.event_deleted);
						startActivity(intent);
					}
				}).setNegativeButton(android.R.string.no, null).show();
	}

	@Override
	protected void onResume() {
		super.onResume();

		fillOperationList();
		if (mListState != null)
			((ListView) findViewById(R.id.op_list)).onRestoreInstanceState(mListState);
		mListState = null;
	}

	// scroll position saving
	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		mListState = ((ListView) findViewById(R.id.op_list)).onSaveInstanceState();
		state.putParcelable(LIST_STATE, mListState);
		setupActionBar();
	}

	public void onNewOperation() {
		Intent intent = new Intent(this, EditOperationActivity.class);
		intent.putExtra("eventId", eventId);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	private void fillOperationList() {
		if(adapterZ==null || dataChanged){
			dataChanged=false;
		OperationDataSource dataSource = new OperationDataSource(this);
		List<Operation> operations = dataSource.getOperationListByEventId(eventId);
		adapterZ = new OperationsListViewAdapter(this, operations);
		
		}
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) findViewById(R.id.op_list);
		fillEventInfo(eventId);
		listView.setAdapter(adapter);
		OperationListActivity.this.adapterZ.getFilter().filter(
				((SearchView) findViewById(R.id.uni_op_searchView)).getQuery());
	}

	public View createListHeader() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(com.dzebsu.acctrip.R.layout.operations_header, null, false);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(R.string.event_title);
			getActionBar().setHomeButtonEnabled(true);
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		dataChanged=true;
	}
	public void fillEventInfo(long eventId) {
		EventDataSource dataSource = new EventDataSource(this);
		Event ev = dataSource.getEventById(eventId);
		((TextView) findViewById(R.id.op_name_tv)).setText(ev.getName());
		((TextView) findViewById(R.id.op_desc_tv)).setText(ev.getDesc());
		((TextView) findViewById(R.id.op_event_id)).setText(getString(R.string.op_event_id) + String.valueOf(eventId));
		((TextView) findViewById(R.id.op_total_ops)).setText(getString(R.string.op_total_ops) + "16");
		((TextView) findViewById(R.id.op_all_expenses)).setText("-3546 USD");
	}

}
