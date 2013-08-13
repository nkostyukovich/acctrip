package com.dzebsu.acctrip;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

	private static final String SAVE_STATE_KEY_PRIMARY_CURRENCY_ID = "primaryCurrencyId";

	private static final String SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE = "primaryCurrencyCode";

	private static final String INTENT_KEY_EVENT_NAME = "eventName";

	private static final String NEW_INTENT_KEY_EVENT_ID = "eventId";

	private static final String INTENT_KEY_EDIT_ID = "editId";

	private long primaryCurrencyId = -1;

	private Button primaryCurrencyBtn;

	private String currencyCode;

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
		if (savedInstanceState == null || !savedInstanceState.containsKey(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID)) return;
		primaryCurrencyId = savedInstanceState.getLong(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID);
		currencyCode = savedInstanceState.getString(SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE);
		primaryCurrencyBtn.setText(currencyCode);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SAVE_STATE_KEY_PRIMARY_CURRENCY_ID, primaryCurrencyId);
		outState.putString(SAVE_STATE_KEY_PRIMARY_CURRENCY_CODE, currencyCode);
	}

	private void setActivityForNew() {
		((EditText) this.findViewById(R.id.editEventName)).setText(getIntent().getStringExtra(INTENT_KEY_EVENT_NAME));
		primaryCurrencyBtn.setText(getString(R.string.edit_event_prim_curr)
				+ getString(R.string.edit_event_prim_curr_def));
	}

	private void setActivityForEdit() {
		editEvent = new EventDataSource(this).getEventById(getIntent().getLongExtra(INTENT_KEY_EDIT_ID, -1));
		primaryCurrencyId = editEvent.getPrimaryCurrency().getId();
		primaryCurrencyBtn.setText(getString(R.string.edit_event_prim_curr) + editEvent.getPrimaryCurrency().getCode());
		currencyCode = editEvent.getPrimaryCurrency().getCode();
		((EditText) this.findViewById(R.id.editEventDesc)).setText(editEvent.getDesc());
		((EditText) this.findViewById(R.id.editEventName)).setText(editEvent.getName());
	}

	private void setButtonsListeners() {
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
		final String name = ((EditText) this.findViewById(R.id.editEventName)).getText().toString();
		if (checkForNotEnteredData(name)) return;
		final String desc = ((EditText) this.findViewById(R.id.editEventDesc)).getText().toString();
		Intent inthere = getIntent();
		if (!inthere.hasExtra(INTENT_KEY_EDIT_ID)) {
			writeNewEventToDb(name, desc);
		} else {
			// TODO retrieve old values
			if (primaryCurrencyId != editEvent.getPrimaryCurrency().getId()) {
				invokeNewPrimaryCurrencyWarningDialog(name, desc);
			} else {
				updateEventInDB(name, desc);
				finish();
			}
		}
	}

	private boolean checkForNotEnteredData(final String name) {
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

	private void invokeNewPrimaryCurrencyWarningDialog(final String name, final String desc) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		String message = String.format(getString(R.string.warning_new_prim_curr), currencyCode, editEvent.getName());
		alert.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.warning_word).setMessage(message)
				.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateEventWithDefRates(name, desc);
						finish();
					}

				}).setPositiveButton(R.string.provide, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateEventAndEditRates(name, desc);
					}
				}).create().show();
	}

	private void updateEventAndEditRates(final String name, final String desc) {
		updateEventWithDefRates(name, desc);
		startEditCurrencyPairsActivity();
	}

	private void startEditCurrencyPairsActivity() {
		Intent intent = new Intent(EditEventActivity.this, EventCurrenciesListActivity.class);
		intent.putExtra(NEW_INTENT_EXTRA_EVENT_ID, editEvent.getId());
		EditEventActivity.this.startActivity(intent);
		finish();
	}

	private void updateEventWithDefRates(final String name, final String desc) {
		updateEventInDB(name, desc);
		updateCurrencyPairsWithDefRates();
	}

	private void updateCurrencyPairsWithDefRates() {
		CurrencyPairDataSource currpairsData = new CurrencyPairDataSource(EditEventActivity.this);
		currpairsData.deleteCurrencyPairIfUnused(editEvent.getId(), editEvent.getPrimaryCurrency().getId(),
				primaryCurrencyId, primaryCurrencyId);
		if (currpairsData.getCurrencyPairByValues(editEvent.getId(), primaryCurrencyId) == null) {
			currpairsData.insert(editEvent.getId(), primaryCurrencyId);
		}
		currpairsData.updateRatesBunchToDefaultValueByEventId(editEvent.getId());
	}

	private void writeNewEventToDb(final String name, final String desc) {
		long eventId = new EventDataSource(this).insert(name, desc, primaryCurrencyId);
		// add primary currency to event currencies list with default rate
		new CurrencyPairDataSource(this).insert(eventId, primaryCurrencyId);
		startOperationListActivity(eventId);
		finish();
	}

	private void startOperationListActivity(long eventId) {
		// go right to new event
		Intent intent = new Intent(this, OperationListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// edited intent.putExtra("toast", R.string.op_deleted);
		intent.putExtra(NEW_INTENT_KEY_EVENT_ID, eventId);
		startActivity(intent);
	}

	private static final String NEW_INTENT_EXTRA_EVENT_ID = "eventId";

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
		currencyCode = title;
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
