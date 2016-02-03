package org.jhaws.common.lang;

import java.io.Serializable;

public class Value<T> implements Serializable {
	private static final long serialVersionUID = -5341543889953418944L;

	public static <T> Value<T> value(T value) {
		return new Value<>(value);
	}

	protected T value;

	public Value() {
		super();
	}

	public Value(T value) {
		this.setValue(value);
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T get() {
		return getValue();
	}

	public void set(T value) {
		setValue(value);
	}

	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value<?> other = (Value<?>) obj;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}
}