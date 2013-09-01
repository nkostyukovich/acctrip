package com.dzebsu.acctrip.eventcurrencies.management;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.dzebsu.acctrip.BaseSupportStableDialog;
import com.dzebsu.acctrip.EventCurrenciesListActivity;
import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.SimpleDialogListener;
import com.dzebsu.acctrip.adapters.EventCurrenciesSimpleDialogListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;

public class EventCurrenciesSimpleDialog extends BaseSupportStableDialog {

	private Event event;

	private SimpleDialogListener listener;

	private static final String INTENT_EXTRA_EVENT_ID = "eventId";

	public static EventCurrenciesSimpleDialog newInstance(Event event) {
		EventCurrenciesSimpleDialog dialog = new EventCurrenciesSimpleDialog();
		dialog.setEvent(event);
		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof SimpleDialogListener) {
			this.listener = (SimpleDialogListener) activity;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.event_currencies).setPositiveButton(R.string.edit_all,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(EventCurrenciesSimpleDialog.this.getActivity(),
								EventCurrenciesListActivity.class);
						intent.putExtra(INTENT_EXTRA_EVENT_ID, event.getId());
						EventCurrenciesSimpleDialog.this.getActivity().startActivity(intent);
					}
				}).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dismiss();
			}
		});
		final List<CurrencyPair> cps = new CurrencyPairDataSource(this.getActivity())
				.getCurrencyPairListByEventId(event.getId());
		ListAdapter adapter = new EventCurrenciesSimpleDialogListViewAdapter(this.getActivity(), cps, event
				.getPrimaryCurrency());
		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cps.get(which).getSecondCurrency().getId() == event.getPrimaryCurrency().getId()) {
					if (getDialog() != null && getRetainInstance()) getDialog().setDismissMessage(null);
					return;
				}
				showFastEditDialog(cps.get(which));
				// dismiss();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	protected void showFastEditDialog(CurrencyPair currencyPair) {
		EditCurrencyPairDialog dialog = EditCurrencyPairDialog.newInstance(event.getPrimaryCurrency(), currencyPair);
		dialog.setListener(listener);
		dialog.show(getFragmentManager(), "EditDialog");

	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setListenerToUse(Activity listener) {
		if (listener instanceof SimpleDialogListener) this.listener = (SimpleDialogListener) listener;
	}

}
