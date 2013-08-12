package com.dzebsu.acctrip;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.dzebsu.acctrip.adapters.DictionaryListViewAdapter;
import com.dzebsu.acctrip.db.datasources.IDictionaryDataSource;
import com.dzebsu.acctrip.dictionary.DictionaryType;
import com.dzebsu.acctrip.dictionary.utils.DictUtils;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class DictionaryElementPickerFragment<T extends BaseDictionary> extends DialogFragment {

	private IDictionaryDataSource<T> dataSource;

	private DictionaryType type;

	public void setDataSource(IDictionaryDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}

	public static <T extends BaseDictionary> DictionaryElementPickerFragment<T> newInstance(Class<T> clazz, Context cxt) {
		DictionaryElementPickerFragment<T> fragment = new DictionaryElementPickerFragment<T>();
		fragment.setClass(clazz);
		fragment.setDataSource(DictUtils.getEntityDataSourceInstance(clazz, cxt));
		fragment.setRetainInstance(true);
		return fragment;
	}

	public DictionaryElementPickerFragment() {
		super();
	}

	public void setClass(Class<T> clazz) {
		this.clazz = clazz;
	}

	private Class<T> clazz;

	private IDictionaryFragmentListener pickListener;

	private View view;

	private int obj;

	private DictionaryListViewAdapter<T> adapterZ;

	public void setOnPickFragmentListener(IDictionaryFragmentListener listener) {
		pickListener = listener;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance()) getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IDictionaryFragmentListener) {
			this.pickListener = (IDictionaryFragmentListener) activity;
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// setRetainInstance(true) save us from NPE here
		dataSource.setContext(this.getActivity());

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_dictionary_list, null, false);
		final ListView list = (ListView) view.findViewById(R.id.dictionarylist);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				Bundle args = new Bundle();
				args.putLong("pickedId", id);
				BaseDictionary entry = adapterZ.getItem(pos);
				String title = entry.getName();
				if (entry instanceof Currency) {
					title = ((Currency) entry).getCode();
				}
				args.putString("picked", title);
				Object o = pickListener;
				try {
					pickListener.onActionPerformed(args);
				} catch (java.lang.InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				DictionaryElementPickerFragment.this.dismiss();

			}
		});

		((ImageButton) view.findViewById(R.id.dic_new)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putBoolean("requestNew", true);
				args.putSerializable("clazz", clazz);
				try {
					pickListener.onActionPerformed(args);
				} catch (java.lang.InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				DictionaryElementPickerFragment.this.dismiss();
			}
		});
		SearchView objectsFilter = (SearchView) view.findViewById(R.id.dic_search);
		objectsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				DictionaryElementPickerFragment.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});

		builder.setView(view);

		// Create the AlertDialog object and return it
		final AlertDialog al = builder.create();
		// to prevent from closing when name="" and show toast
		return al;
	}

	@Override
	public void onResume() {
		super.onResume();
		fillList();
		Window window = getDialog().getWindow();
		// XXX attention absolute value, problem when list smaller than this
		// area, it's not touchable
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 400);

	}

	private void fillList() {
		List<T> objs = dataSource.getEntityList();
		adapterZ = new DictionaryListViewAdapter<T>(getActivity(), objs);
		// trigger filter to it being applied on resume

		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) view.findViewById(R.id.dictionarylist);
		listView.setAdapter(adapter);
		this.adapterZ.getFilter().filter(((SearchView) view.findViewById(R.id.dic_search)).getQuery());
	}

}
