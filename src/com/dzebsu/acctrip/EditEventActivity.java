package com.dzebsu.acctrip;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dzebsu.acctrip.db.datasources.EventDataSource;

public class EditEventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		// Show the Up button in the action bar.
		String name=getIntent().getStringExtra("eventName");
		((EditText) this.findViewById(R.id.editEventName)).setText(name);
		setupActionBar();
		//TODO not works
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_event, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSaveEvent(View view) {
		String name = ((EditText) this.findViewById(R.id.editEventName)).getText().toString();
		if(name.isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.enter_text, Toast.LENGTH_SHORT).show();
			return;
		}
		String desc = ((EditText) this.findViewById(R.id.editEventDesc)).getText().toString();
		EventDataSource dataSource = new EventDataSource(this);
		long eventId=dataSource.insert(name, desc);
		//finish(); go right to new event
		Intent intent = new Intent(this, OperationListActivity.class);
		intent.putExtra("eventId", eventId);
		startActivity(intent);
	}
	//finishes activity when cancel clicked
	public void onCancelBtn(View view){
		finish();
	}
	
	public void onNewOperation(View view){
		//TODO add operation here
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		EditText editName= (EditText) findViewById(R.id.editEventName);
		InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
		
	}

}
