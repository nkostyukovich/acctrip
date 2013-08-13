package com.dzebsu.acctrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class NewCurrencyAppearedDialog extends BaseStableDialog {

	private Event event;

	private Currency currency;

	public static NewCurrencyAppearedDialog newInstance(Event event, Currency currency) {
		NewCurrencyAppearedDialog dialog = new NewCurrencyAppearedDialog();
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

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
		String message = String.format(getString(R.string.warning_first_time_curr), currency.getCode(),
				event.getName(), event.getPrimaryCurrency().getCode());
		alert.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.warning_word).setMessage(message)
				.setNegativeButton(R.string.later, null).setPositiveButton(R.string.provide,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								invokeCurrencyRateEdit(currency.getId());
							}
						});
		return alert.create();
	}

	private void invokeCurrencyRateEdit(long currencyId) {
		EditCurrencyPairDialog newDialog = EditCurrencyPairDialog.newInstance(event.getPrimaryCurrency(),
				new CurrencyPairDataSource(this.getActivity()).getCurrencyPairByValues(event.getId(), currencyId));
		newDialog.setListener((SimpleDialogListener) this.getActivity());
		newDialog.show(getFragmentManager(), "EditDialog");
	}

}
