package com.dzebsu.acctrip;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.adapters.DictionaryListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;
import com.dzebsu.acctrip.dictionary.DictionaryListFragment;
import com.dzebsu.acctrip.dictionary.WrappedObject;
import com.dzebsu.acctrip.dictionary.onPositiveBtnListener;

public class DictionaryElementPickerFragment extends DialogFragment{

	static public DictionaryElementPickerFragment prepareDialog(int obj) {
		Bundle args = new Bundle();
		args.putInt("objType", obj);
		return DictionaryElementPickerFragment.newInstance(args);
	}
	
	static DictionaryElementPickerFragment newInstance(Bundle data) {
		DictionaryElementPickerFragment f = new DictionaryElementPickerFragment();
        f.setArguments(data);
        return f;
    }

	private onPickFragmentListener pickListener;
	private View view; 
	private int obj;
	private DictionaryListViewAdapter adapterZ;

	public void setOnPickFragmentListener(onPickFragmentListener listener){
		pickListener=listener;
	}
	
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		obj=getArguments().getInt("objType");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_dictionary_list2, null,false);
		final ListView list = (ListView) view.findViewById(R.id.dictionarylist);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				Bundle args=new Bundle();
				args.putLong("pickedId",id);
				args.putString("picked",((WrappedObject)adapterZ.getItem(pos)).getName());
				pickListener.onActionInDialog(args);
				DictionaryElementPickerFragment.this.dismiss();

			}
		});
		
		((ImageButton) view.findViewById(R.id.dic_new)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle args=new Bundle();
				args.putBoolean("requestNew",true);
				pickListener.onActionInDialog(args);
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
		final AlertDialog al=builder.create();
		//to prevent from closing when name="" and show toast
		return al;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		fillList();
		
	}
	private void fillList() {
		List<? extends WrappedObject> objs = null;
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
		
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) view.findViewById(R.id.dictionarylist);
		listView.setAdapter(adapter);
		this.adapterZ.getFilter().filter(((SearchView) view.findViewById(R.id.dic_search)).getQuery());
	}
	



}
