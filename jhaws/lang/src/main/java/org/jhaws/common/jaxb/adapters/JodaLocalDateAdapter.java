package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;

public class JodaLocalDateAdapter extends XmlAdapter<XMLGregorianCalendar, LocalDate> {

    private DatatypeFactory datatypeFactory;

    public JodaLocalDateAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public XMLGregorianCalendar marshal(LocalDate v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthOfYear(), v.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED);
    }

    @Override
    public LocalDate unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        return new LocalDate(v.getYear(), v.getMonth(), v.getDay());
    }
}
