package org.jhaws.common.lang;

public class FloatValue extends Value<Float> {
	private static final long serialVersionUID = -1530814975734843133L;

	public FloatValue() {
		super(0f);
	}

	public FloatValue(float value) {
		super(value);
	}

	public FloatValue add(float i) {
		set(get() + i);
		return this;
	}

	public FloatValue remove(float i) {
		return add(-i);
	}
}
