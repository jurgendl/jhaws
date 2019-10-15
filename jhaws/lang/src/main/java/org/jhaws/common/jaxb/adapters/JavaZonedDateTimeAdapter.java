package org.jhaws.common.jaxb.adapters;

import java.time.ZonedDateTime;

import javax.xml.datatype.XMLGregorianCalendar;

public class JavaZonedDateTimeAdapter extends XMLGregorianCalendarAdapter<ZonedDateTime> {
	@Override
	public XMLGregorianCalendar marshal(ZonedDateTime v) throws Exception {
		if (v == null) {
			return null;
		}
		int offset = v.getZone().getRules().getOffset(v.toLocalDateTime()).getTotalSeconds() / 3600;
		return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthValue(), v.getDayOfMonth(), v.getHour(),
				v.getMinute(), v.getSecond(), v.getNano() / 1000 / 1000, offset);
	}

	@Override
	public ZonedDateTime unmarshal(XMLGregorianCalendar v) throws Exception {
		if (v == null) {
			return null;
		}
		int millisecond = v.getMillisecond();
		if (Integer.MIN_VALUE == millisecond) {
			millisecond = 0;
		}
		java.time.ZoneId z = java.time.ZoneId.of(v.getTimeZone(v.getTimezone()).getID());
		return ZonedDateTime.of(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(),
				millisecond * 1000 * 1000, z);
	}
}
