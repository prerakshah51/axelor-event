package com.axelor.event.db.repo;

import java.math.BigDecimal;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.google.inject.Inject;

public class EventRegistartionRepositoryImpl extends EventRegistrationRepository {
	
	@Inject
	EventRepository eventRepo;
	
	@Override
	public void remove(EventRegistration entity) {
		super.remove(entity);
		Event event = eventRepo.all().filter("self.reference = ?", entity.getEvent().getReference()).fetchOne();
		BigDecimal amountCollected = event.getAmountCollected();
		amountCollected = amountCollected.subtract(entity.getAmount());
		event.setAmountCollected(amountCollected);
		Integer totalEntry = event.getTotalEntry();
		totalEntry = totalEntry - 1;
		event.setTotalEntry(totalEntry);
		BigDecimal totalDiscount = event.getTotalDiscount();
		totalDiscount = totalDiscount.subtract(event.getEventFees().subtract(entity.getAmount()));
		event.setTotalDiscount(totalDiscount);
		eventRepo.save(event);
	}
}
