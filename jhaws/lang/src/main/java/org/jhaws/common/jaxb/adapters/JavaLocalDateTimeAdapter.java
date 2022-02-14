package org.jhaws.common.jaxb.adapters;

import java.time.LocalDateTime;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

public class JavaLocalDateTimeAdapter extends XMLGregorianCalendarAdapter<LocalDateTime> {
    @Override
    public XMLGregorianCalendar marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthValue(), v.getDayOfMonth(), v.getHour(), v.getMinute(), v.getSecond(), v.getNano() / 1000 / 1000, DatatypeConstants.FIELD_UNDEFINED);
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
        return LocalDateTime.of(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(), millisecond * 1000 * 1000);
    }
}
