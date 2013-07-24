package com.dzebsu.acctrip.dictionary;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dzebsu.acctrip.EditEventActivity;
import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.adapters.DictionaryListViewAdapter;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;
import com.dzebsu.acctrip.models.Category;
import com.dzebsu.acctrip.models.Currency;
import com.dzebsu.acctrip.models.Place;

public class DictionaryListFragment extends Fragment implements onSaveElementListener{

	private DictionaryListViewAdapter adapterZ;
	// private Class<T> type;
	private int obj;

	public DictionaryListFragment() {
		// TODO Auto-generated constructor stub
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

		return inflater.inflate(R.layout.fragment_dictionary_list, container, false);

	}

	@Override
	public void onResume() {
		super.onResume();
		ImageButton ned= new ImageButton(this.getActivity());
		
		
		((ImageButton)getView().findViewById(R.id.dic_new)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNewElement();
				
			}
		});
		
		
		
		SearchView objectsFilter = (SearchView) getView().findViewById(R.id.dic_search);
		objectsFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				DictionaryListFragment.this.adapterZ.getFilter().filter(newText);
				return true;
			}
		});
		// end
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fillList();
	}

	
	
	
	public void onNewElement(){
		DictionaryNewDialogFragment newFragment = DictionaryNewDialogFragment.newInstance(obj);
	    newFragment.show(getFragmentManager(), "dialog");
	    newFragment.setOnSaveElementListener(this);
	}
	
	
	private void fillList(){
		List<? extends WrappedObject> objs=null;
		switch (obj) {
		case 1:
			objs=fillPlaceList();
			break;
		case 2:
			objs=fillCategoryList();
			break;
		case 3:
			objs=fillCurrencyList();
			break;
		}
		adapterZ = new DictionaryListViewAdapter(getActivity(), objs);
		ListAdapter adapter = adapterZ;
		ListView listView = (ListView) getView().findViewById(R.id.dictionarylist);
		// trigger filter to it being applied on resume
		this.adapterZ.getFilter().filter(((SearchView) getView().findViewById(R.id.dic_search)).getQuery());
		listView.setAdapter(adapter);
		
	}
	
	
	private List<? extends WrappedObject> fillCategoryList() {
		CategoryDataSource dataSource = new CategoryDataSource(this.getActivity());
		return dataSource.getCategoryList();
	}

	private List<? extends WrappedObject> fillCurrencyList() {
		CurrencyDataSource dataSource = new CurrencyDataSource(this.getActivity());
		return dataSource.getCurrencyList();
	}

	private List<? extends WrappedObject> fillPlaceList() {
		PlaceDataSource dataSource = new PlaceDataSource(this.getActivity());
		return dataSource.getPlaceList();
	}


	@Override
	public void onSaveBtnDialog() {
		fillList();
		
	}





}
