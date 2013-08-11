package com.dzebsu.acctrip.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class EventCurrenciesListViewAdapter extends ArrayAdapter<CurrencyPair> {

	private List<CurrencyPair> objects;

	private boolean[] leftOri;

	private double[] firstValues;

	private double[] secondValues;

	private Currency primaryCurrency;

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	// TODO FOCUS on first EDITTEXT

	public void setFirstValue(int position, double value) {
		if (leftOri[position]) {
			firstValues[position] = value;
		} else {
			secondValues[position] = value;
		}
	}

	public List<CurrencyPair> getObjects() {
		return objects;
	}

	public void setObjects(List<CurrencyPair> objects) {
		this.objects = objects;
	}

	public void setSecondValue(int position, double value) {
		if (leftOri[position]) {
			secondValues[position] = value;
		} else {
			firstValues[position] = value;
		}
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}

	public boolean[] getLeftOri() {
		return leftOri;
	}

	public void setLeftOri(boolean[] rightOri) {
		this.leftOri = rightOri;
	}

	public double[] getFirstValues() {
		return firstValues;
	}

	public void setFirstValues(double[] firstValues) {
		this.firstValues = firstValues;
	}

	public double[] getSecondValues() {
		return secondValues;
	}

	public void setSecondValues(double[] secondValues) {
		this.secondValues = secondValues;
	}

	private LayoutInflater inflater;

	public EventCurrenciesListViewAdapter(Context context, List<CurrencyPair> objects, Currency primaryCurrency) {
		super(context, com.dzebsu.acctrip.R.layout.row_currency_pair_list, objects);
		this.objects = objects;
		leftOri = new boolean[objects.size()];
		Arrays.fill(leftOri, true);
		firstValues = new double[objects.size()];
		Arrays.fill(firstValues, 1.00);
		secondValues = new double[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			secondValues[i] = objects.get(i).getRate();
		}
		this.primaryCurrency = primaryCurrency;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class RowViewHolder {

		public TextView firstCurrencyCode = null;

		public TextView secondCurrencyCode = null;

		public EditText firstCurrencyValue = null;

		public EditText secondCurrencyValue = null;
	}

	@Override
	public int getCount() {
		return objects.size();
	};

	@Override
	public CurrencyPair getItem(int position) {
		return objects.get(position);
	}

	public boolean isItemOrientationLeft(int position) {
		return leftOri[position];
	}

	public void setItemOrientationLeft(int position, boolean ori) {
		leftOri[position] = ori;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_currency_pair_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.firstCurrencyCode = (TextView) rowView.findViewById(R.id.cur_row_cur1);
			rowViewHolder.secondCurrencyCode = (TextView) rowView.findViewById(R.id.cur_row_cur2);
			rowViewHolder.firstCurrencyValue = (EditText) rowView.findViewById(R.id.cur_row_et_cur1);
			rowViewHolder.secondCurrencyValue = (EditText) rowView.findViewById(R.id.cur_row_et_cur2);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();

		String firstCurrencyCode;
		String secondCurrencyCode;
		String firstCurrencyValue;
		String secondCurrencyValue;

		if (leftOri[position]) {
			firstCurrencyCode = primaryCurrency.getCode();
			secondCurrencyCode = objects.get(position).getSecondCurrency().getCode();
			firstCurrencyValue = CurrencyUtils.formatAfterPoint(firstValues[position]);
			secondCurrencyValue = CurrencyUtils.formatAfterPoint(secondValues[position]);
		} else {
			firstCurrencyCode = objects.get(position).getSecondCurrency().getCode();
			secondCurrencyCode = primaryCurrency.getCode();
			firstCurrencyValue = CurrencyUtils.formatAfterPoint(secondValues[position]);
			secondCurrencyValue = CurrencyUtils.formatAfterPoint(firstValues[position]);
		}

		holder.firstCurrencyCode.setText(firstCurrencyCode);
		holder.secondCurrencyCode.setText(secondCurrencyCode);
		holder.firstCurrencyValue.setText(firstCurrencyValue);
		holder.secondCurrencyValue.setText(secondCurrencyValue);

		final int pos = position;
		holder.firstCurrencyValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				// TODO replace parse double everywhere
				setFirstValue(pos, CurrencyUtils.getDouble(s.toString()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		holder.secondCurrencyValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setSecondValue(pos, CurrencyUtils.getDouble(s.toString()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		((ImageButton) rowView.findViewById(R.id.cur_row_reverse_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EventCurrenciesListViewAdapter.this.reverseItemOrientationLeft(pos);
				EventCurrenciesListViewAdapter.this.notifyDataSetChanged();

			}
		});

		return rowView;
	}

	protected void reverseItemOrientationLeft(int pos) {
		leftOri[pos] = !leftOri[pos];

	}

}
