package com.dzebsu.acctrip.dictionary;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class DictionaryNewDialogFragment<T extends BaseDictionary> extends DialogFragment {

	private T entity;

	private IDialogListener<T> listener;

	private View view;

	private DictionaryType type;

	public static <T extends BaseDictionary> DictionaryNewDialogFragment<T> newInstance(T entity, DictionaryType type) {
		DictionaryNewDialogFragment<T> fragment = new DictionaryNewDialogFragment<T>();
		fragment.setEntity(entity);
		fragment.setType(type);
		return fragment;
	}

	public DictionaryNewDialogFragment() {
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public void setType(DictionaryType type) {
		this.type = type;
	}

	public void setOnPositiveBtnListener(IDialogListener<T> listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_input_add);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dictonary_new_dialog, null);
		if (type == DictionaryType.CURRENCY) {
			((EditText) view.findViewById(R.id.dic_new_name_et2)).setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.dic_new_name_tv2)).setVisibility(View.VISIBLE);
		}
		((TextView) view.findViewById(R.id.dic_new_name_tv)).setText(type.getNameFldLabel());
		if (entity.getId() != null) {
			builder.setIcon(android.R.drawable.ic_menu_edit);
			((EditText) view.findViewById(R.id.dic_new_name_et)).setText(entity.getName());
			if (entity instanceof Currency) {
				((EditText) view.findViewById(R.id.dic_new_name_et2)).setText(((Currency) entity).getCode());
			}
		}
		// TODO implement other dialog for Delete

		builder.setView(view);
		builder.setTitle(entity.getId() == null ? type.getNewDialogTitle() : type.getEditDialogTitle())
				.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						DictionaryNewDialogFragment.this.getDialog().dismiss();
					}
				});
		// Create the AlertDialog object and return it
		final AlertDialog al = builder.create();
		// to prevent from closing when name="" and show toast
		al.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = al.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						// onPositiveBtn();

					}
				});
			}
		});
		return al;
	}

	public void onPositiveBtn() {
		String name = ((EditText) view.findViewById(R.id.dic_new_name_et)).getText().toString();
		if (name.isEmpty()) {
			Toast.makeText(this.getActivity().getApplicationContext(), R.string.enter_text, Toast.LENGTH_SHORT).show();
			return;
		}
		entity.setName(name);
		if (type == DictionaryType.CURRENCY) {
			String code = ((EditText) view.findViewById(R.id.dic_new_name_et2)).getText().toString();
			if (code.isEmpty()) {
				Toast.makeText(this.getActivity().getApplicationContext(), R.string.enter_code, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			((Currency) entity).setCode(code);
		}
		listener.onSuccess(entity);
		this.dismiss();
	}
}
