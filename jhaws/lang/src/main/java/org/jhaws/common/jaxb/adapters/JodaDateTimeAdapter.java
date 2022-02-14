package org.jhaws.common.jaxb.adapters;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class JodaDateTimeAdapter extends XMLGregorianCalendarAdapter<DateTime> {
    @Override
    public XMLGregorianCalendar marshal(DateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        long millisOffset = v.getZone().getOffset(v);
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(), v.getHourOfDay(), v.getMinuteOfHour(), v.getSecondOfMinute(), v.getMillisOfSecond(), (int) (millisOffset / 1000 / 3600));
    }

    @Override
    public DateTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        int millisecond = v.getMillisecond();
        if (Integer.MIN_VALUE == millisecond) {
            millisecond = 0;
        }
        return new DateTime(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(), millisecond, DateTimeZone.forOffsetHours(v.getTimezone()));
    }
}
