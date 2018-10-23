package com.axelor.event.web;

import com.axelor.db.JpaSupport;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IExceptionEvent;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Response;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ibm.icu.math.BigDecimal;

public class EventRegistrationController extends JpaSupport {

	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationService eventRegService;

	public Response validateEmail(String email) {
		Response response = new ActionResponse();
		if (!email.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")) {
			response.addError("email", I18n.get(IExceptionEvent.INVALID_EMAIL));
		}
		return response;
	}

	public void hideEventField(ActionRequest request, ActionResponse response) {
		String check = (String) request.getContext().get("check");
		if (check == null) {
			Event event = request.getContext().getParent().asType(Event.class);
			response.setValue("event", event);
			response.setReadonly("event", true);
		}
	}

	public void setAmount(ActionRequest request, ActionResponse response) {
		String check = (String) request.getContext().get("check");
		if (check == null) {
			Event event = request.getContext().getParent().asType(Event.class);
			EventRegistration eventReg = request.getContext().asType(EventRegistration.class);
			eventReg = eventRegService.setAmount(event, eventReg);
			response.setValue("amount", eventReg.getAmount());
		} else {
			EventRegistration eventReg = request.getContext().asType(EventRegistration.class);
			Event event = eventRepo.all().filter("self.reference = ?", eventReg.getEvent().getReference()).fetchOne();
			eventReg = eventRegService.setAmount(event, eventReg);
			response.setValue("amount", eventReg.getAmount());
		}
	}

	@Transactional
	public void setEventComputationalData(ActionRequest request, ActionResponse response) {
		String check = (String) request.getContext().get("check");
		if (check == null) {
			Event event = request.getContext().asType(Event.class);
			if (event.getEventRegistration() != null && !event.getEventRegistration().isEmpty()) {
				int eventRegSize = event.getEventRegistration().size();
				EventRegistration eventReg = event.getEventRegistration().get(eventRegSize - 1);
				if (eventRegSize > event.getCapacity()) {
					response.setError(I18n.get(IExceptionEvent.EXCEEDS_CAPACITY));
				} else {
					event = eventRegService.setEventComputationalData(eventReg, event);
					response.setValue("amountCollected", event.getAmountCollected());
					response.setValue("totalEntry", event.getTotalEntry());
					response.setValue("totalDiscount", event.getTotalDiscount());
				}
			} else {
				response.setValue("amountCollected", BigDecimal.ZERO);
				response.setValue("totalEntry", 0);
				response.setValue("totalDiscount", BigDecimal.ZERO);
			}
		} else {
			EventRegistration eventReg = request.getContext().asType(EventRegistration.class);
			Event event = eventRepo.all().filter("self.reference = ?", eventReg.getEvent().getReference()).fetchOne();
			if (event.getTotalEntry() + 1 > event.getCapacity()) {
				response.setError(I18n.get(IExceptionEvent.EXCEEDS_CAPACITY));
			} else {
				eventRegService.setEventComputationalData(eventReg);
			}
		}
	}

	public void checkEventRegistrationDate(ActionRequest request, ActionResponse response) {
		String check = (String) request.getContext().get("check");
		if (check == null) {
			Event event = request.getContext().getParent().asType(Event.class);
			EventRegistration eventReg = request.getContext().asType(EventRegistration.class);
			eventRegService.checkEventRegistrationDate(event, eventReg);
		} else {
			EventRegistration eventReg = request.getContext().asType(EventRegistration.class);
			Event event = eventRepo.all().filter("self.reference = ?", eventReg.getEvent().getReference()).fetchOne();
			eventRegService.checkEventRegistrationDate(event, eventReg);
		}
	}
}
