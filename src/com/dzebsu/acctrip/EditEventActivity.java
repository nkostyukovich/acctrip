package com.dzebsu.acctrip;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dzebsu.acctrip.db.datasources.EventDataSource;

public class EditEventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		Intent intent = getIntent();
		((Button) findViewById(R.id.edit_op_save_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSaveEvent();

			}
		});

		((Button) findViewById(R.id.edit_op_cancel_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCancelBtn();

			}
		});

		((EditText) this.findViewById(R.id.editEventName)).setText(intent.getStringExtra("eventName"));
		if (intent.hasExtra("edit")) {
			((EditText) this.findViewById(R.id.editEventDesc)).setText(intent.getStringExtra("eventDesc"));
		}
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(R.string.event_edit_act_title);
		}
	}

	// due different ways how to get here
	// getSupportParent
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
				// this.
				/*
				 * Intent intent = new Intent(this, EventListActivity.class);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * startActivity(intent);
				 */
				finish();
				return true;
			case R.id.save_op:
				onSaveEvent();
				return true;
			case R.id.cancel_op:
				onCancelBtn();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onSaveEvent() {
		String name = ((EditText) this.findViewById(R.id.editEventName)).getText().toString();
		if (name.isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.enter_text, Toast.LENGTH_SHORT).show();
			return;
		}
		String desc = ((EditText) this.findViewById(R.id.editEventDesc)).getText().toString();
		EventDataSource dataSource = new EventDataSource(this);
		Intent inthere = getIntent();
		if (!inthere.hasExtra("edit")) {
			// XXX 1
			long eventId = dataSource.insert(name, desc, 1);
			// go right to new event
			Intent intent = new Intent(this, OperationListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// edited intent.putExtra("toast", R.string.op_deleted);

			intent.putExtra("eventId", eventId);
			startActivity(intent);
			finish();
		} else {
			dataSource.update(inthere.getLongExtra("id", -1), name, desc, 1);
			finish();
		}
	}

	// finishes activity when cancel clicked
	public void onCancelBtn() {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText editName = (EditText) findViewById(R.id.editEventName);
		InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);

	}

}
