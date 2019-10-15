package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public abstract class XMLGregorianCalendarAdapter<X> extends XmlAdapter<XMLGregorianCalendar, X> {
	protected DatatypeFactory datatypeFactory;

	public XMLGregorianCalendarAdapter() {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
}
