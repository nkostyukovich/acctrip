package com.dzebsu.acctrip.operations;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dzebsu.acctrip.EditOperationActivity;
import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.activity.EditEventActivity;
import com.dzebsu.acctrip.activity.OperationsActivity;
import com.dzebsu.acctrip.adapters.OperationsListViewAdapter;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyPairDataSource;
import com.dzebsu.acctrip.db.datasources.EventDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.eventcurrencies.management.BaseNewPrimaryCurrencyDialog;
import com.dzebsu.acctrip.eventcurrencies.management.EventCurrenciesSimpleDialog;
import com.dzebsu.acctrip.eventcurrencies.management.NewCurrencyAppearedDialog;
import com.dzebsu.acctrip.eventcurrencies.management.NewPrimaryCurrencyAppearedDialog;
import com.dzebsu.acctrip.eventcurrencies.management.TotalNewPrimaryCurrencyDialog;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.dictionaries.Currency;
import com.dzebsu.acctrip.settings.SettingsFragment;

public class OperationListFragment extends Fragment implements TabUpdateListener {

	public static final String INTENT_KEY_NEW_CURRENCY_APPEARED = "newCurrencyAppeared";

	private ActionMode mActionMode;

	private int selectedItem;

	private Object selectedViewTag;

	private List<Operation> operations;

	private Map<Long, CurrencyPair> currencyPairs;

	private final static int SELECTION_COLOR = android.R.color.holo_blue_dark;

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			final ListView list = (ListView) getView().findViewById(R.id.op_list);
			list.findViewWithTag(selectedViewTag).setBackgroundColor(
					getActivity().getApplication().getResources().getColor(android.R.color.transparent));

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_action_bar_dictionary, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
				case R.id.dic_edit:
					onOperationEdit(adapterZ.getItemId(selectedItem - 1));
					mode.finish(); // Action picked, so close the CAB
					return true;
				case R.id.dic_del:
					onDeleteOperation(adapterZ.getItemId(selectedItem - 1));
					mode.finish(); // Action picked, so close the CAB
					return true;
				default:
					return false;
			}
		}
	};

	private Event event;

	// Anonymous class wanted this adapter inside itself
	private OperationsListViewAdapter adapterZ;

	// for restoring list scroll position
	private static final String LIST_STATE = "listState";

	public static final String INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED = "newPrimaryCurrencyAppeared";

	private Parcelable mListState = null;

	private boolean dataChanged = false;

	private Bundle intent;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(LIST_STATE))
			mListState = savedInstanceState.getParcelable(LIST_STATE);
	}

	protected void onDeleteOperation(final long itemId) {
		new AlertDialog.Builder(this.getActivity()).setTitle(R.string.delete_dialog_title).setMessage(
				String.format(getString(R.string.confirm_del), getString(R.string.this_op))).setIcon(
				android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.dic_del,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						OperationDataSource opdata = new OperationDataSource(OperationListFragment.this.getActivity());
						Operation op = opdata.getOperationById(itemId);
						opdata.deleteById(itemId);
						new CurrencyPairDataSource(OperationListFragment.this.getActivity())
								.deleteCurrencyPairIfUnused(event.getId(), op.getCurrency().getId(), -1, event
										.getPrimaryCurrency().getId());
						Intent intent = new Intent(OperationListFragment.this.getActivity(), OperationsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("toast", R.string.op_deleted);
						intent.putExtra("eventId", event.getId());
						startActivity(intent);
					}
				}).setNegativeButton(android.R.string.no, null).show();

	}

	protected void onOperationEdit(long itemId) {
		Intent intent = new Intent(OperationListFragment.this.getActivity(), EditOperationActivity.class);
		intent.putExtra("eventId", event.getId());
		intent.putExtra("mode", "edit");
		intent.putExtra("opID", itemId);
		startActivity(intent);
	}

	public static final String INTENT_KEY_EVENT_ID = "eventId";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_operation_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initValues();
	}

	private void initValues() {
		intent = getArguments();

		long eventId = intent.getLong(INTENT_KEY_EVENT_ID, 0);
		event = new EventDataSource(this.getActivity()).getEventById(eventId);

		ListView listView = (ListView) getView().findViewById(R.id.op_list);
		listView.addHeaderView(createListHeader());
		fillOperationList();

		listView.setLongClickable(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
				if (mActionMode != null) {
					return false;
				}
				selectedViewTag = view.getTag();
				mActionMode = getActivity().startActionMode(mActionModeCallback);
				selectedItem = pos;
				view.setBackgroundColor(getActivity().getApplication().getResources().getColor(SELECTION_COLOR));
				return true;
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mActionMode != null) {
					mActionMode.finish();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				if (mActionMode != null) {
					mActionMode.finish();
				}
			}
		});

		((Button) getView().findViewById(R.id.op_new_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNewOperation();
			}
		});
		createFilter();
		if (intent.containsKey("toast"))
			Toast.makeText(getActivity().getApplicationContext(), intent.getInt("toast", R.string.not_message),
					Toast.LENGTH_SHORT).show();
		if (intent.containsKey(INTENT_KEY_NEW_CURRENCY_APPEARED)) {
			newCurrencyAppeared();
		}
		if (intent.containsKey(INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED)) {
			newPrimaryCurrencyAppeared();
		}

	}

	private void createFilter() {
		SearchView eventsFilter = (SearchView) getView().findViewById(R.id.uni_op_searchView);
		eventsFilter.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (mActionMode != null) mActionMode.finish();

			}
		});
		eventsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				OperationListFragment.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});
	}

	private void newPrimaryCurrencyAppeared() {
		Bundle args = intent.getBundle(INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED);

		invokeSuggestEditCurrenciesDialogPrimary(new CurrencyDataSource(this.getActivity()).getEntityById(args
				.getLong("currencyId")), args.getLong("currencyIdBefore"));
		intent.remove(INTENT_KEY_NEW_PRIMARY_CURRENCY_APPEARED);

	}

	private void invokeSuggestEditCurrenciesDialogPrimary(Currency currency, long currencyIdBefore) {
		BaseNewPrimaryCurrencyDialog dialog;
		if (new CurrencyPairDataSource(this.getActivity()).getCurrencyPairByValues(event.getId(), event
				.getPrimaryCurrency().getId()) != null) {
			dialog = NewPrimaryCurrencyAppearedDialog.newInstance(event, currency, currencyIdBefore);
		} else {
			dialog = TotalNewPrimaryCurrencyDialog.newInstance(event, currency, currencyIdBefore);
		}
		dialog.show(getFragmentManager(), "newPrimaryCurrencyAppeared");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.operation_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_edit_event_currs:
				showEventCurrenciesSimpleDialog();
				return true;
			case R.id.delete_event:
				onDeleteEvent(item.getActionView());
				return true;
			case R.id.event_edit:
				onEventEdit(item.getActionView());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void showEventCurrenciesSimpleDialog() {
		EventCurrenciesSimpleDialog newDialog = EventCurrenciesSimpleDialog.newInstance(event);
		newDialog.setListenerToUse(this.getActivity());
		newDialog.show(getFragmentManager(), "SimpleCurrencyPairs");
	}

	private void onEventEdit(View actionView) {
		Intent intent = new Intent(this.getActivity(), EditEventActivity.class);

		intent.putExtra("editId", event.getId());
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	public void onDeleteEvent(View view) {
		long ops = new OperationDataSource(this.getActivity()).getCountByEventId(event.getId());
		String message = ops > 0 ? String.format(getString(R.string.ev_used_by_ops), new EventDataSource(this
				.getActivity()).getEventById(event.getId()).getName(), ops) : String.format(
				getString(R.string.confirm_del), new EventDataSource(this.getActivity()).getEventById(event.getId())
						.getName());
		new AlertDialog.Builder(this.getActivity()).setTitle(R.string.delete_dialog_title).setMessage(message).setIcon(
				android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.dic_del,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						EventDataSource dataSource = new EventDataSource(OperationListFragment.this.getActivity());
						dataSource.delete(event.getId());
						if (PreferenceManager.getDefaultSharedPreferences(OperationListFragment.this.getActivity())
								.getLong(SettingsFragment.CURRENT_EVENT_MODE_EVENT_ID, -1) == event.getId())
							PreferenceManager.getDefaultSharedPreferences(OperationListFragment.this.getActivity())
									.edit().putLong(SettingsFragment.CURRENT_EVENT_MODE_EVENT_ID, -1).commit();
						new CurrencyPairDataSource(OperationListFragment.this.getActivity()).deleteByEventId(event
								.getId());

						Toast.makeText(OperationListFragment.this.getActivity(), R.string.event_deleted,
								Toast.LENGTH_SHORT).show();
						OperationListFragment.this.getActivity().onBackPressed();
					}
				}).setNegativeButton(android.R.string.no, null).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		fillOperationList();
		if (mListState != null) ((ListView) getView().findViewById(R.id.op_list)).onRestoreInstanceState(mListState);
		mListState = null;
	}

	// scroll position saving
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		mListState = ((ListView) getView().findViewById(R.id.op_list)).onSaveInstanceState();
		state.putParcelable(LIST_STATE, mListState);
	}

	public void onNewOperation() {
		Intent intent = new Intent(this.getActivity(), EditOperationActivity.class);
		intent.putExtra("eventId", event.getId());
		intent.putExtra("mode", "new");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	private void fillOperationList() {
		event = new EventDataSource(this.getActivity()).getEventById(event.getId());
		if (adapterZ == null || dataChanged) {
			dataChanged = false;
			currencyPairs = new CurrencyPairDataSource(this.getActivity()).getCurrencyPairMapByEventId(event.getId());
			operations = new OperationDataSource(this.getActivity()).getOperationListByEventId(event.getId());
			adapterZ = new OperationsListViewAdapter(this.getActivity(), operations);
			adapterZ.setCurrencyPairRates(currencyPairs);
			adapterZ.setPrimaryCurrency(event.getPrimaryCurrency());

		}
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) getView().findViewById(R.id.op_list);
		fillEventInfo();
		listView.setAdapter(adapter);
		OperationListFragment.this.adapterZ.getFilter().filter(
				((SearchView) getView().findViewById(R.id.uni_op_searchView)).getQuery());
	}

	public View createListHeader() {
		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(com.dzebsu.acctrip.R.layout.operation_list_header, null, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		dataChanged = true;
	}

	@Override
	public void onPause() {

		super.onPause();
		if (mActionMode != null) mActionMode.finish();
	}

	public void fillEventInfo() {
		((TextView) getView().findViewById(R.id.op_list_header_name)).setText(event.getName());
		((TextView) getView().findViewById(R.id.op_list_header_desc)).setText(event.getDesc());
		((TextView) getView().findViewById(R.id.op_list_header_sum)).setText(CurrencyUtils
				.formatDecimalNotImportant(CurrencyUtils.getTotalEventExpenses(operations, currencyPairs))
				+ " " + event.getPrimaryCurrency().getCode());
	}

	private void newCurrencyAppeared() {
		Bundle args = intent.getBundle(INTENT_KEY_NEW_CURRENCY_APPEARED);
		invokeSuggestEditCurrenciesDialog(new CurrencyDataSource(this.getActivity()).getEntityById(args
				.getLong("currencyId")));
		intent.remove(INTENT_KEY_NEW_CURRENCY_APPEARED);
	}

	private void invokeSuggestEditCurrenciesDialog(Currency currency) {
		NewCurrencyAppearedDialog dialog = NewCurrencyAppearedDialog.newInstance(event, currency);
		dialog.show(getFragmentManager(), "newCurrencyAppearedDialog");
	}

	@Override
	public void update() {
		currencyPairs = new CurrencyPairDataSource(this.getActivity()).getCurrencyPairMapByEventId(event.getId());
		if (adapterZ != null) {
			adapterZ.setCurrencyPairRates(currencyPairs);
			adapterZ.setPrimaryCurrency(event.getPrimaryCurrency());
			adapterZ.notifyDataSetChanged();
		}
		fillEventInfo();
	}

	private TabUpdater updater;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof TabUpdater) {
			(updater = (TabUpdater) activity).registerTab(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (updater != null) {
			updater.unregisterTab(this);
		}
	}
}
