package com.dzebsu.acctrip.dictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;
import com.dzebsu.acctrip.db.datasources.CurrencyDataSource;
import com.dzebsu.acctrip.db.datasources.PlaceDataSource;

public class DictionaryNewDialogFragment extends DialogFragment {

	
	
	static DictionaryNewDialogFragment newInstance(Bundle data) {
		DictionaryNewDialogFragment f = new DictionaryNewDialogFragment();
        f.setArguments(data);
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
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dictonary_new_dialog, null);
		Bundle args=getArguments();
		obj=args.getInt("objType");
		int nameTV=args.getInt("name_tv");
		int title=args.getInt("title");
		int positiveBtn=args.getInt("positiveBtn");
		int negativeBtn=args.getInt("negativeBtn");
		if(obj==3){
			((EditText) view.findViewById(R.id.dic_new_name_et2)).setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.dic_new_name_tv2)).setVisibility(View.VISIBLE);
		}
		
		((TextView)view.findViewById(R.id.dic_new_name_tv)).setText(nameTV);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		builder.setTitle(title).setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//We will override
			}
		}).setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				DictionaryNewDialogFragment.this.getDialog().dismiss();
			}
		});
		// Create the AlertDialog object and return it
		final AlertDialog al=builder.create();
		//to prevent from closing when name="" and show toast
		al.setOnShowListener(new DialogInterface.OnShowListener() {
		    @Override
		    public void onShow(DialogInterface dialog) {

		        Button b = al.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		                onSaveElement();
		                
		            }
		        });
		    }
		});
		return al;
	}

	public void onSaveElement() {
		//TODO all
		String name = ((EditText) view.findViewById(R.id.dic_new_name_et)).getText().toString();
		if (name.isEmpty()) {
			Toast.makeText(this.getActivity().getApplicationContext(), R.string.enter_text, Toast.LENGTH_SHORT).show();
			return;
		}
		Bundle args=new Bundle();
		args.putString("name", name);
		if(obj==3)
			args.putString("code", ((EditText) view.findViewById(R.id.dic_new_name_et2)).getText().toString());

		saveListener.onSaveBtnDialog(args);
		this.dismiss();
	}

}
