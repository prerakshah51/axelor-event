package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {

	void setEventEntryandAmountCollected(EventRegistration eventReg);

	EventRegistration setAmount(Event event, EventRegistration eventReg);

	Event setEventEntryandAmountCollected1(EventRegistration eventReg, Event event);

	void checkEventRegistrationDate(Event event, EventRegistration eventReg);

}
