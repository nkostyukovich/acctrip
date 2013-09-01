package com.dzebsu.acctrip.db.datasources;

import java.util.Set;

import android.content.Context;

import com.dzebsu.acctrip.db.EventAccContract;
import com.dzebsu.acctrip.models.OperationType;

public class StatisticsQueryParams {

	private Long eventId;

	private Set<Long> currencies;

	private Set<Long> categories;

	private Set<Long> places;

	private OperationType type;

	private Long downDate;

	private Long upDate;

	private String day;

	private StatisticsQueryParams() {

	}

	public static StatisticsQueryParams createParamsByDay(Long eventId, String day) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setDay(day);
		return params;
	}

	public static StatisticsQueryParams createParamsByBaseValues(Long eventId, Set<Long> currencies,
			Set<Long> categories, Set<Long> places) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCurrencies(currencies);
		params.setCategories(categories);
		params.setPlaces(places);
		return params;
	}

	public static StatisticsQueryParams createParamsByCurrency(Long eventId, Set<Long> currencies) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCurrencies(currencies);
		return params;
	}

	public static StatisticsQueryParams createParamsByCategory(Long eventId, Set<Long> categories) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCategories(categories);
		return params;
	}

	public static StatisticsQueryParams createParamsByPlace(Long eventId, Set<Long> places) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setPlaces(places);
		return params;
	}

	public static StatisticsQueryParams createParamsByDate(Long eventId, Long downDate, Long upDate) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setDownDate(downDate);
		params.setUpDate(upDate);
		return params;
	}

	public static StatisticsQueryParams createParamsByBaseDateRange(Long eventId, Set<Long> currencies,
			Set<Long> categories, Set<Long> places, Long downDate, Long upDate) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCurrencies(currencies);
		params.setCategories(categories);
		params.setPlaces(places);
		params.setDownDate(downDate);
		params.setUpDate(upDate);
		return params;
	}

	public static StatisticsQueryParams createParamsByBaseOpType(Long eventId, Set<Long> currencies,
			Set<Long> categories, Set<Long> places, OperationType type) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCurrencies(currencies);
		params.setCategories(categories);
		params.setPlaces(places);
		params.setType(type);
		return params;
	}

	public static StatisticsQueryParams createParamsByAll(Long eventId, Set<Long> currencies, Set<Long> categories,
			Set<Long> places, OperationType type, Long downDate, Long upDate) {
		StatisticsQueryParams params = new StatisticsQueryParams();
		params.setEventId(eventId);
		params.setCurrencies(currencies);
		params.setCategories(categories);
		params.setPlaces(places);
		params.setDownDate(downDate);
		params.setUpDate(upDate);
		params.setType(type);
		return params;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Set<Long> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<Long> currencies) {
		this.currencies = currencies;
	}

	public Set<Long> getCategories() {
		return categories;
	}

	public void setCategories(Set<Long> categories) {
		this.categories = categories;
	}

	public Set<Long> getPlaces() {
		return places;
	}

	public void setPlaces(Set<Long> places) {
		this.places = places;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public Long getDownDate() {
		return downDate;
	}

	public void setDownDate(Long downDate) {
		this.downDate = downDate;
	}

	public Long getUpDate() {
		return upDate;
	}

	public void setUpDate(Long upDate) {
		this.upDate = upDate;
	}

	private String addAND(String source) {
		if (!source.equals(""))
			return source + " AND ";
		else return source;
	}

	private String addOR(String source) {
		if (!source.equals(""))
			return source + " OR ";
		else return source;
	}

	private String addGroupOR(Set<Long> ids, String alias, String source) {
		String query = "";
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				query = addOR(query);
				query += alias + "=" + id;
			}
			if (!query.equals("")) {
				source = addAND(source);
				source += "(" + query + ")";
			}
		}

		return source;
	}

	public String getWhereQuery(Context ctx) {
		String query = "";
		if (eventId != null) {
			query = addAND(query);
			query += EventAccContract.Event.ALIAS_ID + "=" + eventId;
		}
		if (type != null) {
			query = addAND(query);
			query += EventAccContract.Operation.TYPE + "=" + type.getLabel(ctx);
		}
		if (downDate != null) {
			query = addAND(query);
			query += EventAccContract.Operation.DATE + ">=" + downDate;
		}
		if (upDate != null) {
			query = addAND(query);
			query += EventAccContract.Operation.DATE + "<=" + upDate;
		}

		query = addGroupOR(currencies, EventAccContract.Currency.ALIAS_ID, query);

		query = addGroupOR(categories, EventAccContract.Category.ALIAS_ID, query);
		query = addGroupOR(places, EventAccContract.Place.ALIAS_ID, query);

		if (day != null) {
			query = addAND(query);
			query += "strftime('%d-%m-%Y'," + EventAccContract.Operation.DATE + "/1000,'unixepoch')='" + day + "'";
		}

		if (!query.equals("")) query = " where " + query + " ";
		return query.toString();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}
