package org.jhaws.common.jaxb.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jhaws.common.lang.ObjectWrapper;

public class DateFormatAdapter extends XmlAdapter<String[], DateFormat> {

    @Override
    public DateFormat unmarshal(String[] v) throws Exception {
        if (v == null || v.length == 0) {
            return null;
        }
        String pattern = v[0];
        String[] parts = v[1].split("_");
        Locale locale = new Locale(parts[0], parts[1]);

        return new SimpleDateFormat(pattern, locale);
    }

    @Override
    public String[] marshal(DateFormat df) throws Exception {
        if (df == null) {
            return null;
        }

        ObjectWrapper ow = new ObjectWrapper(df);
        String pattern = ow.get("pattern", String.class);
        Locale locale = ow.get("locale", Locale.class);

        return new String[] { pattern, locale.toString() };
    }
}
