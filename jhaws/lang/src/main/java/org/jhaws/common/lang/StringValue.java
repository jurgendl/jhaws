package org.jhaws.common.lang;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringValue extends Value<String> {
	private static final long serialVersionUID = 6327052574471636557L;

	public StringValue() {
		super(null);
	}

	public StringValue(String value) {
		super(value);
	}
}
