package com.dzebsu.acctrip.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dzebsu.acctrip.models.Event;

public class EventListViewAdapter extends ArrayAdapter<Event> {
	private final List<Event> objects;
	private final Context context;
	public EventListViewAdapter(Context context, List<Event> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_event_list, objects);
		this.objects=objects;
		this.context=context;
	}
	static class RowViewHolder{
		public TextView name=null;
		public TextView desc=null;
		public TextView operationsCnt=null;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      rowView = inflater.inflate(com.dzebsu.acctrip.R.layout.row_event_list, parent,false);
		      RowViewHolder rowViewHolder = new RowViewHolder();
		      rowViewHolder.name = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.nameTextView);
		      rowViewHolder.desc = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.descTextView);
		      rowViewHolder.operationsCnt = (TextView) rowView.findViewById(com.dzebsu.acctrip.R.id.opsCountTextView);
		      rowView.setTag(rowViewHolder);
		    }
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		holder.name.setText(objects.get(position).getName());
		holder.desc.setText(objects.get(position).getDesc());
		holder.operationsCnt.setText("Nope haha");
/*		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View row = inflater.inflate(com.dzebsu.acctrip.R.layout.row_event_list, parent,false);
		 RowViewHolder holder=(RowViewHolder) convertView.getTag();
*/		 
		return rowView;
	}
}
