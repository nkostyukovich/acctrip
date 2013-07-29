package com.dzebsu.acctrip.dictionary;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.adapters.DictionaryListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class DictionaryListFragment extends Fragment implements onPositiveBtnListener {

	private DictionaryListViewAdapter adapterZ;

	private final static int SELECTION_COLOR = android.R.color.holo_red_dark;

	private int selectedItem;

	// private Class<T> type;
	private int obj;

	ActionMode mActionMode;

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			final ListView list = (ListView) getView().findViewById(R.id.dictionarylist);
			list.getChildAt(selectedItem).setBackgroundColor(
					getActivity().getApplication().getResources().getColor(android.R.color.transparent));

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_action_bar_dictionary, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.dic_edit:
				onElementEdit(adapterZ.getItemId(selectedItem));
				mode.finish(); // Action picked, so close the CAB
				return true;
			case R.id.dic_del:
				onDeleteElement(adapterZ.getItemId(selectedItem));
				mode.finish(); // Action picked, so close the CAB
				return true;
			default:
				return false;
			}
		}
	};

	private boolean dataChanged = false;

	// 1place 2cat 3cur
	public DictionaryListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add filter_Event_Edittext for events names
		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		obj = data.getInt("current_page", 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View vi = inflater.inflate(R.layout.fragment_dictionary_list, container, false);
		final ListView list = (ListView) vi.findViewById(R.id.dictionarylist);
		list.setLongClickable(true);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
				if (mActionMode != null) {
					return false;
				}

				mActionMode = getActivity().startActionMode(mActionModeCallback);
				selectedItem = pos;
				view.setBackgroundColor(getActivity().getApplication().getResources().getColor(SELECTION_COLOR));
				return true;
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {

				if (mActionMode != null) mActionMode.finish();

			}
		});
		return vi;
	}

	@Override
	public void onResume() {
		super.onResume();
		fillList();

		((ImageButton) getView().findViewById(R.id.dic_new)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onNewElement();

			}
		});

		SearchView objectsFilter = (SearchView) getView().findViewById(R.id.dic_search);
		objectsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (mActionMode != null) mActionMode.finish();
				DictionaryListFragment.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});
		// end

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onPause() {

		super.onPause();
		if (mActionMode != null) mActionMode.finish();
	}

	public void onElementEdit(long id) {
		int title = 1, name = 1;
		BaseDictionary object = null;
		String name1 = null, code1 = null;
		switch (obj) {
		case 1:
			title = R.string.dic_edit_place_title;
			name = R.string.dic_new_place;
			object = new PlaceDataSource(getActivity()).getPlaceById(id);
			name1 = object.getName();
			break;
		case 2:
			title = R.string.dic_edit_category_title;
			name = R.string.dic_new_category;
			object = new CategoryDataSource(getActivity()).getCategoryById(id);
			name1 = object.getName();
			break;
		case 3:
			title = R.string.dic_edit_currency_title;
			name = R.string.dic_new_currency;
			object = new CurrencyDataSource(getActivity()).getCurrencyById(id);
			name1 = object.getName();
			code1 = ((Currency) object).getCode();
			break;
		}

		DictionaryNewDialogFragment newFragment = DictionaryNewDialogFragment.prepareDialog(obj, "edit", name,
				getString(title), R.string.save_edit_btn, name1, code1, id);
		newFragment.show(getFragmentManager(), "dialog");
		newFragment.setOnPositiveBtnListener(this);
	}

	public void onDeleteElement(long id) {
		String title = getString(R.string.confirm_del);
		int name = 1;
		BaseDictionary object = null;
		String name1 = null;
		OperationDataSource opDB = new OperationDataSource(getActivity());
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.warning).setIcon(
				android.R.drawable.stat_notify_error).setPositiveButton(R.string.okay, null);
		long ops = 0;
		switch (obj) {
		case 1:
			name = R.string.dic_new_place;
			PlaceDataSource db = new PlaceDataSource(getActivity());
			ops = opDB.getOperationListByPlaceId(id).size();
			if (ops > 0) {
				alert.setMessage(String.format(getString(R.string.pl_used_by_ops), ops)).create().show();
				return;
			}
			object = db.getPlaceById(id);
			name1 = object.getName();
			break;
		case 2:
			name = R.string.dic_new_category;
			CategoryDataSource db1 = new CategoryDataSource(getActivity());
			ops = opDB.getOperationListByCategoryId(id).size();
			if (ops > 0) {
				alert.setMessage(String.format(getString(R.string.cat_used_by_ops), ops)).create().show();
				;
				return;
			}
			object = db1.getCategoryById(id);
			name1 = object.getName();
			break;
		case 3:
			name = R.string.dic_new_currency;
			CurrencyDataSource db2 = new CurrencyDataSource(getActivity());
			ops = opDB.getOperationListByCurrencyId(id).size();
			if (ops > 0) {
				alert.setMessage(String.format(getString(R.string.cur_used_by_ops), ops)).create().show();
				return;
			}
			object = db2.getCurrencyById(id);
			name1 = object.getName();
			break;
		}

		DictionaryNewDialogFragment newFragment = DictionaryNewDialogFragment.prepareDialog(obj, "delete", name, String
				.format(title, name1), R.string.dic_del, "", "", id);
		newFragment.show(getFragmentManager(), "dialog");
		newFragment.setOnPositiveBtnListener(this);
	}

	public void onNewElement() {
		int title = 1, name = 1;
		switch (obj) {
		case 1:
			title = R.string.dic_new_place_title;
			name = R.string.dic_new_place;
			break;
		case 2:
			title = R.string.dic_new_category_title;
			name = R.string.dic_new_category;
			break;
		case 3:
			title = R.string.dic_new_currency_title;
			name = R.string.dic_new_currency;
			break;
		}
		DictionaryNewDialogFragment newFragment = DictionaryNewDialogFragment.prepareDialog(obj, "new", name,
				getString(title), R.string.save, null, null, 0);
		newFragment.show(getFragmentManager(), "dialog");
		newFragment.setOnPositiveBtnListener(this);

	}

	private void fillList() {
		if (adapterZ == null || dataChanged) {
			dataChanged = false;
			List<? extends BaseDictionary> objs = null;
			switch (obj) {
			case 1:
				objs = new PlaceDataSource(this.getActivity()).getPlaceList();
				break;
			case 2:
				objs = new CategoryDataSource(this.getActivity()).getCategoryList();
				break;
			case 3:
				objs = new CurrencyDataSource(this.getActivity()).getCurrencyList();
				break;
			}
			adapterZ = new DictionaryListViewAdapter(getActivity(), objs);
			// trigger filter to it being applied on resume

		}
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) getView().findViewById(R.id.dictionarylist);
		listView.setAdapter(adapter);
		this.adapterZ.getFilter().filter(((SearchView) getView().findViewById(R.id.dic_search)).getQuery());
	}

	@Override
	public void onPositiveBtnDialog(Bundle args) {
		String mode = args.getString("mode");
		if (mode.equals("new")) {
			String name = args.getString("name");
			switch (obj) {
			case 1:
				new PlaceDataSource(getActivity()).insert(name);
				break;
			case 2:
				new CategoryDataSource(getActivity()).insert(name);
				break;
			case 3:
				new CurrencyDataSource(getActivity()).insert(name, args.getString("code"));
				break;
			}
			Toast.makeText(getActivity(), "Created.", Toast.LENGTH_SHORT).show();
		} else if (mode.equals("edit")) {
			String name = args.getString("name");
			long id = args.getLong("id");
			switch (obj) {
			case 1:
				new PlaceDataSource(getActivity()).update(id, name);
				break;
			case 2:
				new CategoryDataSource(getActivity()).update(id, name);
				break;
			case 3:
				new CurrencyDataSource(getActivity()).update(id, name, args.getString("code"));
				break;
			}
			Toast.makeText(getActivity(), "Edited.", Toast.LENGTH_SHORT).show();
		} else if (mode.equals("delete")) {
			long id = args.getLong("id");
			switch (obj) {
			case 1:
				new PlaceDataSource(getActivity()).delete(id);
				break;
			case 2:
				new CategoryDataSource(getActivity()).delete(id);
				break;
			case 3:
				new CurrencyDataSource(getActivity()).delete(id);
				break;
			}
			Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
		}
		dataChanged = true;
		fillList();

	}

}
