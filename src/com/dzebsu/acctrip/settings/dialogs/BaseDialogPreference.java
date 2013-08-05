package com.dzebsu.acctrip.settings.dialogs;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.dzebsu.acctrip.R;

public abstract class BaseDialogPreference extends DialogPreference {

	protected Context cxt;

	public BaseDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.cxt = context;
	}

	@Override
	public void onDialogClosed(boolean positiveResult) {
		if (!positiveResult) return;
		Integer message;
		if ((message = performConfirmedAction()) != null) {
			Toast.makeText(cxt, cxt.getString(message), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(cxt, cxt.getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
		}
		super.onDialogClosed(positiveResult);

	}

	protected abstract Integer performConfirmedAction();

}
