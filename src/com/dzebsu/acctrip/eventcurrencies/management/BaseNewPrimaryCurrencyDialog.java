package com.dzebsu.acctrip.eventcurrencies.management;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.dzebsu.acctrip.BaseStableDialog;
import com.dzebsu.acctrip.EventCurrenciesListActivity;
import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public abstract class BaseNewPrimaryCurrencyDialog extends BaseStableDialog {

	protected void setFields(Event event, Currency currency, long currencyIdBefore) {
		this.event = event;
		this.currency = currency;
		this.currencyIdBefore = currencyIdBefore;
	}

	protected Event event;

	protected Currency currency;

	protected long currencyIdBefore;

	protected boolean actionPerformed = false;

	protected static final String NEW_INTENT_EXTRA_EVENT_ID = "eventId";

	public long getCurrencyIdBefore() {
		return currencyIdBefore;
	}

	public void setCurrencyIdBefore(long currencyIdBefore) {
		this.currencyIdBefore = currencyIdBefore;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (!actionPerformed) {
			updateCurrencyPairsWithDefRates();
			Toast.makeText(getActivity(), R.string.currency_rates_edited, Toast.LENGTH_SHORT).show();
		}
	}

	protected void startEditCurrencyPairsActivity() {
		Intent intent = new Intent(this.getActivity(), EventCurrenciesListActivity.class);
		intent.putExtra(NEW_INTENT_EXTRA_EVENT_ID, event.getId());
		this.getActivity().startActivity(intent);
		// dismiss();
	}

	protected void updateCurrencyPairsWithDefRates() {
		CurrencyPairDataSource currpairsData = new CurrencyPairDataSource(this.getActivity());
		clearDBIfUnused(currpairsData);
		addIfNewCurrency(currpairsData);
		currpairsData.updateRatesBunchToDefaultValueByEventId(event.getId());
		Toast.makeText(getActivity(), R.string.currency_rates_edited, Toast.LENGTH_SHORT).show();
	}

	protected void clearDBIfUnused(CurrencyPairDataSource db) {
		db.deleteCurrencyPairIfUnused(event.getId(), currencyIdBefore, currency.getId(), currency.getId());
	}

	protected void addIfNewCurrency(CurrencyPairDataSource db) {
		if (db.getCurrencyPairByValues(event.getId(), currency.getId()) == null) {
			db.insert(event.getId(), currency.getId());
		}
	}

}
