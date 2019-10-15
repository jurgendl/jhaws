package org.jhaws.common.jaxb.adapters;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class TimestampAdapter extends XmlAdapter<XMLGregorianCalendar, Timestamp> {
    @Override
    public XMLGregorianCalendar marshal(Timestamp v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = GregorianCalendar.class.cast(Calendar.getInstance());
        gregorianCalendar.setTime(v);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

    @Override
    public Timestamp unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = v.toGregorianCalendar();
        return new Timestamp(gregorianCalendar.getTime().getTime());
    }
}
