package com.dzebsu.acctrip.settings.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.dzebsu.acctrip.R;

public class LanguagePickerListPreference extends ListPreference {

	public LanguagePickerListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			new AlertDialog.Builder(this.getContext()).setIcon(android.R.drawable.stat_notify_error).setTitle(
					R.string.pref_restart_dialog_title).setMessage(R.string.pref_restart_dialog_message)
					.setPositiveButton(R.string.close_app, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								LanguagePickerListPreference.this.finalize();
								System.exit(0);
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).create().show();
		}
	}
}
