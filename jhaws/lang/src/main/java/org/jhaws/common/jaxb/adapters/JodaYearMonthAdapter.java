package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.YearMonth;

public class JodaYearMonthAdapter extends XmlAdapter<String, YearMonth> {
    public JodaYearMonthAdapter() {
        super();
    }

    @Override
    public String marshal(YearMonth v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.getYear() + ";" + v.getMonthOfYear();
    }

    @Override
    public YearMonth unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        String[] p = v.split(";");
        return new YearMonth(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }
}
