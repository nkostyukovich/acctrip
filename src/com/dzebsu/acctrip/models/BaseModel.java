package com.dzebsu.acctrip.models;

public abstract class BaseModel {

	private long id;

	public BaseModel() {
	}

	public BaseModel(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
