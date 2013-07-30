package com.dzebsu.acctrip.dictionary.utils;

import java.util.HashMap;
import java.util.Map;

import com.dzebsu.acctrip.db.EventAccContract.Place;
import com.dzebsu.acctrip.dictionary.DictionaryType;
import com.dzebsu.acctrip.models.dictionaries.Category;
import com.dzebsu.acctrip.models.dictionaries.Currency;

public class DictUtils {

	private static Map<Class<?>, DictionaryType> dictTypeMapping;

	static {
		dictTypeMapping = new HashMap<Class<?>, DictionaryType>();
		dictTypeMapping.put(Place.class, DictionaryType.PLACE);
		dictTypeMapping.put(Category.class, DictionaryType.CATEGORY);
		dictTypeMapping.put(Currency.class, DictionaryType.CURRENCY);
	}

	public static DictionaryType getDictionaryType(Class<?> clazz) {
		return dictTypeMapping.get(clazz);
	}

}
