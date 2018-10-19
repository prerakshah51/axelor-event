package com.axelor.event.exception;

public interface IExceptionEvent {
	
	static final String IMPORT_SUCCESS = /* $$( */ "Event registartion import completed" /* ) */;
	static final String IMPORT_FAIL = /* $$( */ "Event registartion import failed" /* ) */;
	static final String REGISTRATION_OPEN_DATE1 = /* $$( */ "Registartion open date should not after event start date" /* ) */;
	static final String REGISTRATION_OPEN_DATE2 = /* $$( */ "Registartion open date should not after event end date" /* ) */;
	static final String REGISTRATION_CLOSE_DATE1 = /* $$( */ "Registartion close date should not after event start date" /* ) */;
	static final String REGISTRATION_CLOSE_DATE2 = /* $$( */ "Registartion close date should not after event end date" /* ) */;
	static final String REGISTRATION_DATE = /* $$( */ "Registration date is not valid for record: " /* ) */;
	static final String EXCEEDS_CAPACITY = /* $$( */ "Event capacity exceeds" /* ) */;
	static final String INVALID_EMAIL = /* $$( */ "This email id is not valid" /* ) */;
	static final String BEFORE_DAYS = /* $$( */ "This 'before days' value is not possible" /* ) */;
}
