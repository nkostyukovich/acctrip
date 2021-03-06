package com.dzebsu.acctrip.models;

import java.util.Date;

import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.models.dictionaries.Place;

public class Operation extends BaseModel {

	private Category category;

	private String desc;

	private OperationType type;

	private double value;

	private Currency currency;

	private Date date;

	private Event event;

	private Place place;

	public Operation() {
		super();
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
}
