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
import com.dzebsu.acctrip.dictionary.WrappedObject;
import com.dzebsu.acctrip.models.Currency;

public class DictionaryListViewAdapter<T extends WrappedObject> extends ArrayAdapter<WrappedObject> {
	private List<WrappedObject> objects;
	private List<WrappedObject> objectsInit;
	private final Context context;

	public DictionaryListViewAdapter(Context context, List<WrappedObject> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_dictionary_list, objects);
		this.objectsInit = objects;
		this.objects = objectsInit;
		this.context = context;
	}

	static class RowViewHolder {
		public TextView name = null;
	}

	public long getObjectByIdInList(long id) {
		return objects.get((int) id).getId();
	}

	@Override
	public int getCount() {
		return objects.size();
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.row_dictionary_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.name = (TextView) rowView.findViewById(R.id.dictionary_name_list);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		
		if(objects.get(position) instanceof Currency){
			holder.name.setText(objects.get(position).getName()+" "+((Currency)objects.get(position)).getCode());
		}
		else holder.name.setText(objects.get(position).getName());

		return rowView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new ObjectFilter();
	}

	// custom filter
	private class ObjectFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filter = new FilterResults();
			ArrayList<WrappedObject> filtered = new ArrayList<WrappedObject>();
			objects = objectsInit;
			if (constraint != null) {
				for (WrappedObject g : objects) {
					if (g.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
						filtered.add(g);
				}
			}
			filter.values = filtered;

			return filter;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			objects = (ArrayList<WrappedObject>) results.values;
			notifyDataSetChanged();
		}

	}
}
