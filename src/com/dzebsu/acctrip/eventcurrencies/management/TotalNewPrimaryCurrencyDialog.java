package com.dzebsu.acctrip.eventcurrencies.management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class TotalNewPrimaryCurrencyDialog extends BaseNewPrimaryCurrencyDialog {

	public static TotalNewPrimaryCurrencyDialog newInstance(Event event, Currency currency, long currencyIdBefore) {
		TotalNewPrimaryCurrencyDialog dialog = new TotalNewPrimaryCurrencyDialog();
		dialog.setFields(event, currency, currencyIdBefore);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
		String message = String.format(getString(R.string.warning_total_new_prim_curr), currency.getCode(), event
				.getName());
		alert.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.warning_word).setMessage(message)
				.setNegativeButton(R.string.defaults, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionPerformed = true;
						updateCurrencyPairsWithDefRates();
					}

				}).setPositiveButton(R.string.enter_one_ratio, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionPerformed = true;
						enterOneRatio();
					}
				}).setNeutralButton(R.string.edit_all, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionPerformed = true;
						setDefaultsAndEditAll();

					}
				});
		return alert.create();
	}

	protected void setDefaultsAndEditAll() {
		updateCurrencyPairsWithDefRates();
		startEditCurrencyPairsActivity();
	}

	protected void enterOneRatio() {
		BaseNewPrimaryCurrencyDialog dialog = EventCurrencyPairPickerDialog.newInstance(event, currency,
				currencyIdBefore);
		dialog.show(getFragmentManager(), "primary_edit_picker");

	}

	protected void autoEvolveRatesAndUpdate() {
		CurrencyPairDataSource cpData = new CurrencyPairDataSource(this.getActivity());
		cpData.updateByList(event.getId(), CurrencyUtils.autoEvolveRates(event.getPrimaryCurrency().getId(), cpData
				.getCurrencyPairMapByEventId(event.getId())));
		clearDBIfUnused(cpData);
	}

}
