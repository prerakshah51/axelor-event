package com.axelor.event.service;

import java.io.IOException;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.meta.db.MetaFile;

public interface EventService {

	void importRegistration(MetaFile dataFile, Event event) throws IOException;

	EventRegistration setEventData(EventRegistration eventReg);

}
