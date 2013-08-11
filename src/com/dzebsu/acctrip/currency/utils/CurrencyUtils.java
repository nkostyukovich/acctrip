package com.dzebsu.acctrip.currency.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class CurrencyUtils {

	public static final int ACCURACY_IS_IMPORTANT = 4;

	public static final int ACCURACY_IS_NOT_IMPORTANT = 2;

	// TODO remove all E!! and provide different length after
	// point

	public static String formatDecimalImportant(double value) {
		return formatAfterPoint(value, ACCURACY_IS_IMPORTANT);
	}

	public static String formatDecimalNotImportant(double value) {
		return formatAfterPoint(value, ACCURACY_IS_NOT_IMPORTANT);
	}

	public static String formatAfterPoint(double value, int decimalDigits) {
		char[] digitsAfterPoint = new char[decimalDigits];
		Arrays.fill(digitsAfterPoint, '#');
		DecimalFormat money = new DecimalFormat("##." + String.valueOf(digitsAfterPoint));
		money.setRoundingMode(RoundingMode.UP);
		return money.format(value);
	}

	public static double getDouble(String s) {
		if (s.isEmpty())
			return 0.;
		else return Double.parseDouble(s);
	}
}
