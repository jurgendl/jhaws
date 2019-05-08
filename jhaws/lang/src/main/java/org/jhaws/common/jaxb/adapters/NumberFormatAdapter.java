package org.jhaws.common.jaxb.adapters;

import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.lang.ObjectWrapper;

public class NumberFormatAdapter extends XmlAdapter<String, NumberFormat> {

    @Override
    public NumberFormat unmarshal(String v) throws Exception {
        if (StringUtils.isBlank(v)) {
            return null;
        }

        String[] parts = v.split("_");
        return NumberFormat.getInstance(new Locale(parts[0], parts[1]));
    }

    @Override
    public String marshal(NumberFormat v) throws Exception {
        if (v == null) {
            return null;
        }

        return new ObjectWrapper(v).get("symbols.locale", Locale.class).toString();
    }

}
