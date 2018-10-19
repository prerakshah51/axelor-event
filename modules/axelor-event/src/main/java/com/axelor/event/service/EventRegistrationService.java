package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {

	void setEventComputationalData(EventRegistration eventReg);

	EventRegistration setAmount(Event event, EventRegistration eventReg);

	Event setEventComputationalData(EventRegistration eventReg, Event event);

	void checkEventRegistrationDate(Event event, EventRegistration eventReg);

}
