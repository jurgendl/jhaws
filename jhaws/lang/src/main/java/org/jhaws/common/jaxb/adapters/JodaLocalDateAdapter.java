package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;

/**
 * JAXB-adapter om LocalDate om te zetten in XMLGregorianCalendar.
 * 
 * @author bnootaer
 */
public class JodaLocalDateAdapter extends XmlAdapter<XMLGregorianCalendar, LocalDate> {

    private DatatypeFactory datatypeFactory;

    public JodaLocalDateAdapter() {
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
    public XMLGregorianCalendar marshal(LocalDate v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED);
    }

    /**
     * Converteert Xml gregorian calendar.
     * 
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public LocalDate unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        return new LocalDate(v.getYear(), v.getMonth(), v.getDay());
    }
}
