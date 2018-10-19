package com.axelor.event.db;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "EVENT_DISCOUNT")
public class Discount extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_DISCOUNT_SEQ")
	@SequenceGenerator(name = "EVENT_DISCOUNT_SEQ", sequenceName = "EVENT_DISCOUNT_SEQ", allocationSize = 1)
	private Long id;

	private Integer beforeDays = 0;

	private BigDecimal discountPercentage = BigDecimal.ZERO;

	private BigDecimal discountAmount = BigDecimal.ZERO;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Discount() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBeforeDays() {
		return beforeDays == null ? 0 : beforeDays;
	}

	public void setBeforeDays(Integer beforeDays) {
		this.beforeDays = beforeDays;
	}

	public BigDecimal getDiscountPercentage() {
		return discountPercentage == null ? BigDecimal.ZERO : discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount == null ? BigDecimal.ZERO : discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
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
		if (!(obj instanceof Discount)) return false;

		final Discount other = (Discount) obj;
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
			.add("beforeDays", getBeforeDays())
			.add("discountPercentage", getDiscountPercentage())
			.add("discountAmount", getDiscountAmount())
			.omitNullValues()
			.toString();
	}
}
