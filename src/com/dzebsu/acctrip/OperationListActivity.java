package com.dzebsu.acctrip;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dzebsu.acctrip.adapters.EventListViewAdapter;
import com.dzebsu.acctrip.adapters.OperationsListViewAdapter;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

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
	private OperationsListViewAdapter adapterZ;
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
		eventId=getIntent().getLongExtra("eventId",0);
		
		 setContentView(R.layout.activity_operation_list);
		ListView listView = (ListView) findViewById(R.id.op_list);
		listView.addHeaderView(createListHeader());
		fillOperationList();
		fillEventInfo(eventId);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//onSelectEvent(adapterZ.getOperationByIdInList(id).getId());
			}
		});
		
		//add filter_Event_Edittext for events names
	    SearchView eventsFilter = (SearchView) findViewById(R.id.uni_op_searchView);
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation_list, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
//think itn't needed here
//		fillEventList();
		if (mListState != null)
			((ListView) findViewById(R.id.op_list)).onRestoreInstanceState(mListState);
	    mListState = null;
	}
	
	
	//scroll position saving
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    mListState = ((ListView) findViewById(R.id.op_list)).onSaveInstanceState();
	    state.putParcelable(LIST_STATE, mListState);
	}

	public void onNewOperation(View view) {
//		Intent intent = new Intent(this, EditOperationActivity.class);
//		startActivity(intent);
	}

	private void fillOperationList() {
		OperationDataSource dataSource = new OperationDataSource(this);
		List<Operation> operations = dataSource.getOperationList(eventId);
		adapterZ= new OperationsListViewAdapter(this,  operations);
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) findViewById(R.id.op_list);
		//trigger filter to it being applied on resume
		OperationListActivity.this.adapterZ.getFilter().filter(((SearchView) findViewById(R.id.uni_op_searchView)).getQuery());
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

	public void fillEventInfo(long eventId){
		EventDataSource dataSource = new EventDataSource(this);
		Event ev=dataSource.getEventById(eventId);
		((TextView)findViewById(R.id.op_name_tv)).setText(ev.getName());
		((TextView)findViewById(R.id.op_desc_tv)).setText(ev.getDesc());
		((TextView)findViewById(R.id.op_event_id)).setText(getString(R.string.op_event_id)+String.valueOf(eventId));
		((TextView)findViewById(R.id.op_total_ops)).setText(getString(R.string.op_total_ops)+"16");
		((TextView)findViewById(R.id.op_all_expenses)).setText("-$3546");
	}
}
