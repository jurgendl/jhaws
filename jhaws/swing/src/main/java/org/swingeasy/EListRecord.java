package org.swingeasy;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Jurgen
 */
public class EListRecord<T> implements Comparable<EListRecord<? super T>> {
	protected T bean;

	public EListRecord() {
		super();
	}

	public EListRecord(T bean) {
		super();
		this.bean = bean;
	}

	/**
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final EListRecord<? super T> other) {
		return new CompareToBuilder().append(this.bean, other.bean).toComparison();
	}

	/**
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof EListRecord)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		EListRecord castOther = (EListRecord) other;
		return new EqualsBuilder().append(this.bean, castOther.bean).isEquals();
	}

	public T get() {
		return this.bean;
	}

	public String getStringValue() {
		return String.valueOf(this.bean);
	}

	public String getTooltip() {
		return this.getStringValue();
	}

	/**
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.bean).toHashCode();
	}

	public void set(T newValue) {
		this.bean = newValue;
	}

	/**
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getStringValue();
	}
}
