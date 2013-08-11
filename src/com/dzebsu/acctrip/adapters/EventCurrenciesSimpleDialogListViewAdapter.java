package com.dzebsu.acctrip.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.models.CurrencyPair;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class EventCurrenciesSimpleDialogListViewAdapter extends ArrayAdapter<CurrencyPair> {

	private List<CurrencyPair> objects;

	private Currency primaryCurrency;

	private LayoutInflater inflater;

	public EventCurrenciesSimpleDialogListViewAdapter(Context context, List<CurrencyPair> objects,
			Currency primaryCurrency) {
		super(context, R.layout.row_currency_pair_simple, objects);
		this.objects = objects;
		this.primaryCurrency = primaryCurrency;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	static class RowViewHolder {

		public TextView firstCurrencyCode = null;

		public TextView secondCurrencyCode = null;

		public TextView rate = null;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_currency_pair_simple, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.firstCurrencyCode = (TextView) rowView.findViewById(R.id.cur_row_cur1);
			rowViewHolder.secondCurrencyCode = (TextView) rowView.findViewById(R.id.cur_row_cur2);
			rowViewHolder.rate = (TextView) rowView.findViewById(R.id.cur_row_rate);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		holder.firstCurrencyCode.setText(primaryCurrency.getCode());
		holder.secondCurrencyCode.setText(objects.get(position).getSecondCurrency().getCode());
		holder.rate.setText(CurrencyUtils.formatAfterPoint(objects.get(position).getRate()));
		return rowView;
	}

}
