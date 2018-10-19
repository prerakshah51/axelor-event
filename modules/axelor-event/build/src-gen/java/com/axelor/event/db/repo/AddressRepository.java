package com.axelor.event.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.event.db.Address;

public class AddressRepository extends JpaRepository<Address> {

	public AddressRepository() {
		super(Address.class);
	}

}

