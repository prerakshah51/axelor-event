package com.axelor.exception.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.exception.db.TraceBack;

public class TraceBackRepository extends JpaRepository<TraceBack> {

	public TraceBackRepository() {
		super(TraceBack.class);
	}

	public TraceBack findByName(String name) {
		return Query.of(TraceBack.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// TYPE SELECT
	public static final int TYPE_TECHNICAL = 0;
	public static final int TYPE_FUNCTIONNAL = 1;

	// CATEGORY SELECT
	public static final int CATEGORY_MISSING_FIELD = 1;
	public static final int CATEGORY_NO_UNIQUE_KEY = 2;
	public static final int CATEGORY_NO_VALUE = 3;
	public static final int CATEGORY_CONFIGURATION_ERROR = 4;
	public static final int CATEGORY_INCONSISTENCY = 5;
}

