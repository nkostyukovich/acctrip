package com.dzebsu.acctrip.models;

import com.dzebsu.acctrip.models.dictionaries.Currency;

public class CurrencyPair {

	private Long eventId;

	private Currency firstCurrency;

	private Currency secondCurrency;

	// 1/2
	private double rate;

	public CurrencyPair(Long eventId, Currency firstCurrency, Currency secondCurrency, double rate) {
		this.eventId = eventId;
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.rate = rate;
	}

	public CurrencyPair(Long eventId, Currency firstCurrency, Currency secondCurrency) {
		this.eventId = eventId;
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.rate = 1;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
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
