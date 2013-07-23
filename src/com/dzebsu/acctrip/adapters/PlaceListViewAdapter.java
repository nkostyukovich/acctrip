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
import com.dzebsu.acctrip.db.datasources.OperationDataSource;
import com.dzebsu.acctrip.models.Place;

public class PlaceListViewAdapter extends ArrayAdapter<Place> {
	private List<Place> objects;
	private List<Place> objectsInit;
	private final Context context;
	public PlaceListViewAdapter(Context context, List<Place> objects) {
		super(context, com.dzebsu.acctrip.R.layout.row_place_list, objects);
		this.objectsInit=objects;
		this.objects=objectsInit;
		this.context=context;
	}
	static class RowViewHolder{
		public TextView name=null;
		public TextView expenses=null;
		public TextView ops=null;
		public TextView placeId=null;
	}
	public long getPlaceByIdInList(long id){
		return objects.get((int)id).getId();
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
		      rowView = inflater.inflate(R.layout.row_place_list, parent,false);
		      RowViewHolder rowViewHolder = new RowViewHolder();
		      rowViewHolder.name = (TextView) rowView.findViewById(R.id.place_name_list);
		      rowViewHolder.expenses = (TextView) rowView.findViewById(R.id.place_expenses_list);
		      rowViewHolder.ops = (TextView) rowView.findViewById(R.id.place_ops_list);
		      rowViewHolder.placeId = (TextView) rowView.findViewById(R.id.place_id_list);
		      rowView.setTag(rowViewHolder);
		    }
		RowViewHolder holder = (RowViewHolder) rowView.getTag();
		holder.name.setText(objects.get(position).getName());
//		String s=objects.get(position).getDesc();
//		if(s.length()>60)
//			s=s.substring(0, 60)+"...";
		holder.expenses.setText(context.getString(R.string.place_expenses_list)+" -352 USD");
		holder.ops.setText(context.getString(R.string.place_ops_list)+" 25");
		holder.placeId.setText(context.getString(R.string.place_id_list)+((Long)objects.get(position).getId()).toString());
/*		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View row = inflater.inflate(com.dzebsu.acctrip.R.layout.row_event_list, parent,false);
		 RowViewHolder holder=(RowViewHolder) convertView.getTag();
*/		 
		return rowView;
	}
	 @Override
	    public Filter getFilter() {
	        // TODO Auto-generated method stub
	        return new PlaceFilter();
	    }
	 //custom filter
	 private class PlaceFilter extends Filter {

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint){
	            FilterResults filter = new FilterResults();
	            ArrayList<Place> filtered = new ArrayList<Place>();
	            objects=objectsInit;
	            if (constraint != null) {
	                    for (Place g : objects) {
	                        if (g.getName()
	                                .toLowerCase()
	                                .contains(constraint.toString().toLowerCase()))
	                            filtered.add(g);
	                    }
	                }
	                filter.values = filtered;
	            
	            return filter;
	        }

	        @Override
	        protected void publishResults(CharSequence constraint,
	        		FilterResults results) {
	            objects = (ArrayList<Place>) results.values;
	            notifyDataSetChanged();
	        }

	    }
}
