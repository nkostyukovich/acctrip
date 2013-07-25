package com.dzebsu.acctrip.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.dzebsu.acctrip.models.Event;
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
		public TextView opId = null;
		public TextView place = null;
		public TextView category = null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return objects.get(position).getId();
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
			rowViewHolder.opId = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_id_tv);
			rowViewHolder.place = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_place);
			rowViewHolder.category = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.op_list_category);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, EEE, MMM d", Locale.getDefault());
		holder.date.setText(sdf.format(objects.get(position).getDate()));
		String s = objects.get(position).getDesc();
		if (s.length() > 80)
			s = s.substring(0, 80) + "...";
		holder.desc.setText(s);
		holder.expenses.setText("-320 " + objects.get(position).getCurrency().getCode());
		holder.opId.setText(context.getString(com.dzebsu.acctrip.R.string.op_id_tv)
				+ ((Long) objects.get(position).getId()).toString());
		s = objects.get(position).getPlace().getName();
		if (s.length() > 12)
			s = s.substring(0, 12) + "...";
		holder.place.setText(s);
		s = objects.get(position).getCategory().getName();
		if (s.length() > 12)
			s = s.substring(0, 12) + "...";
		holder.category.setText(s);

		return rowView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
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
					SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
					String s = g.getDesc() + g.getId() + g.getValue() + g.getCategory().getName()
							+ sdf.format(g.getDate()) + g.getPlace().getName();
					// TODO sort by value if only numbers
					if (s.toLowerCase().contains(constraint.toString().toLowerCase()))
						filtered.add(g);
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