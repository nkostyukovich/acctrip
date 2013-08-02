package com.dzebsu.acctrip.dictionary;

import java.io.Serializable;

import com.dzebsu.acctrip.R;

public enum DictionaryType implements Serializable {

	PLACE(R.string.place),
	CATEGORY(R.string.category),
	CURRENCY(R.string.currency);

	private DictionaryType(int elementName) {
		this.elementName = elementName;

	}

	private int elementName;

	public int getElementName() {
		return elementName;
	}

	public void setElementName(int elementName) {
		this.elementName = elementName;
	}

}
