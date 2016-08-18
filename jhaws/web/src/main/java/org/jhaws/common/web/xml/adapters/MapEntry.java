package org.jhaws.common.web.xml.adapters;

import javax.xml.bind.annotation.XmlElement;

import org.jhaws.common.web.xml.XmlWrapper;

public class MapEntry {
	@XmlElement(name = "key", required = false)
	XmlWrapper<?> key;

	@XmlElement(name = "val", required = false)
	XmlWrapper<?> value;

	private MapEntry() {
		super();
	}

	public MapEntry(Object key, Object val) {
		this();

		this.key = new XmlWrapper<>(key);
		this.value = new XmlWrapper<>(val);
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}
}
