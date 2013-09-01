package com.dzebsu.acctrip.operations;

public class StatListItem {

	String title;

	String value;

	public StatListItem(String title, String value) {
		this.title = title;
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
