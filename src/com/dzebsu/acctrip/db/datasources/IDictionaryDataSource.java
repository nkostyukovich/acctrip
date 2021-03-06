package com.dzebsu.acctrip.db.datasources;

import java.util.List;

import android.content.Context;

import com.dzebsu.acctrip.models.Operation;
import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;

public interface IDictionaryDataSource<T extends BaseDictionary> {

	long insertEntity(T entity);

	void updateEntity(T entity);

	T getEntityById(long id);

	List<Operation> getOperationListByEntityId(long id);

	List<T> getEntityList();

	void deleteEntity(long id);

	void setContext(Context ctx);

}
