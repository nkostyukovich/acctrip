package com.dzebsu.acctrip;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.dzebsu.acctrip.adapters.EventCurrenciesSimpleDialogListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;

public class EventCurrenciesSimpleDialog extends DialogFragment {

	private Event event;

	private static final String INTENT_EXTRA_EVENT_ID = "eventId";

	public static EventCurrenciesSimpleDialog newInstance(Event event) {
		EventCurrenciesSimpleDialog dialog = new EventCurrenciesSimpleDialog();
		dialog.setEvent(event);
		return dialog;
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
				showFastEditDialog(cps.get(which));
				// dismiss();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	protected void showFastEditDialog(CurrencyPair currencyPair) {
		EditCurrencyPairDialog.newInstance(event.getPrimaryCurrency(), currencyPair).show(getFragmentManager(),
				"EditDialog");

	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
