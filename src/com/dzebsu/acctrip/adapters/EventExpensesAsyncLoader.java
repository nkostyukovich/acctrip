package com.dzebsu.acctrip.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;

public class EventExpensesAsyncLoader extends AsyncTask<Void, Void, Map<Long, Double>> {

	// TODO stop in activity
	private Context cxt;

	private EventExpensesLoadListener listener;

	public EventExpensesAsyncLoader(Context cxt, EventExpensesLoadListener listener) {
		this.cxt = cxt;
		this.listener = listener;
	}

	@Override
	protected Map<Long, Double> doInBackground(Void... params) {
		List<Event> events = new EventDataSource(cxt).getEventList();
		Map<Long, Double> expenses = new HashMap<Long, Double>(events.size());
		OperationDataSource opDataSource = new OperationDataSource(cxt);
		CurrencyPairDataSource cpDataSource = new CurrencyPairDataSource(cxt);
		for (Event e : events) {
			expenses.put(e.getId(), getTotalSum(opDataSource.getOperationListByEventId(e.getId()), cpDataSource
					.getCurrencyPairMapByEventId(e.getId())));
			// TODO update every time maybe?
		}
		return expenses;

	}

	private double getTotalSum(List<Operation> operations, Map<Long, CurrencyPair> currencyPairs) {
		double sum = 0.;
		for (Operation op : operations) {
			sum += op.getValue() / currencyPairs.get(op.getCurrency().getId()).getRate();
		}
		return sum;
	}

	@Override
	protected void onPostExecute(Map<Long, Double> result) {
		listener.allExpensesLoaded(result);
		listener = null;
	}
}
