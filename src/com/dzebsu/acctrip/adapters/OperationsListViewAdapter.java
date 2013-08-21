package com.dzebsu.acctrip.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.dzebsu.acctrip.currency.utils.CurrencyUtils;
import com.dzebsu.acctrip.date.utils.DateFormatter;
import com.dzebsu.acctrip.models.Operation;

public class OperationsListViewAdapter extends ArrayAdapter<Operation> {

	private List<Operation> objects;

	private List<Operation> objectsInit;

	private final Context context;

	private LayoutInflater inflater;

	public OperationsListViewAdapter(Context context, List<Operation> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_operation_list, objects);
		this.objectsInit = objects;
		this.objects = objectsInit;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class RowViewHolder {

		public TextView date = null;

		public TextView desc = null;

		public TextView expenses = null;

		public TextView expensesPrimary = null;

		public TextView category = null;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public long getItemId(int position) {
		return objects.get(position).getId();
	}

	@Override
	public Operation getItem(int position) {
		return objects.get(position);
	}

	@Override
	public int getCount() {
		return objects.size();
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(com.dzebsu.acctrip.R.layout.row_operation_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.date = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_date_tv);
			rowViewHolder.desc = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_desc_tv);
			rowViewHolder.expenses = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_expenses);
			rowViewHolder.expensesPrimary = (TextView) rowView
					.findViewById(com.dzebsu.acctrip.R.id.op_list_expenses_primary);
			rowViewHolder.category = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_category);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();

		holder.desc.setText(objects.get(position).getDesc());

		String date = DateFormatter.formatDateAndTime(context, objects.get(position).getDate());
		String place = objects.get(position).getPlace().getName();

		String cur = objects.get(position).getCurrency().getCode();
		holder.expenses.setText(CurrencyUtils.formatDecimalNotImportant(objects.get(position).getValue()) + " " + cur);
		// TODO add value in primary currency
		holder.expensesPrimary.setText(CurrencyUtils.formatDecimalNotImportant(objects.get(position).getValue()) + " "
				+ cur);
		holder.category.setText(objects.get(position).getCategory().getName());
		holder.date.setText(place + ", " + date);

		return rowView;
	}

	@Override
	public Filter getFilter() {
		return new OperationFilter();
	}

	// custom filter
	private class OperationFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filter = new FilterResults();
			ArrayList<Operation> filtered = new ArrayList<Operation>();
			objects = objectsInit;
			if (constraint != null) {
				for (Operation g : objects) {

					String s = g.getDesc() + g.getId() + g.getValue() + (g.getCategory().getName())
							+ DateFormatter.formatDateAndTime(context, g.getDate()) + (g.getPlace().getName());
					if (s.toLowerCase().contains(constraint.toString().toLowerCase())) filtered.add(g);
				}
			}
			filter.values = filtered;

			return filter;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			objects = (ArrayList<Operation>) results.values;
			notifyDataSetChanged();
		}

	}
}
