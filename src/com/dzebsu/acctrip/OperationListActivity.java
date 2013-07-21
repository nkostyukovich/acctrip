package com.dzebsu.acctrip;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.models.Event;

public class OperationListActivity extends Activity {
	
	private long eventId;
	static class HeaderViewHolder{

		public TextView name=null;
		public TextView desc=null;
		public TextView eventId=null;
		public TextView totalOps=null;
		public TextView expenses=null;
		public SearchView uni_filter=null;

	}
	//Anonymous class wanted this adapter inside itself
	private EventListViewAdapter adapterZ;
	//for restoring list scroll position
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		 setContentView(R.layout.activity_operation_list);
		ListView listView = (ListView) findViewById(R.id.op_list);
		listView.addHeaderView(createListHeader());
		fillOperationList();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//onSelectEvent(adapterZ.getEventByIdInList((int)id).getId());
			}
		});
		
		//add filter_Event_Edittext for events names
		/*    SearchView eventsFilter = (SearchView) findViewById(R.id.event_SearchView);
	    eventsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				OperationListActivity.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});
	    //end
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation_list, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	public void onNewOperation(View view) {
//		Intent intent = new Intent(this, EditOperationActivity.class);
//		startActivity(intent);
	}

	private void fillOperationList() {
		EventDataSource dataSource = new EventDataSource(this);
		List<Event> events = dataSource.getEventList();
		adapterZ= new EventListViewAdapter(this,  events);
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) findViewById(R.id.op_list);
		//trigger filter to it being applied on resume
	//	OperationListActivity.this.adapterZ.getFilter().filter(((EditText) findViewById(R.id.editText1)).getText().toString());
		listView.setAdapter(adapter);		
	}
	
	public View createListHeader(){
		View headerView=null;
		if (headerView == null) {
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			headerView = inflater.inflate(com.dzebsu.acctrip.R.layout.operations_header, null,false);
		      HeaderViewHolder headerViewHolder = new HeaderViewHolder();
		      headerViewHolder.name = (TextView) headerView.findViewById(com.dzebsu.acctrip.R.id.op_name_tv);
		      headerViewHolder.desc = (TextView) headerView.findViewById(com.dzebsu.acctrip.R.id.op_desc_tv);
		      headerViewHolder.eventId = (TextView) headerView.findViewById(com.dzebsu.acctrip.R.id.op_event_id);
		      headerViewHolder.totalOps = (TextView) headerView.findViewById(com.dzebsu.acctrip.R.id.op_total_ops);
		      headerViewHolder.expenses = (TextView) headerView.findViewById(com.dzebsu.acctrip.R.id.op_all_expenses);
		      headerViewHolder.uni_filter = (SearchView) headerView.findViewById(com.dzebsu.acctrip.R.id.uni_op_searchView);
		      headerView.setTag(headerViewHolder);
		    }
		HeaderViewHolder holder = (HeaderViewHolder) headerView.getTag();
		holder.name.setText("123");
		holder.desc.setText("1234");
		holder.eventId.setText("12345");
		holder.totalOps.setText("123456");
		holder.expenses.setText("1234567");
		holder.desc.setText("12345678");
		return headerView;
	}

}
