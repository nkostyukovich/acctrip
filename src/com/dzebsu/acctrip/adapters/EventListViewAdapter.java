package com.dzebsu.acctrip.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzebsu.acctrip.models.Event;
import com.dzebsu.acctrip.settings.SettingsFragment;

public class EventListViewAdapter extends ArrayAdapter<Event> {

	private List<Event> objects;

	private List<Event> objectsInit;

	private final Context context;

	private LayoutInflater inflater;

	private long starId;

	public EventListViewAdapter(Context context, List<Event> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_event_list, objects);
		starId = PreferenceManager.getDefaultSharedPreferences(context).getLong(
				SettingsFragment.CURRENT_EVENT_MODE_EVENT_ID, -1);
		this.objectsInit = objects;
		this.objects = objectsInit;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class RowViewHolder {

		public TextView name = null;

		public TextView desc = null;

		public ImageView star = null;
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
	public Event getItem(int position) {
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
			rowView = inflater.inflate(com.dzebsu.acctrip.R.layout.row_event_list, parent, false);
			RowViewHolder rowViewHolder = new RowViewHolder();
			rowViewHolder.name = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.nameTextView);
			rowViewHolder.desc = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.descTextView);
			rowViewHolder.star = (ImageView) rowView.findViewById(com.dzebsu.acctrip.R.id.row_event_list_star);
			rowView.setTag(rowViewHolder);
		}
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		holder.name.setText(objects.get(position).getName());
		String s = objects.get(position).getDesc();
		if (s.length() > 60) s = s.substring(0, 60) + "...";
		holder.desc.setText(s);
		holder.star.setVisibility(objects.get(position).getId() == starId ? View.VISIBLE : View.GONE);
		return rowView;
	}

	@Override
	public Filter getFilter() {
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
					if (g.getName().toLowerCase().contains(constraint.toString().toLowerCase())) filtered.add(g);
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
