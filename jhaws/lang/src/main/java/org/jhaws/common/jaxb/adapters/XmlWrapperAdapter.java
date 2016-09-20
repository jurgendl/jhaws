package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jhaws.common.jaxb.XmlWrapper;

public class XmlWrapperAdapter extends XmlAdapter<Object, XmlWrapper<Object>> {

	@Override
	public XmlWrapper<Object> unmarshal(Object v) throws Exception {
		return new XmlWrapper<Object>(v);
	}

	@Override
	public Object marshal(XmlWrapper<Object> v) throws Exception {
		return v.getValue();
	}

}
