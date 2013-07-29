package com.dzebsu.acctrip;

import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.models.Operation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class OperationDetailsActivity extends Activity {

	private Operation operation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operation_details);
		Intent intent=getIntent();
		long id=intent.getLongExtra("opID",0);
		operation=new OperationDataSource(this).getOperationById(id);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			String evName=getIntent().getStringExtra("evName");
			evName=evName.length()>18?evName.substring(0,18)+"..":evName;
			getActionBar().setTitle(evName);
		}
	}

	//due different ways how to get here
	//getSupportParent
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operation_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		//	this.
			/*Intent intent = new Intent(this, EventListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);*/
			finish();
			return true;
		case R.id.edit_op:
			editOperationInvoke();
			return true;
		case R.id.delete_op:
			deleteOperation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void editOperationInvoke() {
		// TODO Auto-generated method stub
		
	}

	private void deleteOperation() {
		new AlertDialog.Builder(this).setTitle("Delete dialog").setMessage(String.format(getString(R.string.confirm_del), "this operation")).setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(R.string.dic_del, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						new OperationDataSource(OperationDetailsActivity.this).deleteById(operation.getId());
						Intent intent = new Intent(OperationDetailsActivity.this, OperationListActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra	("toast", R.string.op_deleted);
						intent.putExtra("eventId", operation.getEvent().getId());
						startActivity(intent);
					}
				}).setNegativeButton(android.R.string.no, null).show();
		
	}
}
