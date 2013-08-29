package com.dzebsu.acctrip.operations;

import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsDataSource;
import com.dzebsu.acctrip.db.datasources.StatisticsQueryParams;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

public class StatisticsFragment extends Fragment implements TabUpdateListener {

	private Map<String, List<StatListItem>> sortGroupsList;

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

	@Override
	public void onResume() {
		super.onResume();
		fillData();
	}

	private void fillData() {
		getData();
		fillHeaderInfo();
		fillList();
	}

	private void getData() {
		groups = StatisticsUtils.getSortCategories(this.getActivity());
		currs = StatisticsUtils.getCurrenciesRates(this.getActivity(), event.getId());
		sortGroupsList = StatisticsUtils.getFilteredStatValues(this.getActivity(), event, currs);

	}

	private void fillList() {
		if (listAdapter == null) {
			listAdapter = new OperationsStatisticsListViewAdapter(this.getActivity(), event.getId(), groups,
					sortGroupsList);

			ExpandableListAdapter adapter = listAdapter;
			ExpandableListView listView = (ExpandableListView) getView().findViewById(R.id.stat_expandableList);
			// trigger filter to it being applied on resume
			listView.setAdapter(adapter);
		} else {
			listAdapter.setSortedValues(sortGroupsList);
		}
	}

	private void fillHeaderInfo() {

		((TextView) getView().findViewById(R.id.st_head_total_exp_title)).setText(R.string.st_head_total_expenses_);
		List<Operation> operations = new OperationDataSource(getActivity()).getOperationListByEventId(event.getId());
		double total = CurrencyUtils.getTotalEventExpenses(operations, currs);
		((TextView) getView().findViewById(R.id.st_head_total_exp_value)).setText(CurrencyUtils
				.formatDecimalNotImportant(total)
				+ " " + event.getPrimaryCurrency().getCode());
		((TextView) getView().findViewById(R.id.st_head_total_today_title)).setText(R.string.st_head_today_spent_);
		double value = CurrencyUtils.getTotalEventExpenses(new StatisticsDataSource(getActivity())
				.getFilteredOperationList(StatisticsQueryParams.createParamsByDate(event.getId(), DateFormatter
						.getStartOfDay(), DateFormatter.getEndOfDay())), currs);
		((TextView) getView().findViewById(R.id.st_head_total_today_value)).setText(CurrencyUtils
				.formatDecimalNotImportant(value)
				+ " " + event.getPrimaryCurrency().getCode() + StatisticsUtils.getCent(value, total));
	}

	@Override
	public void update() {
		fillData();
	}

	private TabUpdater updater;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof TabUpdater) {
			(updater = (TabUpdater) activity).registerTab(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (updater != null) {
			updater.unregisterTab(this);
		}
	}

}