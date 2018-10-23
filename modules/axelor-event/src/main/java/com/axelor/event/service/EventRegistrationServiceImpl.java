package com.axelor.event.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.validation.ValidationException;
import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IExceptionEvent;
import com.axelor.i18n.I18n;
import com.google.inject.Inject;

public class EventRegistrationServiceImpl implements EventRegistrationService {

	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationRepository eventRegRepo;

	@Override
	public EventRegistration setAmount(Event event, EventRegistration eventReg) {
		if (event.getDiscounts() != null && event.getDiscounts().size() > 0) {
			long daysLeft = ChronoUnit.DAYS.between(eventReg.getRegistrationDate().toLocalDate(),
					event.getRegistrationClose());
			List<Discount> discountList = event.getDiscounts();
			for (Discount discount : discountList) {
				long beforeDays = discount.getBeforeDays();
				if (daysLeft == beforeDays) {
					BigDecimal amount = event.getEventFees().subtract(discount.getDiscountAmount());
					eventReg.setAmount(amount);
					break;
				} else if (daysLeft >= beforeDays) {
					BigDecimal amount = event.getEventFees().subtract(discount.getDiscountAmount());
					eventReg.setAmount(amount);
				} else {
					eventReg.setAmount(event.getEventFees());
				}
			}
		} else {
			eventReg.setAmount(event.getEventFees());
		}
		return eventReg;
	}

	@Override
	public void setEventComputationalData(EventRegistration eventReg) {
		Event event = eventRepo.all().filter("self.reference = ?", eventReg.getEvent().getReference()).fetchOne();
		Event tempEvent = eventRepo.all().filter("self.reference = ?", eventReg.getEvent().getReference()).fetchOne();
		BigDecimal totalDiscount = event.getEventFees().subtract(eventReg.getAmount());
		tempEvent.setTotalEntry(event.getTotalEntry() + 1);
		tempEvent.setAmountCollected(event.getAmountCollected().add(eventReg.getAmount()));
		tempEvent.setTotalDiscount(event.getTotalDiscount().add(totalDiscount));
		eventRepo.save(tempEvent);
	}

	@Override
	public Event setEventComputationalData(EventRegistration eventReg, Event event) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalDiscount = BigDecimal.ZERO;
		int totalEntry = 0;
		List<EventRegistration> eventRegList = event.getEventRegistration();
		for (EventRegistration eventRegistration : eventRegList) {
			totalAmount = totalAmount.add(eventRegistration.getAmount());
			totalDiscount = totalDiscount.add(event.getEventFees().subtract(eventRegistration.getAmount()));
			totalEntry = totalEntry + 1;
		}
		event.setAmountCollected(totalAmount);
		event.setTotalDiscount(totalDiscount);
		event.setTotalEntry(totalEntry);
		return event;
	}

	@Override
	public void checkEventRegistrationDate(Event event, EventRegistration eventReg) {
		LocalDate regClose = event.getRegistrationClose();
		LocalDate regOpen = event.getRegistrationOpen();
		LocalDate regDate = eventReg.getRegistrationDate().toLocalDate();
		if (regDate.isAfter(regClose) || regDate.isBefore(regOpen)) {
			throw new ValidationException(I18n.get(IExceptionEvent.REGISTRATION_DATE_BETWEEN));
		}
	}
}
