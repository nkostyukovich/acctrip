package com.dzebsu.acctrip.currency.utils;

public class CurrencyUtils {

	public static double convertCurrs(double amountFirstCurr, double rate1to2) {
		return amountFirstCurr / rate1to2;
	}
}
