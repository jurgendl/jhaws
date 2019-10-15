package org.jhaws.common.jaxb.adapters;

import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

public class JodaDateTimeAdapter extends XmlAdapter<XMLGregorianCalendar, DateTime> {
    private DatatypeFactory datatypeFactory;

    public JodaDateTimeAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public XMLGregorianCalendar marshal(DateTime v) throws Exception {
        if (v == null) {
            return null;
        }

        int offset = TimeZone.getDefault().getOffset(v.toDateTime().toDate().getTime()) / 60 / 1000;
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(), v.getHourOfDay(), v.getMinuteOfHour(),
                v.getSecondOfMinute(), v.getMillisOfSecond(), offset);
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

        return new DateTime(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(), millisecond);
    }
}
