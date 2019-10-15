package org.jhaws.common.jaxb.adapters;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class JavaLocalTimeAdapter extends XmlAdapter<XMLGregorianCalendar, LocalTime> {
    private DatatypeFactory datatypeFactory;

    public JavaLocalTimeAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public XMLGregorianCalendar marshal(LocalTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return datatypeFactory.newXMLGregorianCalendar(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED, v.getHour(), v.getMinute(), v.getSecond(), v.getNano() / 1000 / 1000,
                DatatypeConstants.FIELD_UNDEFINED);
    }

    @Override
    public LocalTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        return LocalTime.of(v.getHour(), v.getMinute(), v.getSecond(), v.getMillisecond() * 1000 * 1000);
    }
}
