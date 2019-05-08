package org.jhaws.common.jaxb.adapters;

import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDateTime;

/**
 * JAXB-adapter om LocalDate om te zetten in XMLGregorianCalendar.
 * 
 * @author bnootaer
 */
public class JodaLocalDateTimeAdapter extends XmlAdapter<XMLGregorianCalendar, LocalDateTime> {
    private DatatypeFactory datatypeFactory;

    public JodaLocalDateTimeAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converteert een datum naar een XmlGregorianCalendar
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public XMLGregorianCalendar marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return null;
        }

        int offset = TimeZone.getDefault().getOffset(v.toDateTime().toDate().getTime()) / 60 / 1000;
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(), v.getHourOfDay(), v.getMinuteOfHour(),
                v.getSecondOfMinute(), v.getMillisOfSecond(), offset);
    }

    /**
     * Converteert Xml gregorian calendar.
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public LocalDateTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        int millisecond = v.getMillisecond();
        if (Integer.MIN_VALUE == millisecond) {
            millisecond = 0;
        }

        return new LocalDateTime(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(), millisecond);
    }
}
