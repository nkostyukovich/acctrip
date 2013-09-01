package com.dzebsu.acctrip.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.date.utils.DateFormatter;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsQueryParams;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

public class StatisticsUtils {

	public static String[] getSortCategories(Context context) {
		return context.getResources().getStringArray(R.array.statistics_groups);
	}

	public static String getCent(double value, double total) {
		return " (" + CurrencyUtils.formatDecimalNotImportant(value / total * 100.) + "%)";
	}

	public static Map<String, List<StatListItem>> getFilteredStatValues(Context context, Event event,
			Map<Long, CurrencyPair> currs) {
		String[] groups = getSortCategories(context);
		Map<String, List<StatListItem>> sortGroupsList = new HashMap<String, List<StatListItem>>(groups.length);
		StatisticsDataSource data = new StatisticsDataSource(context);
		List<Operation> ops;
		List<StatListItem> items;
		StatisticsQueryParams params;
		double value;
		double total = CurrencyUtils.getTotalEventExpenses(new OperationDataSource(context)
				.getOperationListByEventId(event.getId()), currs);

		// by day
		List<String> days = data.getEventDays(event.getId());
		items = new ArrayList<StatListItem>();
		for (String day : days) {
			params = StatisticsQueryParams.createParamsByDay(event.getId(), day);
			ops = data.getFilteredOperationList(params);
			value = CurrencyUtils.getTotalEventExpenses(ops, currs);
			items.add(new StatListItem(DateFormatter.formatDate(context, ops.get(0).getDate()), CurrencyUtils
					.formatDecimalNotImportant(value)
					+ " " + event.getPrimaryCurrency().getCode() + getCent(value, total)));
		}
		sortGroupsList.put(groups[0], items);

		// by categories
		List<Long> categories = data.getEventCategories(event.getId());
		items = new ArrayList<StatListItem>();
		for (Long category : categories) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(category);
			params = StatisticsQueryParams.createParamsByCategory(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			value = CurrencyUtils.getTotalEventExpenses(ops, currs);
			items.add(new StatListItem(ops.get(0).getCategory().getName(), CurrencyUtils
					.formatDecimalNotImportant(value)
					+ " " + event.getPrimaryCurrency().getCode() + getCent(value, total)));
		}
		sortGroupsList.put(groups[1], items);

		// by currencies
		List<Long> currencies = data.getEventCurrencies(event.getId());
		items = new ArrayList<StatListItem>();
		for (Long currency : currencies) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(currency);
			params = StatisticsQueryParams.createParamsByCurrency(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			value = CurrencyUtils.getTotalEventExpenses(ops, currs);
			items.add(new StatListItem(CurrencyUtils.formatDecimalNotImportant(CurrencyUtils.TotalSum(ops)) + " "
					+ ops.get(0).getCurrency().getCode(), CurrencyUtils.formatDecimalNotImportant(value) + " "
					+ event.getPrimaryCurrency().getCode() + getCent(value, total)));
		}
		sortGroupsList.put(groups[2], items);

		// by categories
		List<Long> places = data.getEventPlaces(event.getId());
		items = new ArrayList<StatListItem>();
		for (Long place : places) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(place);
			params = StatisticsQueryParams.createParamsByPlace(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			value = CurrencyUtils.getTotalEventExpenses(ops, currs);
			items.add(new StatListItem(ops.get(0).getPlace().getName(), CurrencyUtils.formatDecimalNotImportant(value)
					+ " " + event.getPrimaryCurrency().getCode() + getCent(value, total)));
		}
		sortGroupsList.put(groups[3], items);
		return sortGroupsList;
	}

	public static Map<Long, CurrencyPair> getCurrenciesRates(Context context, Long eventId) {
		return new CurrencyPairDataSource(context).getCurrencyPairMapByEventId(eventId);
	}

}
