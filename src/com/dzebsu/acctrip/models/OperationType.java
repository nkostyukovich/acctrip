package com.dzebsu.acctrip.models;

import android.content.Context;
import android.content.res.Resources;

public enum OperationType {
	
	INCOME, 	
	EXPENSE;
	
	private OperationType() {
	}

	public String getLabel(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(this.name(), "string", context.getPackageName());
        if (0 != resId) {
            return (res.getString(resId));
        }
        return (name());
    }
}
