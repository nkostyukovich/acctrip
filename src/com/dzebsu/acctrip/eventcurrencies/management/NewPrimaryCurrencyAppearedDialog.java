package com.dzebsu.acctrip.eventcurrencies.management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class NewPrimaryCurrencyAppearedDialog extends BaseNewPrimaryCurrencyDialog {

	public static NewPrimaryCurrencyAppearedDialog newInstance(Event event, Currency currency, long currencyIdBefore) {
		NewPrimaryCurrencyAppearedDialog dialog = new NewPrimaryCurrencyAppearedDialog();
		dialog.setFields(event, currency, currencyIdBefore);
		return dialog;
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
		clearDBIfUnused(cpData);
		Toast.makeText(getActivity(), R.string.currency_rates_edited, Toast.LENGTH_SHORT).show();
	}

	private void autoEvolveRatesAndEditAll() {
		autoEvolveRatesAndUpdate();
		startEditCurrencyPairsActivity();
	}

}
