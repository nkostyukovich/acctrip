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

import com.dzebsu.acctrip.models.Event;

public class EventListViewAdapter extends ArrayAdapter<Event> {
	private List<Event> objects;
	private List<Event> objectsInit;
	private final Context context;
	private LayoutInflater inflater;

	public EventListViewAdapter(Context context, List<Event> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_event_list, objects);
		this.objectsInit = objects;
		this.objects = objectsInit;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class RowViewHolder {
		public TextView name = null;
		public TextView desc = null;
		public TextView eventId = null;
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
			rowView = inflater.inflate(com.dzebsu.acctrip.R.layout.row_event_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.name = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.nameTextView);
			rowViewHolder.desc = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.descTextView);
			rowViewHolder.eventId = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.eventIdTextView);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		holder.name.setText(objects.get(position).getName());
		String s = objects.get(position).getDesc();
		if (s.length() > 60)
			s = s.substring(0, 60) + "...";
		holder.desc.setText(s);
		holder.eventId.setText(context.getString(com.dzebsu.acctrip.R.string.id_text_view)
				+ ((Long) objects.get(position).getId()).toString());
		return rowView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new EventFilter();
	}

	// custom filter
	private class EventFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filter = new FilterResults();
			ArrayList<Event> filtered = new ArrayList<Event>();
			objects = objectsInit;
			if (constraint != null) {
				for (Event g : objects) {
					if (g.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
						filtered.add(g);
				}
			}
			filter.values = filtered;

			return filter;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			objects = (ArrayList<Event>) results.values;
			notifyDataSetChanged();
		}

	}
}
