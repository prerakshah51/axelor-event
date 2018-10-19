package com.axelor.event.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.event.db.Discount;

public class DiscountRepository extends JpaRepository<Discount> {

	public DiscountRepository() {
		super(Discount.class);
	}

}

