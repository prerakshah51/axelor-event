package com.axelor.event.web;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.exception.IExceptionEvent;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class DiscountController {

	public void checkBeforeDays(ActionRequest req, ActionResponse res) {
		Event event = req.getContext().getParent().asType(Event.class);
		Discount discount = req.getContext().asType(Discount.class);
		long openCloseDuration = ChronoUnit.DAYS.between(event.getRegistrationOpen(), event.getRegistrationClose());
		long beforeDays = discount.getBeforeDays();
		if (beforeDays > openCloseDuration || beforeDays < 0) {
			res.setError(I18n.get(IExceptionEvent.BEFORE_DAYS));
		}
	}

	public void setDiscountAmount(ActionRequest req, ActionResponse res) {
		Event event = req.getContext().getParent().asType(Event.class);
		Discount discount = req.getContext().asType(Discount.class);
		BigDecimal discountAmount = discount.getDiscountPercentage().multiply(event.getEventFees())
				.divide(new BigDecimal(100));
		res.setValue("discountAmount", discountAmount);
	}
}
