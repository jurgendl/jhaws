package org.jhaws.common.lang;

public class IntegerValue extends Value<Integer> {
	private static final long serialVersionUID = -1530814975734843133L;

	public IntegerValue() {
		super(0);
	}

	public IntegerValue(int value) {
		super(value);
	}

	public IntegerValue add() {
		return add(1);
	}

	public IntegerValue remove() {
		return add(-1);
	}

	public IntegerValue add(int i) {
		set(get() + i);
		return this;
	}

	public IntegerValue remove(int i) {
		return add(-i);
	}
}
