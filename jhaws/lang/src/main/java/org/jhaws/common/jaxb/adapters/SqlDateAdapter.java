package org.jhaws.common.jaxb.adapters;

import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class SqlDateAdapter extends XmlAdapter<XMLGregorianCalendar, java.sql.Date> {
    /**
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public XMLGregorianCalendar marshal(java.sql.Date v) throws Exception {
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
    public java.sql.Date unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = v.toGregorianCalendar();
        return new java.sql.Date(gregorianCalendar.getTime().getTime());
    }
}
