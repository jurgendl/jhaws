package org.jhaws.common.jaxb.adapters;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class JavaLocalDateAdapter extends XmlAdapter<XMLGregorianCalendar, LocalDate> {

    private DatatypeFactory datatypeFactory;

    public JavaLocalDateAdapter() {
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
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthValue(), v.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED);
    }

    @Override
    public LocalDate unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        return LocalDate.of(v.getYear(), v.getMonth(), v.getDay());
    }
}
