package com.dzebsu.acctrip;

import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.date.utils.DateFormatter;
import com.dzebsu.acctrip.db.datasources.BaseDictionaryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.dictionary.DictionaryNewDialogFragment;
import com.dzebsu.acctrip.dictionary.IDialogListener;
import com.dzebsu.acctrip.dictionary.utils.DictUtils;
import com.dzebsu.acctrip.dictionary.utils.TextUtils;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.OperationType;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class EditOperationActivity extends FragmentActivity implements DatePickerListener, IDictionaryFragmentListener,
		SimpleDialogListener {

	private class IdBox {

		long id;

		public IdBox(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	// 1 place 2 cat 3 cur// what does user want to create now
	private Button dictionaryBtnInQuestion;

	private Event event;

	private IdBox entityInQuestion;

	private Date date;

	private IdBox placeId = new IdBox(-1);

	private IdBox categoryId = new IdBox(-1);

	private IdBox currencyId = new IdBox(-1);

	private String mode = null;

	private long opID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_operation);
		Intent intent = getIntent();
		event = new EventDataSource(this).getEventById(intent.getLongExtra("eventId", 0));
		mode = intent.getStringExtra("mode");
		opID = intent.getLongExtra("opID", 0);
		setupActionBar();
		fillDefaultValues();
	}

	private void fillDefaultValues() {
		((Button) findViewById(R.id.edit_op_save_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSaveOperation();

			}
		});

		((Button) findViewById(R.id.edit_op_cancel_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCancelBtn();

			}
		});

		((TextView) findViewById(R.id.op_edit_event_name)).setText(new EventDataSource(this)
				.getEventById(event.getId()).getName());
		((TextView) findViewById(R.id.op_edit_value_tv)).setText(getString(R.string.op_edit_value_tv));
		date = Calendar.getInstance().getTime();
		Operation defOP;
		if (mode.equals("new")) {
			defOP = new OperationDataSource(this).getLastOperationByEventId(event.getId());
		} else {
			defOP = new OperationDataSource(this).getOperationById(opID);
			((Spinner) this.findViewById(R.id.op_edit_type_spinner)).setSelection(defOP.getType().compareTo(
					OperationType.EXPENSE) == 0 ? 0 : 1);
			((EditText) this.findViewById(R.id.op_edit_value_et)).setText(String.valueOf(Math.abs(defOP.getValue())));
			((EditText) this.findViewById(R.id.op_edit_desc_et)).setText(defOP.getDesc());

			date = defOP.getDate();
		}
		String dateS = DateFormatter.formatDate(this, date);

		String place = getString(R.string.edit_event_place_def);
		String category = getString(R.string.edit_event_cat_def);
		String currency = getString(R.string.edit_event_curr_def);

		if (defOP != null) {
			Place pl = defOP.getPlace();
			Category ca = defOP.getCategory();
			Currency cu = defOP.getCurrency();

			currency = cu.getCode();
			place = pl.getName();
			category = ca.getName();

			placeId.setId(pl.getId());
			categoryId.setId(ca.getId());
			currencyId.setId(cu.getId());
		}
		final Button curBtn = ((Button) findViewById(R.id.op_edit_currency_btn));
		final Button placeBtn = ((Button) findViewById(R.id.op_edit_place_btn));
		final Button catBtn = ((Button) findViewById(R.id.op_edit_category_btn));
		Button dateBtn = ((Button) findViewById(R.id.op_edit_date_btn));
		Button timeBtn = ((Button) findViewById(R.id.op_edit_time_btn));
		curBtn.setText(currency);
		catBtn.setText(category);
		placeBtn.setText(place);
		dateBtn.setText(dateS);
		timeBtn.setText(DateFormatter.formatTime(this, date));
		curBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(Currency.class);
				entityInQuestion = currencyId;
				dictionaryBtnInQuestion = curBtn;
			}
		});
		catBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(Category.class);
				entityInQuestion = categoryId;
				dictionaryBtnInQuestion = catBtn;
			}
		});
		placeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invokeDictionaryPicker(Place.class);
				entityInQuestion = placeId;
				dictionaryBtnInQuestion = placeBtn;
			}
		});
		dateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment newFragment = new DatePickerFragment();
				Bundle args = new Bundle();
				if (mode.equals("edit") || date != null) {
					args.putLong("date", DateFormatter.convertDateToLong(date));
				}
				newFragment.setArguments(args);
				newFragment.setDataPickerListener(EditOperationActivity.this);
				newFragment.show(getSupportFragmentManager(), "datePicker");

			}
		});
		timeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerFragment newFragment = new TimePickerFragment();
				Bundle args = new Bundle();
				if (mode.equals("edit") || date != null) {
					args.putLong("time", DateFormatter.convertDateToLong(date));
				}
				newFragment.setArguments(args);
				newFragment.setDatePickerListener(EditOperationActivity.this);
				newFragment.show(getSupportFragmentManager(), "timePicker");

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
				finish();
				return true;
			case R.id.open_dictionaries:
				onOpenDictionaries();
				return true;
			case R.id.save_op:
				onSaveOperation();
				return true;
			case R.id.cancel_op:
				onCancelBtn();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onOpenDictionaries() {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);
	}

	private String desc;

	private OperationType opType;

	private String value;

	public void onSaveOperation() {
		value = ((EditText) this.findViewById(R.id.op_edit_value_et)).getText().toString();
		if (value.isEmpty() || categoryId.getId() == -1 || currencyId.getId() == -1 || placeId.getId() == -1) {
			String message;
			message = value.isEmpty() ? getString(R.string.enter_value) : "";
			message = TextUtils
					.addNewLine(message, currencyId.getId() == -1 ? getString(R.string.pick_currency) : null);
			message = TextUtils.addNewLine(message, placeId.getId() == -1 ? getString(R.string.pick_place) : null);
			message = TextUtils
					.addNewLine(message, categoryId.getId() == -1 ? getString(R.string.pick_category) : null);
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			return;
		}
		desc = ((EditText) this.findViewById(R.id.op_edit_desc_et)).getText().toString();
		opType = ((Spinner) this.findViewById(R.id.op_edit_type_spinner)).getSelectedItem().toString()
				.equals("Expense") ? OperationType.EXPENSE : OperationType.INCOME;

		value = opType.compareTo(OperationType.EXPENSE) == 0 ? "-" + value : value;
		if (!mode.equals("edit")) {
			suggestEditCurrencyRate();
		} else {
			suggestEditCurrencyRate();
		}

	}

	private void suggestEditCurrencyRate() {

		if (new CurrencyPairDataSource(this).getCurrencyPairByValues(event.getId(), currencyId.getId()) != null) {
			writeChangesToDB();
			return;
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		String message = String.format(getString(R.string.warning_first_time_curr),
				((Button) findViewById(R.id.op_edit_currency_btn)).getText().toString(), event.getName(), event
						.getPrimaryCurrency().getCode());
		alert.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.warning_word).setMessage(message)
				.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new CurrencyPairDataSource(EditOperationActivity.this)
								.insert(event.getId(), currencyId.getId());
						writeChangesToDB();
					}
				}).setPositiveButton(R.string.provide, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						EditCurrencyPairDialog newDialog = EditCurrencyPairDialog.newInstance(event
								.getPrimaryCurrency(), new CurrencyPair(event.getId(), new CurrencyDataSource(
								EditOperationActivity.this).getEntityById(currencyId.getId())));

						new CurrencyPairDataSource(EditOperationActivity.this)
								.insert(event.getId(), currencyId.getId());
						newDialog.setListener(EditOperationActivity.this);
						newDialog.show(getFragmentManager(), "EditDialog");
					}
				});
		alert.create().show();
	}

	private void writeChangesToDB() {
		if (mode.equals("edit")) {
			updateOperation();
		} else {
			insertOperation();
		}
	}

	private void insertOperation() {
		OperationDataSource dataSource = new OperationDataSource(this);
		dataSource.insert(date, desc, CurrencyUtils.getDouble(value), opType, event.getId(), categoryId.getId(),
				currencyId.getId(), placeId.getId());
		finish();
		Toast.makeText(getApplicationContext(), R.string.op_created, Toast.LENGTH_SHORT).show();
	}

	private void updateOperation() {
		OperationDataSource dataSource = new OperationDataSource(this);
		dataSource.update(opID, date, desc, CurrencyUtils.getDouble(value), opType, event.getId(), categoryId.getId(),
				currencyId.getId(), placeId.getId());
		finish();
		Toast.makeText(getApplicationContext(), R.string.op_changed, Toast.LENGTH_SHORT).show();
	}

	// finishes activity when cancel clicked
	public void onCancelBtn() {
		finish();
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
						EditOperationActivity.this)).insertEntity(entity);
				changeBtnValue(entity instanceof Currency ? ((Currency) entity).getCode() : entity.getName(), id);
			}
		});
	}

	private void createDictionaryEntry(Class<? extends BaseDictionary> clazz) throws InstantiationException,
			IllegalAccessException {
		createDictionaryEntryHelper(clazz);
	}

	private void changeBtnValue(String title, long id) {
		entityInQuestion.setId(id);
		dictionaryBtnInQuestion.setText(title);
	}

	public <T extends BaseDictionary> void invokeDictionaryPicker(Class<T> itemType) {
		DictionaryElementPickerFragment<T> newFragment = DictionaryElementPickerFragment.newInstance(itemType, this);
		newFragment.show(getSupportFragmentManager(), "dialog");
		newFragment.setOnPickFragmentListener(this);
	}

	@Override
	public void onActionPerformed(Bundle args) throws InstantiationException, IllegalAccessException {
		if (args.getBoolean("requestNew", false)) {
			createDictionaryEntry((Class<? extends BaseDictionary>) args.getSerializable("clazz"));
		} else {
			changeBtnValue(args.getString("picked"), args.getLong("pickedId"));
		}
	}

	@Override
	public void onDatePicked(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		date = c.getTime();
		((Button) findViewById(R.id.op_edit_date_btn)).setText(DateFormatter.formatDate(this, date));
	}

	@Override
	public void onTimePicked(int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		date = c.getTime();
		((Button) findViewById(R.id.op_edit_time_btn)).setText(DateFormatter.formatTime(this, date));
	}

	@Override
	public void positiveButtonDialog(Bundle args) {
		writeChangesToDB();

	}

	@Override
	public void negativeButtonDialog(Bundle args) {
		writeChangesToDB();

	}

	@Override
	public void anotherDialogAction(Bundle args) {
		writeChangesToDB();

	}
}
