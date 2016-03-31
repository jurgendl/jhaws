package org.jhaws.common.lang;

public class LongValue extends Value<Long> {
	private static final long serialVersionUID = -1530814975734843133L;

	public LongValue() {
		super(0l);
	}

	public LongValue(long value) {
		super(value);
	}

	public LongValue add() {
		return add(1);
	}

	public LongValue remove() {
		return add(-1);
	}

	public LongValue add(long i) {
		set(get() + i);
		return this;
	}

	public LongValue remove(long i) {
		return add(-i);
	}
}
