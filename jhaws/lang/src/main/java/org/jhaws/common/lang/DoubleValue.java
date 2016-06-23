package org.jhaws.common.lang;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DoubleValue extends Value<Double> {
	private static final long serialVersionUID = -1530814975734843133L;

	public DoubleValue() {
		super(0.0);
	}

	public DoubleValue(double value) {
		super(value);
	}

	public DoubleValue add(double i) {
		set(get() + i);
		return this;
	}

	public DoubleValue remove(double i) {
		return add(-i);
	}
}
