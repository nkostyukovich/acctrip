package com.dzebsu.acctrip.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dzebsu.acctrip.R;
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

	// TODO save all arrays XXX very IMPORTANT

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

	public EventCurrenciesListViewAdapter(Context context, List<CurrencyPair> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_currency_pair_list, objects);
		this.objects = objects;
		leftOri = new boolean[objects.size()];
		Arrays.fill(leftOri, true);
		Arrays.fill(firstValues, 1.00);
		for (int i = 0; i < objects.size(); i++) {
			secondValues[i] = objects.get(i).getRate();
		}
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
			firstCurrencyValue = String.valueOf(firstValues[position]);
			secondCurrencyValue = String.valueOf(secondValues[position]);
		} else {
			firstCurrencyCode = objects.get(position).getSecondCurrency().getCode();
			secondCurrencyCode = primaryCurrency.getCode();
			firstCurrencyValue = String.valueOf(secondValues[position]);
			secondCurrencyValue = String.valueOf(firstValues[position]);
		}

		holder.firstCurrencyCode.setText(firstCurrencyCode);
		holder.secondCurrencyCode.setText(secondCurrencyCode);
		holder.firstCurrencyValue.setText(firstCurrencyValue);
		holder.secondCurrencyValue.setText(secondCurrencyValue);

		final int pos = position;
		holder.firstCurrencyValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					final EditText cur = (EditText) v;
					firstValues[pos] = Double.parseDouble(cur.getText().toString());
				}
			}
		});
		holder.secondCurrencyValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					final EditText cur = (EditText) v;
					secondValues[pos] = Double.parseDouble(cur.getText().toString());
				}
			}
		});
		((Button) rowView.findViewById(R.id.cur_row_reverse_button)).setOnClickListener(new OnClickListener() {

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
