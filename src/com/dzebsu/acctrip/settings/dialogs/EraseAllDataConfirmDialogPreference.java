package com.dzebsu.acctrip.settings.dialogs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.EventAccDbHelper;

public class EraseAllDataConfirmDialogPreference extends DialogPreference {

	private int a;

	private int b;

	private View view;

	private Context context;

	public EraseAllDataConfirmDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setDialogLayoutResource(R.layout.erase_data_dialog_preference);
		// all another options in preference xml
	}

	@Override
	protected void onBindDialogView(View view) {

		a = (int) (Math.random() * 5);
		b = (int) (Math.random() * 5);
		this.view = view;
		((TextView) view.findViewById(R.id.pref_erase_data_confirm_tv)).setText(String.format("%d+%d=", a, b));
		super.onBindDialogView(view);
	}

	public void reCreateAllTables() {
		EventAccDbHelper dbHelper = new EventAccDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.reCreateAllTables(db);
		db.close();
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (!positiveResult) return;
		EditText et = (EditText) view.findViewById(R.id.pref_erase_data_confirm_et);
		if (et.getText().toString().isEmpty()) return;
		if (a + b == Integer.parseInt((et.getText().toString()))) {
			reCreateAllTables();
			Toast.makeText(context, R.string.pref_all_data_erased, Toast.LENGTH_SHORT).show();
		}
	}
}
