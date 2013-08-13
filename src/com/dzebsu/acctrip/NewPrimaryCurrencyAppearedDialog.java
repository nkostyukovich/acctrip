package com.dzebsu.acctrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class NewPrimaryCurrencyAppearedDialog extends BaseStableDialog {

	private Event event;

	private Currency currency;

	public static NewPrimaryCurrencyAppearedDialog newInstance(Event event, Currency currency) {
		NewPrimaryCurrencyAppearedDialog dialog = new NewPrimaryCurrencyAppearedDialog();
		dialog.setEvent(event);
		dialog.setCurrency(currency);
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
				.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateCurrencyPairsWithDefRates();
					}

				}).setPositiveButton(R.string.provide, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						editRates();
					}
				});
		return alert.create();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		updateCurrencyPairsWithDefRates();
	}

	private void editRates() {
		updateCurrencyPairsWithDefRates();
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
		currpairsData.deleteCurrencyPairIfUnused(event.getId(), event.getPrimaryCurrency().getId(), currency.getId(),
				currency.getId());
		if (currpairsData.getCurrencyPairByValues(event.getId(), currency.getId()) == null) {
			currpairsData.insert(event.getId(), currency.getId());
		}
		currpairsData.updateRatesBunchToDefaultValueByEventId(event.getId());
	}

}
