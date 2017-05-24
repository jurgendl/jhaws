package org.jhaws.common.io.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbUnAdapter extends XmlAdapter<Object, Object> {
	@Override
	public Object marshal(Object arg0) throws Exception {
		return arg0;
	}

	@Override
	public Object unmarshal(Object arg0) throws Exception {
		return arg0;
	}
}
