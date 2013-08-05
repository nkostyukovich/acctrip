package com.dzebsu.acctrip.settings.dialogs;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import com.dzebsu.acctrip.R;

public class RestoreFromDeviceDialogPreference extends BaseBackupConfirmDialogPreference {

	public RestoreFromDeviceDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogMessage(String.format(cxt.getString(R.string.pref_restore_from_device_dialog_message),
				Environment.getExternalStorageDirectory() + "\\" + DATABASE_NAME));
	}

	@Override
	protected Integer performConfirmedAction() {
		String s = makeBackupDBToDeviceExternalMemory(true);
		if (s == null) return null;
		Editor ed = this.getEditor();
		ed.putString(this.getKey(), DateFormat.format("dd/MM/yy", Calendar.getInstance()).toString());
		ed.commit();
		return R.string.restore_success;
	}

}
