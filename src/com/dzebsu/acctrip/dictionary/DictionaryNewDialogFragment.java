package com.dzebsu.acctrip.dictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;

public class DictionaryNewDialogFragment extends DialogFragment {

	
	
	static DictionaryNewDialogFragment newInstance(int num) {
		DictionaryNewDialogFragment f = new DictionaryNewDialogFragment();

        // Supply num input as an argument.
		//1,2,3 place cat, cur
        Bundle args = new Bundle();
        args.putInt("objVer", num);
        f.setArguments(args);

        return f;
    }

	private onSaveElementListener saveListener;
	private View view; 
	private int obj;


	public void setOnSaveElementListener(onSaveElementListener listener){
		saveListener=listener;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		obj=getArguments().getInt("objVer");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dictonary_new_dialog, null);
		builder.setView(view);
		int title=1,name=1;
		switch(obj){
			case 1:
				title=R.string.dic_new_place_title;
				name=R.string.dic_new_place;
				break;
			case 2:
				title=R.string.dic_new_category_title;
				name=R.string.dic_new_category;
				break;
			case 3:
				title=R.string.dic_new_currency_title;
				name=R.string.dic_new_currency;
				((EditText) view.findViewById(R.id.dic_new_name_et2)).setVisibility(View.VISIBLE);
				((TextView) view.findViewById(R.id.dic_new_name_tv2)).setVisibility(View.VISIBLE);
				break;
		}
		((TextView)view.findViewById(R.id.dic_new_name_tv)).setText(name);
		
		builder.setTitle(title).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				onSaveElement();
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				DictionaryNewDialogFragment.this.getDialog().cancel();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void onSaveElement() {
		//TODO all
		String name = ((EditText) view.findViewById(R.id.dic_new_name_et)).getText().toString();
		if (name.isEmpty()) {
			Toast.makeText(this.getActivity().getApplicationContext(), R.string.enter_text, Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch(obj){
		case 1:
			new PlaceDataSource(getActivity()).insert(name);
			break;
		case 2:
			new CategoryDataSource(getActivity()).insert(name);
			break;
		case 3:
			String code=((EditText) view.findViewById(R.id.dic_new_name_et2)).getText().toString();
			new CurrencyDataSource(getActivity()).insert(name,code);
			break;
	}
		saveListener.onSaveBtnDialog();
		DictionaryNewDialogFragment.this.getDialog().hide();
	}

}
