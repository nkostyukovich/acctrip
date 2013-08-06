package com.dzebsu.acctrip;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year;
		int month;
		int day;// TODO do not work properly
		if (!this.getArguments().containsKey("date")) {
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		} else {
			final Calendar c = Calendar.getInstance();
			c.setTimeInMillis(getArguments().getLong("date"));
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	private DatePickerListener listener;

	public void setDataPickerListener(DatePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		listener.onDatePicked(year, month, day);
		this.dismiss();
	}

}
