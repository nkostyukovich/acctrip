package com.dzebsu.acctrip.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dzebsu.acctrip.models.Place;

public class PlaceListViewAdapter extends ArrayAdapter<Place> {

	public PlaceListViewAdapter(Context context, int textViewResourceId, List<Place> objects) {
		super(context, textViewResourceId, objects);
	}

}
