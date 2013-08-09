package com.dzebsu.acctrip.models;

import com.dzebsu.acctrip.models.dictionaries.Currency;

public class Event extends BaseModel {

	private String name;

	private String desc;

	private Currency primaryCurrency;

	// TODO need Currency object

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}

	public Event() {
		super();
	}

	public Event(long id) {
		super(id);
	}

	public Event(long id, String name, String desc, Currency primaryCurrency) {
		super(id);
		this.name = name;
		this.desc = desc;
		this.primaryCurrency = primaryCurrency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return name;
	}
}
