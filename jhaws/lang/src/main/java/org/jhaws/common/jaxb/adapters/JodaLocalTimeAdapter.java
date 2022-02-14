package org.jhaws.common.jaxb.adapters;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalTime;

public class JodaLocalTimeAdapter extends XMLGregorianCalendarAdapter<LocalTime> {
    @Override
    public XMLGregorianCalendar marshal(LocalTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, v.getHourOfDay(), v.getMinuteOfHour(), v.getSecondOfMinute(), v.getMillisOfSecond(),
                DatatypeConstants.FIELD_UNDEFINED);
    }

    @Override
    public LocalTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        return new LocalTime(v.getHour(), v.getMinute(), v.getSecond(), v.getMillisecond());
    }
}
