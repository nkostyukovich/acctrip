package com.dzebsu.acctrip.dictionary;

import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;

public interface IDialogListener<T extends BaseDictionary> {

	void onSuccess(T entity);

}
