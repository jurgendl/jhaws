package org.jhaws.common.jaxb.adapters;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

public class JavaLocalDateAdapter extends XMLGregorianCalendarAdapter<LocalDate> {
	@Override
	public XMLGregorianCalendar marshal(LocalDate v) throws Exception {
		if (v == null) {
			return null;
		}
		return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthValue(), v.getDayOfMonth(),
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	@Override
	public LocalDate unmarshal(XMLGregorianCalendar v) throws Exception {
		if (v == null) {
			return null;
		}
		return LocalDate.of(v.getYear(), v.getMonth(), v.getDay());
	}
}
