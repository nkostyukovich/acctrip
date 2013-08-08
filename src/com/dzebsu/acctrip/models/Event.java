package com.dzebsu.acctrip.models;

public class Event extends BaseModel {

	private String name;

	private String desc;

	private long primaryCurrencyId;

	// TODO need Currency object

	public long getPrimaryCurrencyId() {
		return primaryCurrencyId;
	}

	public void setPrimaryCurrencyId(long primaryCurrencyId) {
		this.primaryCurrencyId = primaryCurrencyId;
	}

	public Event() {
		super();
	}

	public Event(long id) {
		super(id);
	}

	public Event(long id, String name, String desc, long primaryCurrencyId) {
		super(id);
		this.name = name;
		this.desc = desc;
		this.primaryCurrencyId = primaryCurrencyId;
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
