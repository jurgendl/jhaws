package org.jhaws.common.web.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassAdapter extends XmlAdapter<String, Class<?>> {
	@Override
	public Class<?> unmarshal(String v) throws Exception {
		return v == null ? null : Class.forName(v);
	}

	@Override
	public String marshal(Class<?> v) throws Exception {
		return v == null ? null : v.getName();
	}
}