package com.dzebsu.acctrip.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dzebsu.acctrip.models.Category;

public class CategoryListViewAdapter extends ArrayAdapter<Category> {

	public CategoryListViewAdapter(Context context, int textViewResourceId, List<Category> objects) {
		super(context, textViewResourceId, objects);
	}

}
