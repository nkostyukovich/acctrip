package com.dzebsu.acctrip.dictionary;

import java.io.Serializable;

import com.dzebsu.acctrip.R;

public enum DictionaryType implements Serializable {

	PLACE(R.string.dic_new_place_title, R.string.dic_edit_place_title, R.string.dic_place_name_lbl),
	CATEGORY(R.string.dic_new_category_title, R.string.dic_edit_category_title, R.string.dic_category_name_lbl),
	CURRENCY(R.string.dic_new_currency_title, R.string.dic_edit_currency_title, R.string.dic_currency_name_lbl);

	private DictionaryType(int newDialogTitle, int editDialogTitle, int nameFldLabel) {
		this.newDialogTitle = newDialogTitle;
		this.editDialogTitle = editDialogTitle;
		this.nameFldLabel = nameFldLabel;
	}

	private int newDialogTitle;

	private int editDialogTitle;

	private int nameFldLabel;

	public int getNewDialogTitle() {
		return newDialogTitle;
	}

	public int getEditDialogTitle() {
		return editDialogTitle;
	}

	public int getNameFldLabel() {
		return nameFldLabel;
	}

}
