package com.axelor.event.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.event.db.EventRegistration;

public class EventRegistrationRepository extends JpaRepository<EventRegistration> {

	public EventRegistrationRepository() {
		super(EventRegistration.class);
	}

	public EventRegistration findByName(String name) {
		return Query.of(EventRegistration.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

