package com.dzebsu.acctrip.settings.dialogs;

import android.content.Context;
import android.util.AttributeSet;

import com.dzebsu.acctrip.R;

public class MakeDefaultSettingsDialogPreference extends BaseBackupConfirmDialogPreference {

	public MakeDefaultSettingsDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected Integer performConfirmedAction() {
		if (this.getSharedPreferences().edit().clear().commit())
			return R.string.def_applied;
		else return null;
		// TODO notify that all changed
	}

}
