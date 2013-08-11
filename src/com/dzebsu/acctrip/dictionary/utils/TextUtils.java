package com.dzebsu.acctrip.dictionary.utils;

public class TextUtils {

	/**
	 * returns the string, the first char lowercase
	 * 
	 * @param target
	 * @return
	 */
	public final static String asLowerCaseFirstChar(final String target) {

		if ((target == null) || (target.length() == 0)) {
			return target; // You could omit this check and simply live with an
							// exception if you like
		}
		return Character.toLowerCase(target.charAt(0)) + (target.length() > 1 ? target.substring(1) : "");
	}

	/**
	 * returns the string, the first char uppercase
	 * 
	 * @param target
	 * @return
	 */
	public final static String asUpperCaseFirstChar(final String target) {
		if ((target == null) || (target.length() == 0)) {
			return target; // You could omit this check and simply live with an
							// exception if you like
		}
		return Character.toUpperCase(target.charAt(0)) + (target.length() > 1 ? target.substring(1) : "");
	}

	public final static String addNewLine(String currentString, String newLine) {
		if (newLine == null) return currentString;
		if (currentString.length() > 0)
			return currentString + "\n" + newLine;
		else return newLine;
	}

}
