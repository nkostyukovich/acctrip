package com.dzebsu.acctrip.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dzebsu.acctrip.DictionaryElementPickerFragment;
import com.dzebsu.acctrip.IDictionaryFragmentListener;
import com.dzebsu.acctrip.LocalizedTripMoney;
import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.dictionary.DictionaryNewDialogFragment;
import com.dzebsu.acctrip.dictionary.DictionaryType;
import com.dzebsu.acctrip.dictionary.IDialogListener;
import com.dzebsu.acctrip.dictionary.utils.TextUtils;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.settings.SettingsFragment;

public class EditEventActivity extends FragmentActivity implements IDictionaryFragmentListener {

	private static final String SAVE_STATE_KEY_PRIMARY_CURRENCY_ID = "primaryCurrencyId";

	private static final String SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE = "primaryCurrencyCode";

	private static final String INTENT_KEY_EVENT_NAME = "eventName";

	private static final String NEW_INTENT_KEY_EVENT_ID = "eventId";

	private static final String INTENT_KEY_EDIT_ID = "editId";

	private long primaryCurrencyId = -1;

	private Button primaryCurrencyBtn;

	private String currencyTitle;

	private Event editEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		setButtonsListeners();
		if (getIntent().hasExtra(INTENT_KEY_EDIT_ID)) {
			setActivityForEdit();
		} else {
			setActivityForNew();
		}
		setupActionBar();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState == null || !savedInstanceState.containsKey(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID)) {
			return;
		}
		primaryCurrencyId = savedInstanceState.getLong(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID);
		currencyTitle = savedInstanceState.getString(SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE);
		primaryCurrencyBtn.setText(currencyTitle);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID, primaryCurrencyId);
		outState.putString(SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE, currencyTitle);
	}

	private void setActivityForNew() {
		((EditText) this.findViewById(R.id.editEventName)).setText(getIntent().getStringExtra(INTENT_KEY_EVENT_NAME));
		primaryCurrencyBtn.setText(getString(R.string.event_edit_prim_curr_def));
	}

	private void setActivityForEdit() {
		editEvent = new EventDataSource(this).getEventById(getIntent().getLongExtra(INTENT_KEY_EDIT_ID, -1));
		primaryCurrencyId = editEvent.getPrimaryCurrency().getId();
		primaryCurrencyBtn.setText(getString(R.string.event_edit_prim_curr) + editEvent.getPrimaryCurrency().getCode());
		currencyTitle = editEvent.getPrimaryCurrency().getName();
		((EditText) this.findViewById(R.id.editEventDesc)).setText(editEvent.getDesc());
		((EditText) this.findViewById(R.id.editEventName)).setText(editEvent.getName());
	}

	private void setButtonsListeners() {
		((Button) findViewById(R.id.common_save_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSaveEvent();
			}
		});
		((Button) findViewById(R.id.common_cancel_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCancelBtn();
			}
		});
		primaryCurrencyBtn = ((Button) this.findViewById(R.id.editEventPrimaryCurrency));
		primaryCurrencyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeCurrencyDictionaryPicker();
			}
		});
	}

	public void invokeCurrencyDictionaryPicker() {
		DictionaryElementPickerFragment<Currency> newFragment = DictionaryElementPickerFragment.newInstance(
				Currency.class, this);
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
			getActionBar().setTitle(R.string.event_edit_title);
		}
	}

	// TODO what do lines below mean???
	// due different ways how to get here
	// getSupportParent
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
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
		LocalizedTripMoney.hideSoftKeyboard(this);
		final String name = ((EditText) this.findViewById(R.id.editEventName)).getText().toString();
		if (checkForNotEnteredData(name)) {
			return;
		}
		final String desc = ((EditText) this.findViewById(R.id.editEventDesc)).getText().toString();
		Intent inthere = getIntent();
		if (!inthere.hasExtra(INTENT_KEY_EDIT_ID)) {
			writeNewEventToDb(name, desc);
		} else {
			// TODO retrieve old values
			if (primaryCurrencyId != editEvent.getPrimaryCurrency().getId()) {
				long currencyIdBefore = editEvent.getPrimaryCurrency().getId();
				updateEventInDB(name, desc);
				startOperationListActivityWithNewPrimaryCurrency(currencyIdBefore);
			} else {
				updateEventInDB(name, desc);
				finish();
			}
		}
	}

	private void startOperationListActivityWithNewPrimaryCurrency(long currencyIdBefore) {
		// doesn't work
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// work
		LocalizedTripMoney.hideSoftKeyboard(this);
		Bundle args = new Bundle();
		args.putLong("currencyId", primaryCurrencyId);
		args.putLong("currencyIdBefore", currencyIdBefore);
		Intent intent = new Intent(this, OperationsActivity.class);
		intent.putExtra("newPrimaryCurrencyAppeared", args);
		intent.putExtra("eventId", editEvent.getId());

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private boolean checkForNotEnteredData(String name) {
		if (name.isEmpty() || primaryCurrencyId == -1) {
			String message = name.isEmpty() ? getString(R.string.enter_name) : "";
			message = TextUtils
					.addNewLine(message, primaryCurrencyId == -1 ? getString(R.string.enter_currency) : null);
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	private void updateEventInDB(final String name, final String desc) {
		new EventDataSource(EditEventActivity.this).update(editEvent.getId(), name, desc, primaryCurrencyId);
	}

	private void writeNewEventToDb(final String name, final String desc) {
		long eventId = new EventDataSource(this).insert(name, desc, primaryCurrencyId);
		PreferenceManager.getDefaultSharedPreferences(this).edit().putLong(
				SettingsFragment.CURRENT_EVENT_MODE_EVENT_ID, eventId).commit();
		// add primary currency to event currencies list with default rate
		new CurrencyPairDataSource(this).insert(eventId, primaryCurrencyId);
		startOperationListActivity(eventId);
		finish();
	}

	private void startOperationListActivity(long eventId) {
		// go right to new event
		Intent intent = new Intent(this, OperationsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// edited intent.putExtra("toast", R.string.op_deleted);
		intent.putExtra(NEW_INTENT_KEY_EVENT_ID, eventId);
		startActivity(intent);
	}

	// finishes activity when cancel clicked
	public void onCancelBtn() {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText editName = (EditText) findViewById(R.id.editEventName);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
	}

	private void changeBtnValue(String title, long id) {
		primaryCurrencyId = id;
		currencyTitle = title;
		primaryCurrencyBtn.setText(title);
	}

	private void createDictionaryEntryHelper() throws InstantiationException, IllegalAccessException {
		DictionaryNewDialogFragment<Currency> newFragment = null;
		newFragment = DictionaryNewDialogFragment.newInstance(new Currency(), DictionaryType.CURRENCY);
		newFragment.show(getSupportFragmentManager(), "dialog");
		newFragment.setOnPositiveBtnListener(new IDialogListener<Currency>() {

			@Override
			public void onSuccess(Currency entity) {
				long id = new CurrencyDataSource(EditEventActivity.this).insertEntity(entity);
				changeBtnValue(entity.getName(), id);
			}
		});
	}

	private void createDictionaryEntry() throws InstantiationException, IllegalAccessException {
		createDictionaryEntryHelper();
	}

	@Override
	public void onActionPerformed(Bundle args) throws InstantiationException, IllegalAccessException {
		if (args.getBoolean("requestNew", false)) {
			createDictionaryEntry();
		} else {
			changeBtnValue(args.getString(DictionaryElementPickerFragment.ARG_NAME), args
					.getLong(DictionaryElementPickerFragment.ARG_ID));
		}
	}
}
