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

	public void checkBeforeDays(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().getParent().asType(Event.class);
		Discount discount = request.getContext().asType(Discount.class);
		long openCloseDuration = ChronoUnit.DAYS.between(event.getRegistrationOpen(), event.getRegistrationClose());
		long beforeDays = discount.getBeforeDays();
		if (beforeDays > openCloseDuration || beforeDays < 0) {
			response.setError(I18n.get(IExceptionEvent.BEFORE_DAYS));
		}
	}

	public void setDiscountAmount(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().getParent().asType(Event.class);
		Discount discount = request.getContext().asType(Discount.class);
		BigDecimal discountAmount = discount.getDiscountPercentage().multiply(event.getEventFees())
				.divide(new BigDecimal(100));
		response.setValue("discountAmount", discountAmount);
	}
}
