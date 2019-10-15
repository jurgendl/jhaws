package org.jhaws.common.jaxb.adapters;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class JavaLocalDateTimeAdapter extends XmlAdapter<XMLGregorianCalendar, LocalDateTime> {
    private DatatypeFactory datatypeFactory;

    public JavaLocalDateTimeAdapter() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public XMLGregorianCalendar marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return null;
        }

        int offset = 0;
        return datatypeFactory.newXMLGregorianCalendar(v.getYear(), v.getMonthValue(), v.getDayOfMonth(), v.getHour(), v.getMinute(), v.getSecond(),
                v.getNano() / 1000 / 1000, offset);
    }

    @Override
    public LocalDateTime unmarshal(XMLGregorianCalendar v) throws Exception {
        if (v == null) {
            return null;
        }

        int millisecond = v.getMillisecond();
        if (Integer.MIN_VALUE == millisecond) {
            millisecond = 0;
        }

        return LocalDateTime.of(v.getYear(), v.getMonth(), v.getDay(), v.getHour(), v.getMinute(), v.getSecond(), millisecond * 1000 * 1000);
    }
}
