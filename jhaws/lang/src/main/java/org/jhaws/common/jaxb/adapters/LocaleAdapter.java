package org.jhaws.common.jaxb.adapters;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

public class LocaleAdapter extends XmlAdapter<String, Locale> {
    @Override
    public Locale unmarshal(String v) throws Exception {
        if (StringUtils.isBlank(v)) {
            return null;
        }
        String[] parts = v.split("_");
        return new Locale(parts[0], parts.length > 1 ? parts[1] : "");
    }

    @Override
    public String marshal(Locale v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }
}
