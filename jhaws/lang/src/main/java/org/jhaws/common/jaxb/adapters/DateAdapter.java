package org.jhaws.common.jaxb.adapters;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateAdapter extends XMLGregorianCalendarAdapter<Date> {
	@SuppressWarnings("deprecation")
	@Override
	public XMLGregorianCalendar marshal(Date v) throws Exception {
		if (v == null) {
			return null;
		}
		if (v instanceof java.sql.Time) {
			return datatypeFactory.newXMLGregorianCalendar(DatatypeConstants.FIELD_UNDEFINED,
					DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, v.getHours(), v.getMinutes(),
					v.getSeconds(), DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
		}
		return datatypeFactory.newXMLGregorianCalendar(v.getYear() + 1900, v.getMonth() + 1, v.getDay(), v.getHours(),
				v.getMinutes(), v.getSeconds(), DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	@Override
	public Date unmarshal(XMLGregorianCalendar v) throws Exception {
		if (v == null) {
			return null;
		}
		GregorianCalendar gregorianCalendar = v.toGregorianCalendar();
		return gregorianCalendar.getTime();
	}
}
