package com.axelor.event.db.repo;

import java.util.Map;

public class EventRepositoryImpl extends EventRepository {

	@Override
	public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
		if (!context.containsKey("json-enhance")) {
			return json;
		}
		try {

		} catch (Exception e) {
		}
		return json;
	}
}
