package com.dzebsu.acctrip.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.db.datasources.CategoryDataSource;

public class CategoryDialogFragment extends DialogFragment {

	public interface CategoryDialogListener {

		public void onDialogPositiveClick();

	}

	private CategoryDialogListener listener;
	private View view; 

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (CategoryDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement CategoryDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_category, null);
		builder.setView(view);

		builder.setMessage(R.string.add_category).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				onSaveCategory();
				listener.onDialogPositiveClick();
				CategoryDialogFragment.this.getDialog().hide();
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				CategoryDialogFragment.this.getDialog().cancel();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void onSaveCategory() {
		String name = ((EditText) view.findViewById(R.id.categoryNameEdit)).getText().toString();
		CategoryDataSource dataSource = new CategoryDataSource(getActivity());
		dataSource.insert(name);
	}

}
