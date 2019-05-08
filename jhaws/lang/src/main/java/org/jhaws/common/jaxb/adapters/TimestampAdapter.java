package org.jhaws.common.jaxb.adapters;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class TimestampAdapter extends XmlAdapter<XMLGregorianCalendar, Timestamp> {
    /**
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public XMLGregorianCalendar marshal(Timestamp v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = GregorianCalendar.class.cast(GregorianCalendar.getInstance());
        gregorianCalendar.setTime(v);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

    /**
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Timestamp unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = v.toGregorianCalendar();
        return new Timestamp(gregorianCalendar.getTime().getTime());
    }
}
