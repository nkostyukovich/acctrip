package com.dzebsu.acctrip.models.dictionaries;

public class Currency extends BaseDictionary {

	private String code;

	public Currency() {
		super();
	}

	public Currency(long id) {
		super(id);
	}

	public Currency(long id, String name, String code) {
		super(id, name);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
