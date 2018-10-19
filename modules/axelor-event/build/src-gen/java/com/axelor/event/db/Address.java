package com.axelor.event.db;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "EVENT_ADDRESS", indexes = { @Index(columnList = "fullName") })
public class Address extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ADDRESS_SEQ")
	@SequenceGenerator(name = "EVENT_ADDRESS_SEQ", sequenceName = "EVENT_ADDRESS_SEQ", allocationSize = 1)
	private Long id;

	private String flatHouseNumber;

	private String street;

	private String landmark;

	private String city;

	private String country;

	@Widget(readonly = true)
	@NameColumn
	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private String fullName;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Address() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getFlatHouseNumber() {
		return flatHouseNumber;
	}

	public void setFlatHouseNumber(String flatHouseNumber) {
		this.flatHouseNumber = flatHouseNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFullName() {
		try {
			fullName = computeFullName();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getFullName()", e);
		}
		return fullName;
	}

	protected String computeFullName() {
		return flatHouseNumber + ", " + street + ", " + landmark + ", " + city + ", " + country;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Address)) return false;

		final Address other = (Address) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("flatHouseNumber", getFlatHouseNumber())
			.add("street", getStreet())
			.add("landmark", getLandmark())
			.add("city", getCity())
			.add("country", getCountry())
			.omitNullValues()
			.toString();
	}
}
