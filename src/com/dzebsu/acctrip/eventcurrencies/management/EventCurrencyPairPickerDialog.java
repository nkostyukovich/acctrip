package com.dzebsu.acctrip.eventcurrencies.management;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.SimpleDialogListener;
import com.dzebsu.acctrip.adapters.EventCurrenciesPickerDialogListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class EventCurrencyPairPickerDialog extends BaseNewPrimaryCurrencyDialog {

	public static EventCurrencyPairPickerDialog newInstance(Event event, Currency currency, long currencyIdBefore) {
		EventCurrencyPairPickerDialog dialog = new EventCurrencyPairPickerDialog();
		dialog.setFields(event, currency, currencyIdBefore);
		return dialog;
	}

	private SimpleDialogListener listener = null;

	protected void setDefaultsAndEditAll() {
		updateCurrencyPairsWithDefRates();
		startEditCurrencyPairsActivity();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.new_prim_curr_edit_chooser).setPositiveButton(R.string.edit_all,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						actionPerformed = true;
						setDefaultsAndEditAll();
					}
				}).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dismiss();
				if (listener != null) listener.positiveButtonDialog(null);
			}
		});
		final List<CurrencyPair> cps = new CurrencyPairDataSource(this.getActivity())
				.getCurrencyPairListByEventId(event.getId());
		ListAdapter adapter = new EventCurrenciesPickerDialogListViewAdapter(this.getActivity(), cps, event
				.getPrimaryCurrency());
		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				showFastEditDialog(cps.get(which));
				// dismiss();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	protected void showFastEditDialog(CurrencyPair currencyPair) {
		actionPerformed = true;
		EditCurrencyPairAutoEvolveDialog dialog = EditCurrencyPairAutoEvolveDialog.newInstance(event, event
				.getPrimaryCurrency(), currencyIdBefore, currencyPair);
		dialog.setListener(listener);
		// TODO design activity notifier
		dialog.show(getFragmentManager(), "EditDialog");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof SimpleDialogListener) {
			listener = (SimpleDialogListener) activity;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

}
