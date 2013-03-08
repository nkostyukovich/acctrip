package com.dzebsu.acctrip.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dzebsu.acctrip.models.Event;

public class EventListViewAdapter extends ArrayAdapter<Event> {

	public EventListViewAdapter(Context context, int textViewResourceId, List<Event> objects) {
		super(context, textViewResourceId, objects);
	}

}
