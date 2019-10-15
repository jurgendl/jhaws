package org.jhaws.common.jaxb.adapters;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDateTime;

public class JodaLocalDateTimeAdapter extends XMLGregorianCalendarAdapter<LocalDateTime> {
	@Override
	public XMLGregorianCalendar marshal(LocalDateTime v) throws Exception {
		if (v == null) {
			return null;
		}
		return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(),
				v.getHourOfDay(), v.getMinuteOfHour(), v.getSecondOfMinute(), v.getMillisOfSecond(),
				DatatypeConstants.FIELD_UNDEFINED);
	}

	@Override
	public LocalDateTime unmarshal(XMLGregorianCalendar v) throws Exception {
		if (v == null) {
			return null;
		}
		int millisecond = v.getMillisecond();
		if (Integer.MIN_VALUE == millisecond) {
			millisecond = 0;
		}
		return new LocalDateTime(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(),
				millisecond);
	}
}
