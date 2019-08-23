package org.jhaws.common.jaxb.adapters;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class TimeAdapter extends XmlAdapter<XMLGregorianCalendar, Time> {
    /**
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public XMLGregorianCalendar marshal(Time v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = GregorianCalendar.class.cast(Calendar.getInstance());
        gregorianCalendar.setTime(v);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

    /**
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Time unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = v.toGregorianCalendar();
        return new Time(gregorianCalendar.getTime().getTime());
    }
}
