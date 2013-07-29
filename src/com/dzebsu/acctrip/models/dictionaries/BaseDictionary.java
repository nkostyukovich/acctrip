package com.dzebsu.acctrip.models.dictionaries;

import com.dzebsu.acctrip.models.BaseModel;

public abstract class BaseDictionary extends BaseModel {

	private String name;

	public BaseDictionary() {
	}

	public BaseDictionary(long id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
