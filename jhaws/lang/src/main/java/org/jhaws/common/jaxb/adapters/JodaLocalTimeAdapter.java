package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalTime;

public class JodaLocalTimeAdapter extends XmlAdapter<XMLGregorianCalendar, LocalTime> {
    private DatatypeFactory datatypeFactory;

    public JodaLocalTimeAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public XMLGregorianCalendar marshal(LocalTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED, v.getHourOfDay(), v.getMinuteOfHour(), v.getSecondOfMinute(), v.getMillisOfSecond(),
                DatatypeConstants.FIELD_UNDEFINED);
    }

    /**
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public LocalTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        return new LocalTime(v.getHour(), v.getMinute(), v.getSecond(), v.getMillisecond());
    }
}
