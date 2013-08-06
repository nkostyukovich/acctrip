package com.dzebsu.acctrip;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

	private DatePickerListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int hourOfDay;
		int minute;
		boolean is24view = android.text.format.DateFormat.is24HourFormat(this.getActivity());

		if (!this.getArguments().containsKey("time")) {
			final Calendar c = Calendar.getInstance();
			hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
		} else {
			final Calendar c = Calendar.getInstance();
			c.setTimeInMillis(getArguments().getLong("time"));
			hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
		}
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hourOfDay, minute, is24view);
	}

	public void setDatePickerListener(DatePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		listener.onTimePicked(hourOfDay, minute);
		this.dismiss();

	}

}
