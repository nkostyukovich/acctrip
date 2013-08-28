package com.dzebsu.acctrip.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.adapters.OperationsStatisticsListViewAdapter;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.date.utils.DateFormatter;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsQueryParams;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

public class StatisticsFragment extends Fragment {

	public class SortItemPair {

		String title;

		String value;

		public SortItemPair(String title, String value) {
			this.title = title;
			this.value = value;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	private Map<String, List<SortItemPair>> sortGroupsList;

	private Event event;

	private String[] groups;

	private Map<Long, CurrencyPair> currs;

	private OperationsStatisticsListViewAdapter listAdapter;

	public static final String INTENT_KEY_EVENT_ID = "eventId";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		event = new EventDataSource(getActivity()).getEventById(getArguments().getLong(INTENT_KEY_EVENT_ID));
		View view = inflater.inflate(R.layout.statistics_fragment, container, false);
		((ExpandableListView) view.findViewById(R.id.stat_expandableList)).addHeaderView(createHeaderView());
		return view;
	}

	private View createHeaderView() {
		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(com.dzebsu.acctrip.R.layout.statistics_header, null, false);
	}

	private void fillSortCategories() {
		groups = getResources().getStringArray(R.array.statistics_groups);
		sortGroupsList = new HashMap<String, List<SortItemPair>>(groups.length);
		StatisticsDataSource data = new StatisticsDataSource(this.getActivity());
		List<Operation> ops;
		List<SortItemPair> items;
		StatisticsQueryParams params;

		// by day
		List<String> days = data.getEventDays(event.getId());
		items = new ArrayList<SortItemPair>();
		for (String day : days) {
			params = StatisticsQueryParams.createParamsByDay(event.getId(), day);
			ops = data.getFilteredOperationList(params);
			items.add(new SortItemPair(DateFormatter.formatDate(this.getActivity(), ops.get(0).getDate()),
					CurrencyUtils.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(ops, currs)) + " "
							+ event.getPrimaryCurrency().getCode()));
		}
		sortGroupsList.put(groups[0], items);

		// by categories
		List<Long> categories = data.getEventCategories(event.getId());
		items = new ArrayList<SortItemPair>();
		for (Long category : categories) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(category);
			params = StatisticsQueryParams.createParamsByCategory(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			items.add(new SortItemPair(ops.get(0).getCategory().getName() + ":", CurrencyUtils
					.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(ops, currs))
					+ " " + event.getPrimaryCurrency().getCode()));
		}
		sortGroupsList.put(groups[1], items);

		// by currencies
		List<Long> currencies = data.getEventCurrencies(event.getId());
		items = new ArrayList<SortItemPair>();
		for (Long currency : currencies) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(currency);
			params = StatisticsQueryParams.createParamsByCurrency(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			items.add(new SortItemPair(CurrencyUtils.TotalSum(ops) + " " + ops.get(0).getCurrency().getCode() + ":",
					CurrencyUtils.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(ops, currs)) + " "
							+ event.getPrimaryCurrency().getCode()));
		}
		sortGroupsList.put(groups[2], items);

		// by categories
		List<Long> places = data.getEventPlaces(event.getId());
		items = new ArrayList<SortItemPair>();
		for (Long place : places) {
			Set<Long> set = new HashSet<Long>(1);
			set.add(place);
			params = StatisticsQueryParams.createParamsByPlace(event.getId(), set);
			ops = data.getFilteredOperationList(params);
			items.add(new SortItemPair(ops.get(0).getPlace().getName() + ":", CurrencyUtils
					.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(ops, currs))
					+ " " + event.getPrimaryCurrency().getCode()));
		}
		sortGroupsList.put(groups[3], items);
	}

	@Override
	public void onStart() {
		super.onStart();
		fillData();
	}

	private void fillData() {
		fillHeaderInfo();
		fillSortCategories();
		fillList();

	}

	private void fillList() {
		if (listAdapter == null) {
			listAdapter = new OperationsStatisticsListViewAdapter(this.getActivity(), groups, sortGroupsList);
		}
		ExpandableListAdapter adapter = listAdapter;
		ExpandableListView listView = (ExpandableListView) getView().findViewById(R.id.stat_expandableList);
		// trigger filter to it being applied on resume
		listView.setAdapter(adapter);
	}

	private void fillHeaderInfo() {

		((TextView) getView().findViewById(R.id.st_head_total_exp_title)).setText(R.string.st_head_total_expenses_);
		List<Operation> operations = new OperationDataSource(getActivity()).getOperationListByEventId(event.getId());
		currs = new CurrencyPairDataSource(getActivity()).getCurrencyPairMapByEventId(event.getId());
		((TextView) getView().findViewById(R.id.st_head_total_exp_value)).setText(CurrencyUtils
				.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(operations, currs))
				+ " " + event.getPrimaryCurrency().getCode());
		((TextView) getView().findViewById(R.id.st_head_total_today_title)).setText(R.string.st_head_today_spent_);
		((TextView) getView().findViewById(R.id.st_head_total_today_value)).setText(CurrencyUtils
				.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(new StatisticsDataSource(getActivity())
						.getFilteredOperationList(StatisticsQueryParams.createParamsByDate(event.getId(), DateFormatter
								.getStartOfDay(), DateFormatter.getEndOfDay())), currs))
				+ " " + event.getPrimaryCurrency().getCode());
	}
}