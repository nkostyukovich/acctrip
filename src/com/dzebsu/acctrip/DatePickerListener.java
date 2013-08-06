package com.dzebsu.acctrip;

public interface DatePickerListener {

	public void onDatePicked(int year, int month, int day);

	public void onTimePicked(int hourOfDay, int minute);
}
