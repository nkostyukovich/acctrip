package com.dzebsu.acctrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class NewPrimaryCurrencyAppearedDialog extends BaseStableDialog {

	private Event event;

	private Currency currency;

	private long currencyIdBefore;

	private boolean actionPerformed = false;

	public long getCurrencyIdBefore() {
		return currencyIdBefore;
	}

	public void setCurrencyIdBefore(long currencyIdBefore) {
		this.currencyIdBefore = currencyIdBefore;
	}

	public static NewPrimaryCurrencyAppearedDialog newInstance(Event event, Currency currency, long currencyIdBefore) {
		NewPrimaryCurrencyAppearedDialog dialog = new NewPrimaryCurrencyAppearedDialog();
		dialog.setEvent(event);
		dialog.setCurrency(currency);
		dialog.setCurrencyIdBefore(currencyIdBefore);
		return dialog;
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
		String message = String.format(getString(R.string.warning_new_prim_curr), currency.getCode(), event.getName());
		alert.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.warning_word).setMessage(message)
				.setNegativeButton(R.string.defaults, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionPerformed = true;
						updateCurrencyPairsWithDefRates();
					}

				}).setPositiveButton(R.string.new_prim_curr_appeared_auto_rates_btn,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								actionPerformed = true;
								autoEvolveRatesAndUpdate();
							}
						}).setNeutralButton(R.string.new_prim_curr_appeared_edit_all_with_auto_btn,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								actionPerformed = true;
								autoEvolveRatesAndEditAll();

							}
						});
		return alert.create();
	}

	protected void autoEvolveRatesAndUpdate() {
		CurrencyPairDataSource cpData = new CurrencyPairDataSource(this.getActivity());
		cpData.updateByList(event.getId(), CurrencyUtils.autoEvolveRates(event.getPrimaryCurrency().getId(), cpData
				.getCurrencyPairMapByEventId(event.getId())));
		cpData.deleteCurrencyPairIfUnused(event.getId(), currencyIdBefore, currency.getId(), currency.getId());
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (!actionPerformed) updateCurrencyPairsWithDefRates();
	}

	private void autoEvolveRatesAndEditAll() {
		autoEvolveRatesAndUpdate();
		startEditCurrencyPairsActivity();
	}

	private static final String NEW_INTENT_EXTRA_EVENT_ID = "eventId";

	private void startEditCurrencyPairsActivity() {
		Intent intent = new Intent(this.getActivity(), EventCurrenciesListActivity.class);
		intent.putExtra(NEW_INTENT_EXTRA_EVENT_ID, event.getId());
		this.getActivity().startActivity(intent);
		// dismiss();
	}

	private void updateCurrencyPairsWithDefRates() {
		CurrencyPairDataSource currpairsData = new CurrencyPairDataSource(this.getActivity());
		currpairsData.deleteCurrencyPairIfUnused(event.getId(), currencyIdBefore, currency.getId(), currency.getId());
		if (currpairsData.getCurrencyPairByValues(event.getId(), currency.getId()) == null) {
			currpairsData.insert(event.getId(), currency.getId());
		}
		currpairsData.updateRatesBunchToDefaultValueByEventId(event.getId());
	}

}
