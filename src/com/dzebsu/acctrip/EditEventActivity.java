package com.dzebsu.acctrip;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dzebsu.acctrip.db.datasources.BaseDictionaryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.dictionary.DictionaryNewDialogFragment;
import com.dzebsu.acctrip.dictionary.IDialogListener;
import com.dzebsu.acctrip.dictionary.utils.DictUtils;
import com.dzebsu.acctrip.dictionary.utils.TextUtils;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class EditEventActivity extends FragmentActivity implements IDictionaryFragmentListener {

	private static final String INTENT_KEY_EVENT_NAME = "eventName";

	private static final String NEW_INTENT_KEY_EVENT_ID = "eventId";

	private static final String INTENT_KEY_EDIT_ID = "editId";

	private long primaryCurrencyId = -1;

	private Button primaryCurrencyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		Intent intent = getIntent();
		((Button) findViewById(R.id.edit_event_save_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSaveEvent();

			}
		});

		((Button) findViewById(R.id.edit_event_cancel_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCancelBtn();

			}
		});
		primaryCurrencyBtn = ((Button) this.findViewById(R.id.edit_event_primary_currency));
		primaryCurrencyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(Currency.class);
			}
		});

		if (intent.hasExtra(INTENT_KEY_EDIT_ID)) {
			Event ev = new EventDataSource(this).getEventById(intent.getLongExtra(INTENT_KEY_EDIT_ID, -1));
			primaryCurrencyId = ev.getPrimaryCurrency().getId();
			primaryCurrencyBtn.setText(getString(R.string.edit_event_prim_curr) + ev.getPrimaryCurrency().getCode());
			((EditText) this.findViewById(R.id.editEventDesc)).setText(ev.getDesc());
			((EditText) this.findViewById(R.id.editEventName)).setText(ev.getName());
		} else {
			((EditText) this.findViewById(R.id.editEventName)).setText(intent.getStringExtra(INTENT_KEY_EVENT_NAME));
			primaryCurrencyBtn.setText(getString(R.string.edit_event_prim_curr)
					+ getString(R.string.edit_event_prim_curr_def));
		}
		setupActionBar();
	}

	public <T extends BaseDictionary> void invokeDictionaryPicker(Class<T> itemType) {
		DictionaryElementPickerFragment<T> newFragment = DictionaryElementPickerFragment.newInstance(itemType, this);
		newFragment.show(getSupportFragmentManager(), "dialog");
		newFragment.setOnPickFragmentListener(this);
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
		if (name.isEmpty() || primaryCurrencyId == -1) {
			String message = name.isEmpty() ? getString(R.string.enter_name) : "";
			message = TextUtils
					.addNewLine(message, primaryCurrencyId == -1 ? getString(R.string.enter_currency) : null);
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			return;
		}
		String desc = ((EditText) this.findViewById(R.id.editEventDesc)).getText().toString();
		EventDataSource dataSource = new EventDataSource(this);
		Intent inthere = getIntent();
		if (!inthere.hasExtra(INTENT_KEY_EDIT_ID)) {
			long eventId = dataSource.insert(name, desc, primaryCurrencyId);

			// add primary currency to event currencies list
			new CurrencyPairDataSource(this).insert(eventId, primaryCurrencyId);
			// go right to new event
			Intent intent = new Intent(this, OperationListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// edited intent.putExtra("toast", R.string.op_deleted);

			intent.putExtra(NEW_INTENT_KEY_EVENT_ID, eventId);
			startActivity(intent);
			finish();
		} else {
			// TODO check for currency!!!!!
			// XXX
			// TODO
			dataSource.update(inthere.getLongExtra(INTENT_KEY_EDIT_ID, -1), name, desc, primaryCurrencyId);
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

	private void changeBtnValue(String title, long id) {
		primaryCurrencyId = id;
		primaryCurrencyBtn.setText(getString(R.string.edit_event_prim_curr) + title);
	}

	private <T extends BaseDictionary> void createDictionaryEntryHelper(Class<T> clazz) throws InstantiationException,
			IllegalAccessException {
		DictionaryNewDialogFragment<T> newFragment = null;
		newFragment = DictionaryNewDialogFragment.newInstance(clazz.newInstance(), DictUtils.getDictionaryType(clazz));
		newFragment.show(getSupportFragmentManager(), "dialog");
		newFragment.setOnPositiveBtnListener(new IDialogListener<T>() {

			@Override
			public void onSuccess(T entity) {
				long id = ((BaseDictionaryDataSource<T>) DictUtils.getEntityDataSourceInstance(entity.getClass(),
						EditEventActivity.this)).insertEntity(entity);
				changeBtnValue(entity instanceof Currency ? ((Currency) entity).getCode() : entity.getName(), id);
			}
		});
	}

	private void createDictionaryEntry(Class<? extends BaseDictionary> clazz) throws InstantiationException,
			IllegalAccessException {
		createDictionaryEntryHelper(clazz);
	}

	@Override
	public void onActionPerformed(Bundle args) throws InstantiationException, IllegalAccessException {
		if (args.getBoolean("requestNew", false)) {
			createDictionaryEntry((Class<? extends BaseDictionary>) args.getSerializable("clazz"));
		} else {
			changeBtnValue(args.getString("picked"), args.getLong("pickedId"));
		}
	}
}
