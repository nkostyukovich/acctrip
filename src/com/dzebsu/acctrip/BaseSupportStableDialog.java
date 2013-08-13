package com.dzebsu.acctrip;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BaseSupportStableDialog extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance()) getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

}
