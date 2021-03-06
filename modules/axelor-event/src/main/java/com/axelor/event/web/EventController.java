package com.axelor.event.web;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.quartz.xml.ValidationException;
import com.axelor.db.JpaSupport;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IExceptionEvent;
import com.axelor.event.service.EventService;
import com.axelor.exception.AxelorException;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventController extends JpaSupport {

	@Inject
	EventService eventService;

	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationRepository eventRegRepo;

	@Inject
	MetaModelRepository metaModelRepo;

	@Transactional
	public void sendEmail(ActionRequest request, ActionResponse response) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, AxelorException, IOException {
		Event event = request.getContext().asType(Event.class);
		MetaModel metaModel = metaModelRepo.all().filter("self.fullName = ?", request.getModel()).fetchOne();
		eventService.sendEmail(event, metaModel);
	}

	@SuppressWarnings("unchecked")
	public void importRegistration(ActionRequest request, ActionResponse response) throws IOException {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
		MetaFile dataFile = Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
		File csvFile = MetaFiles.getPath(dataFile).toFile();
		Long eventId = Long.valueOf(request.getContext().get("_id").toString());
		Event event = eventRepo.find(eventId);
		if (Files.getFileExtension(csvFile.getName()).equals("csv")) {
			if (event.getEventRegistration().size() < event.getCapacity()) {
				eventService.importRegistration(dataFile, event);
				response.setFlash(I18n.get(IExceptionEvent.IMPORT_SUCCESS));
			} else {
				response.setError(I18n.get(IExceptionEvent.EXCEEDS_CAPACITY));
			}
		} else {
			response.setError(I18n.get(IExceptionEvent.IMPORT_FAIL));
		}
	}

	public Object importRegistrartion(Object bean, Map<String, Object> context) throws ValidationException {
		Event event = (Event) context.get("_event");
		event.setTotalEntry(event.getTotalEntry() + 1);
		if (event.getTotalEntry() <= event.getCapacity()) {
			assert bean instanceof EventRegistration;
			EventRegistration eventReg = (EventRegistration) bean;
			eventReg.setEvent(event);
			eventReg = eventService.setEventData(eventReg);
			return bean;
		}
		return null;
	}

	public void checkEventDates(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		if (event.getStartDate() != null) {
			if (event.getRegistrationOpen() != null
					&& event.getRegistrationOpen().isAfter(event.getStartDate().toLocalDate())) {
				response.setError(I18n.get(IExceptionEvent.REGISTRATION_OPEN_DATE1));
			}
			if (event.getRegistrationClose() != null
					&& event.getRegistrationClose().isAfter(event.getStartDate().toLocalDate())) {
				response.setError(I18n.get(IExceptionEvent.REGISTRATION_CLOSE_DATE1));
			}
		}
		if (event.getEndDate() != null) {
			if (event.getRegistrationOpen() != null
					&& event.getRegistrationOpen().isAfter(event.getEndDate().toLocalDate())) {
				response.setError(I18n.get(IExceptionEvent.REGISTRATION_OPEN_DATE2));
			}
			if (event.getRegistrationClose() != null
					&& event.getRegistrationClose().isAfter(event.getEndDate().toLocalDate())) {
				response.setError(I18n.get(IExceptionEvent.REGISTRATION_CLOSE_DATE2));
			}
		}
		if (event.getEventRegistration() != null && event.getEventRegistration().size() > 0) {
			LocalDate regOpenDate = event.getRegistrationOpen();
			LocalDate regCloseDate = event.getRegistrationClose();
			List<EventRegistration> eventRegList = event.getEventRegistration();
			for (EventRegistration eventRegistration : eventRegList) {
				LocalDate regDate = eventRegistration.getRegistrationDate().toLocalDate();
				if (regDate.isAfter(regCloseDate) || regDate.isBefore(regOpenDate)) {
					response.setError(I18n.get(IExceptionEvent.REGISTRATION_DATE) + eventRegistration.getName());
				}
			}
		}
	}
}
