package com.dzebsu.acctrip.models;

public class CurrencyPair extends BaseModel {

	private long firstCurrency;

	private long secondCurrency;

	// 1/2
	private double rate;

	public CurrencyPair(long id, long firstCurrency, long secondCurrency, double rate) {
		super(id);
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.rate = rate;
	}

	public long getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(long firstCurrency) {
		this.firstCurrency = firstCurrency;
	}

	public long getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(long secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
