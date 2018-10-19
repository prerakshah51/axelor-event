package com.axelor.event.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "EVENT_EVENT", indexes = { @Index(columnList = "reference"), @Index(columnList = "venue") })
public class Event extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_EVENT_SEQ")
	@SequenceGenerator(name = "EVENT_EVENT_SEQ", sequenceName = "EVENT_EVENT_SEQ", allocationSize = 1)
	private Long id;

	@NameColumn
	private String reference;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address venue;

	private LocalDate registrationOpen;

	private LocalDate registrationClose;

	private Integer capacity = 0;

	private BigDecimal eventFees = BigDecimal.ZERO;

	@Widget(multiline = true)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Discount> discounts;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventRegistration> eventRegistration;

	@Widget(readonly = true)
	private Integer totalEntry = 0;

	@Widget(readonly = true)
	private BigDecimal amountCollected = BigDecimal.ZERO;

	@Widget(readonly = true)
	private BigDecimal totalDiscount = BigDecimal.ZERO;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Event() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Address getVenue() {
		return venue;
	}

	public void setVenue(Address venue) {
		this.venue = venue;
	}

	public LocalDate getRegistrationOpen() {
		return registrationOpen;
	}

	public void setRegistrationOpen(LocalDate registrationOpen) {
		this.registrationOpen = registrationOpen;
	}

	public LocalDate getRegistrationClose() {
		return registrationClose;
	}

	public void setRegistrationClose(LocalDate registrationClose) {
		this.registrationClose = registrationClose;
	}

	public Integer getCapacity() {
		return capacity == null ? 0 : capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getEventFees() {
		return eventFees == null ? BigDecimal.ZERO : eventFees;
	}

	public void setEventFees(BigDecimal eventFees) {
		this.eventFees = eventFees;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	/**
	 * Add the given {@link Discount} item to the {@code discounts}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addDiscount(Discount item) {
		if (getDiscounts() == null) {
			setDiscounts(new ArrayList<>());
		}
		getDiscounts().add(item);
	}

	/**
	 * Remove the given {@link Discount} item from the {@code discounts}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeDiscount(Discount item) {
		if (getDiscounts() == null) {
			return;
		}
		getDiscounts().remove(item);
	}

	/**
	 * Clear the {@code discounts} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearDiscounts() {
		if (getDiscounts() != null) {
			getDiscounts().clear();
		}
	}

	public List<EventRegistration> getEventRegistration() {
		return eventRegistration;
	}

	public void setEventRegistration(List<EventRegistration> eventRegistration) {
		this.eventRegistration = eventRegistration;
	}

	/**
	 * Add the given {@link EventRegistration} item to the {@code eventRegistration}.
	 *
	 * <p>
	 * It sets {@code item.event = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEventRegistration(EventRegistration item) {
		if (getEventRegistration() == null) {
			setEventRegistration(new ArrayList<>());
		}
		getEventRegistration().add(item);
		item.setEvent(this);
	}

	/**
	 * Remove the given {@link EventRegistration} item from the {@code eventRegistration}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEventRegistration(EventRegistration item) {
		if (getEventRegistration() == null) {
			return;
		}
		getEventRegistration().remove(item);
	}

	/**
	 * Clear the {@code eventRegistration} collection.
	 *
	 * <p>
	 * If you have to query {@link EventRegistration} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEventRegistration() {
		if (getEventRegistration() != null) {
			getEventRegistration().clear();
		}
	}

	public Integer getTotalEntry() {
		return totalEntry == null ? 0 : totalEntry;
	}

	public void setTotalEntry(Integer totalEntry) {
		this.totalEntry = totalEntry;
	}

	public BigDecimal getAmountCollected() {
		return amountCollected == null ? BigDecimal.ZERO : amountCollected;
	}

	public void setAmountCollected(BigDecimal amountCollected) {
		this.amountCollected = amountCollected;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount == null ? BigDecimal.ZERO : totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
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
		if (!(obj instanceof Event)) return false;

		final Event other = (Event) obj;
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
			.add("reference", getReference())
			.add("startDate", getStartDate())
			.add("endDate", getEndDate())
			.add("registrationOpen", getRegistrationOpen())
			.add("registrationClose", getRegistrationClose())
			.add("capacity", getCapacity())
			.add("eventFees", getEventFees())
			.add("description", getDescription())
			.add("totalEntry", getTotalEntry())
			.add("amountCollected", getAmountCollected())
			.add("totalDiscount", getTotalDiscount())
			.omitNullValues()
			.toString();
	}
}
