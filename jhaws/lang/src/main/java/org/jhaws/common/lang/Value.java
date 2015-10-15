package org.jhaws.common.lang;

/**
 * @author Jurgen
 */
public class Value<T> {
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
}
