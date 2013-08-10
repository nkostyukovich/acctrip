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

import com.dzebsu.acctrip.R;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class DictionaryListViewAdapter<T extends BaseDictionary> extends ArrayAdapter<T> {

	private List<T> objects;

	private List<T> objectsInit;

	private LayoutInflater inflater;

	public DictionaryListViewAdapter(Context context, List<T> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_dictionary_list, objects);
		this.objectsInit = objects;
		this.objects = objectsInit;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class RowViewHolder {

		public TextView name = null;
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
	public int getCount() {
		return objects.size();
	};

	@Override
	public T getItem(int position) {
		return objects.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_dictionary_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.name = (TextView) rowView.findViewById(R.id.dictionary_name_list);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();

		if (objects.get(position) instanceof Currency) {
			holder.name.setText(objects.get(position).getName() + " " + ((Currency) objects.get(position)).getCode());
		} else holder.name.setText(objects.get(position).getName());

		return rowView;
	}

	@Override
	public Filter getFilter() {
		return new ObjectFilter();
	}

	// custom filter
	private class ObjectFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filter = new FilterResults();
			ArrayList<BaseDictionary> filtered = new ArrayList<BaseDictionary>();
			objects = objectsInit;
			if (constraint != null) {
				for (BaseDictionary g : objects) {
					if (g.getName().toLowerCase().contains(constraint.toString().toLowerCase())) filtered.add(g);
				}
			}
			filter.values = filtered;

			return filter;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			objects = (ArrayList<T>) results.values;
			notifyDataSetChanged();
		}

	}
}