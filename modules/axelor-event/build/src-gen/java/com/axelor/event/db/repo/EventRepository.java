package com.axelor.event.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.event.db.Event;

public class EventRepository extends JpaRepository<Event> {

	public EventRepository() {
		super(Event.class);
	}

}

