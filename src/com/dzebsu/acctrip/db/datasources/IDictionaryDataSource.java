package com.dzebsu.acctrip.db.datasources;

import java.util.List;

import com.dzebsu.acctrip.models.dictionaries.BaseDictionary;

public interface IDictionaryDataSource<T extends BaseDictionary> {

	long insertEntity(T entity);

	void updateEntity(T entity);

	T getEntityById(long id);

	List<T> getEntityList();

	void deleteEntity(long id);

}
