package com.dzebsu.acctrip.currency.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyUtils {

	public static double convertCurrs(double amountFirstCurr, double rate1to2) {
		return amountFirstCurr * rate1to2;
	}

	public static String formatAfterPoint(double value) {
		DecimalFormat money = new DecimalFormat("#.####");
		money.setRoundingMode(RoundingMode.UP);
		return money.format(value);
	}

	public static double getDouble(String s) {
		if (s.isEmpty())
			return 0.;
		else return Double.parseDouble(s);
	}
}
