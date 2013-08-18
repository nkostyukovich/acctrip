package com.dzebsu.acctrip.adapters;

import android.content.Context;
import android.os.AsyncTask;

import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;

public class EventExpensesAsyncLoader extends AsyncTask<Long, Void, Double> {

	// TODO stop in activity
	private Context ctx;

	private Long eventId;

	private EventExpensesLoadListener listener;

	public EventExpensesAsyncLoader(Context ctx, EventExpensesLoadListener listener) {
		this.ctx = ctx;
		this.listener = listener;
	}

	@Override
	protected Double doInBackground(Long... params) {
		eventId = params[0];
		return CurrencyUtils.getTotalEventExpenses(new OperationDataSource(ctx).getOperationListByEventId(params[0]),
				new CurrencyPairDataSource(ctx).getCurrencyPairMapByEventId(params[0]));

	}

	@Override
	protected void onPostExecute(Double result) {
		listener.expensesLoaded(eventId, result);
	}
}
