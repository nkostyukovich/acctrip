package com.dzebsu.acctrip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dzebsu.acctrip.db.ConvertUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.dictionary.utils.TextUtils;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.OperationType;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class EditOperationActivity extends FragmentActivity implements DatePickerListener, IDictionaryFragmentListener {

	// 1 place 2 cat 3 cur// what does user want to create now
	private int newItemClass = 0;

	private long eventId = 0;

	private String place = "Place";

	private String category = "Category";

	private String currency = "Curr$";

	private Date date;

	// TODO default values not work
	private long placeId = -1;

	private long categoryId = -1;

	private long currencyId = -1;

	private String mode = null;

	private long opID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_operation);
		Intent intent = getIntent();
		eventId = intent.getLongExtra("eventId", 0);
		mode = intent.getStringExtra("mode");
		opID = intent.getLongExtra("opID", 0);
		setupActionBar();
		fillDefaultValues();
	}

	private void fillDefaultValues() {
		((TextView) findViewById(R.id.op_edit_event_name)).setText(new EventDataSource(this).getEventById(eventId)
				.getName());
		((TextView) findViewById(R.id.op_edit_value_tv)).setText(getString(R.string.op_edit_value_tv));
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
		date = Calendar.getInstance().getTime();
		String dateS = sdf.format(date);
		Operation defOP;
		if (mode.equals("new")) {
			defOP = new OperationDataSource(this).getLastOperationByEventId(eventId);
		} else {
			defOP = new OperationDataSource(this).getOperationById(opID);
			((Spinner) this.findViewById(R.id.op_edit_type_spinner)).setSelection(defOP.getType().compareTo(
					OperationType.EXPENSE) == 0 ? 0 : 1);
			((EditText) this.findViewById(R.id.op_edit_value_et)).setText(String.valueOf(Math.abs(defOP.getValue())));
			((EditText) this.findViewById(R.id.op_edit_desc_et)).setText(defOP.getDesc());
			dateS = sdf.format(defOP.getDate());
			date = defOP.getDate();
		}

		if (defOP != null) {
			Place pl = defOP.getPlace();
			Category ca = defOP.getCategory();
			Currency cu = defOP.getCurrency();

			currency = cu.getCode();
			place = pl.getName();
			category = ca.getName();

			placeId = pl.getId();
			categoryId = ca.getId();
			currencyId = cu.getId();
		}
		Button curBtn = ((Button) findViewById(R.id.op_edit_currency_btn));
		Button placeBtn = ((Button) findViewById(R.id.op_edit_place_btn));
		Button catBtn = ((Button) findViewById(R.id.op_edit_category_btn));
		Button dateBtn = ((Button) findViewById(R.id.op_edit_date_btn));
		curBtn.setText(currency);
		catBtn.setText(category);
		placeBtn.setText(place);
		dateBtn.setText(dateS);
		curBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(3);

			}
		});
		catBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(2);

			}
		});
		placeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(1);

			}
		});
		dateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO get day of month from date
				DatePickerFragment newFragment = new DatePickerFragment();
				Bundle args = new Bundle();
				if (mode.equals("edit") || date != null) {
					args.putLong("date", ConvertUtils.convertDateToLong(date));
				}
				newFragment.setArguments(args);
				newFragment.setDataPickerListener(EditOperationActivity.this);
				newFragment.show(getSupportFragmentManager(), "datePicker");

			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(R.string.title_activity_edit_operation);
		}
	}

	// due different ways how to get here
	// getSupportParent
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_operation, menu);

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
			case R.id.open_dictionaries:
				onOpenDictionaries();
				return true;
			case R.id.save_op:
				onSaveOperation(item.getActionView());
				return true;
			case R.id.cancel_op:
				onCancelBtn(item.getActionView());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onOpenDictionaries() {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);
	}

	public void onSaveOperation(View view) {
		String value = ((EditText) this.findViewById(R.id.op_edit_value_et)).getText().toString();
		if (value.isEmpty() || categoryId == -1 || currencyId == -1 || placeId == -1) {
			String message;

			message = value.isEmpty() ? getString(R.string.enter_value) : "";

			message = TextUtils.addNewLine(message, currencyId == -1 ? getString(R.string.pick_currency) : null);
			message = TextUtils.addNewLine(message, placeId == -1 ? getString(R.string.pick_place) : null);
			message = TextUtils.addNewLine(message, categoryId == -1 ? getString(R.string.pick_category) : null);

			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			return;
		}
		String desc = ((EditText) this.findViewById(R.id.op_edit_desc_et)).getText().toString();
		OperationType opType = ((Spinner) this.findViewById(R.id.op_edit_type_spinner)).getSelectedItem().toString()
				.equals("Expense") ? OperationType.EXPENSE : OperationType.INCOME;

		OperationDataSource dataSource = new OperationDataSource(this);
		value = opType.compareTo(OperationType.EXPENSE) == 0 ? "-" + value : value;

		if (!mode.equals("edit")) {

			dataSource.insert(date, desc, Double.parseDouble(value), opType, eventId, categoryId, currencyId, placeId);
			// go right to event
			finish();
			Toast.makeText(getApplicationContext(), R.string.op_created, Toast.LENGTH_SHORT).show();
		} else {
			// TODO update everything
			dataSource.update(opID, date, desc, Double.parseDouble(value), opType, eventId, categoryId, currencyId,
					placeId);
			finish();
			Toast.makeText(getApplicationContext(), R.string.op_changed, Toast.LENGTH_SHORT).show();
		}

	}

	// finishes activity when cancel clicked
	public void onCancelBtn(View view) {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void createDictionaryEntry() {
		/*
		 * int title = 1, name = 1; switch (newItemClass) { case 1: title =
		 * R.string.dic_new_place_title; name = R.string.dic_place_name_lbl;
		 * break; case 2: title = R.string.dic_new_category_title; name =
		 * R.string.dic_category_name_lbl; break; case 3: title =
		 * R.string.dic_new_currency_title; name =
		 * R.string.dic_currency_name_lbl; break; }
		 */
		// TODO allow create dic entry and provide response in listener
		/*
		 * DictionaryNewDialogFragment<T> newFragment =
		 * DictionaryNewDialogFragment.newInstance(clazz.newInstance(),
		 * dictType); newFragment.show(getFragmentManager(), "dialog");
		 * newFragment.setOnPositiveBtnListener(this);
		 */
		// DictionaryNewDialogFragment newFragment =
		// DictionaryNewDialogFragment.prepareDialog(newItemClass,"new", name,
		// getString(title), R.string.save, null, null, 0);
		// newFragment.show(getSupportFragmentManager(), "dialog");
		// newFragment.setOnPositiveBtnListener(this);
	}

	// @Override
	// public void onSuccess(Bundle args) {
	// String mode = args.getString("mode");
	// if (mode.equals("new")) {
	// String name = args.getString("name");
	// switch (newItemClass) {
	// case 1:
	// changeBtnValue(name, new PlaceDataSource(this).insert(name));
	// break;
	// case 2:
	// changeBtnValue(name, new CategoryDataSource(this).insert(name));
	// break;
	// case 3:
	// changeBtnValue(args.getString("code"), new CurrencyDataSource(this)
	// .insert(name, args.getString("code")));
	// break;
	// }
	//
	// }
	//
	// }

	private void changeBtnValue(String title, long id) {
		switch (newItemClass) {
			case 1:
				place = title;
				placeId = id;
				((Button) findViewById(R.id.op_edit_place_btn)).setText(place);
				break;
			case 2:
				category = title;
				categoryId = id;
				((Button) findViewById(R.id.op_edit_category_btn)).setText(category);
				break;
			case 3:
				currency = title;
				currencyId = id;
				((Button) findViewById(R.id.op_edit_currency_btn)).setText(currency);
				break;
		}
	}

	public void invokeDictionaryPicker(int itemType) {
		newItemClass = itemType;
		DictionaryElementPickerFragment newFragment = DictionaryElementPickerFragment.prepareDialog(newItemClass);
		newFragment.show(getSupportFragmentManager(), "dialog");
		newFragment.setOnPickFragmentListener(this);
		// TODO make dialog fixed height
	}

	@Override
	public void onValueChanged(Bundle args) {
		if (args.getBoolean("requestNew", false)) {
			createDictionaryEntry();
		} else {
			switch (newItemClass) {
				case 1:
					changeBtnValue(args.getString("picked"), args.getLong("pickedId"));
					break;
				case 2:
					changeBtnValue(args.getString("picked"), args.getLong("pickedId"));
					break;
				case 3:
					changeBtnValue(new CurrencyDataSource(this).getEntityById(args.getLong("pickedId")).getCode(), args
							.getLong("pickedId"));
					break;
			}
		}
	}

	@Override
	public void onDatePicked(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
		((Button) findViewById(R.id.op_edit_date_btn)).setText(sdf.format(date));
	}

}
