package org.jhaws.common.lang;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanValue extends Value<Boolean> {
	private static final long serialVersionUID = -1530814975734843133L;

	public BooleanValue() {
		super(Boolean.FALSE);
	}

	public BooleanValue(Boolean value) {
		super(value);
	}

	public BooleanValue not() {
		set(!Boolean.TRUE.equals(get()));
		return this;
	}

	public Boolean is() {
		return get();
	}
}
