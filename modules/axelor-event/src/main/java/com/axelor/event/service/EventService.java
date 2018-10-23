package com.axelor.event.service;

import java.io.IOException;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.exception.AxelorException;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.MetaModel;

public interface EventService {

	void importRegistration(MetaFile dataFile, Event event) throws IOException;

	EventRegistration setEventData(EventRegistration eventReg);

	void sendEmail(Event event, MetaModel metaModel) throws ClassNotFoundException, InstantiationException, IllegalAccessException, AxelorException, IOException;

}
