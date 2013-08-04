package com.dzebsu.acctrip.settings.dialogs;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import com.dzebsu.acctrip.R;

public class BackupOnDeviceDialogPreference extends BaseBackupConfirmDialogPreference {

	// XXX maybe make abstract base class with perform action abstract method?
	public BackupOnDeviceDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogMessage(String.format(cxt.getString(R.string.pref_backup_database_device_dialog_message),
				Environment.getExternalStorageDirectory().getPath()));
	}

	@Override
	public Integer performConfirmedAction() {
		String s = makeBackupDBToDeviceExternalMemory();
		if (s == null) return null;
		Editor ed = this.getEditor();
		ed.putString(this.getKey(), DateFormat.format("dd/MM/yy", Calendar.getInstance()).toString() + "@" + s);
		ed.commit();
		return R.string.backup_copy_success;
	}
}
