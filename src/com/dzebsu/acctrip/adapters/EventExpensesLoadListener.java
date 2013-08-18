package com.dzebsu.acctrip.adapters;

import java.util.Map;

public interface EventExpensesLoadListener {

	public void allExpensesLoaded(Map<Long, Double> expenses);

	public void oneExpensesLoaded(long eventId, double value);
}
