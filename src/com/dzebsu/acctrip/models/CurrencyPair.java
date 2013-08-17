package com.dzebsu.acctrip.models;

import com.dzebsu.acctrip.models.dictionaries.Currency;

public class CurrencyPair {

	//TODO hashcode neeeeed
	private Long eventId;

	private Currency secondCurrency;

	private double rate;

	public CurrencyPair() {
		this.eventId = -1L;
		this.secondCurrency = null;
		this.rate = 1.;
	}

	public CurrencyPair(Long eventId, Currency secondCurrency, double rate) {
		this.eventId = eventId;
		this.secondCurrency = secondCurrency;
		this.rate = rate;
	}

	public CurrencyPair(Long eventId, Currency secondCurrency) {
		this.eventId = eventId;
		this.secondCurrency = secondCurrency;
		this.rate = 1;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
