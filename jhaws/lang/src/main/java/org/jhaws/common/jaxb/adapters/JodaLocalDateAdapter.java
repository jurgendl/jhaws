package org.jhaws.common.jaxb.adapters;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;

public class JodaLocalDateAdapter extends XMLGregorianCalendarAdapter<LocalDate> {
	@Override
	public XMLGregorianCalendar marshal(LocalDate v) throws Exception {
		if (v == null) {
			return null;
		}
		return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(),
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	@Override
	public LocalDate unmarshal(XMLGregorianCalendar v) throws Exception {
		if (v == null) {
			return null;
		}
		return new LocalDate(v.getYear(), v.getMonth(), v.getDay());
	}
}
