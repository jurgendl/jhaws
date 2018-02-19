package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EnumAdapter extends XmlAdapter<String, Enum> {
    @Override
    public Enum unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(v.substring(0, v.lastIndexOf('.')));
        String enumValue = v.substring(v.lastIndexOf('.') + 1);
        return Enum.valueOf(enumClass, enumValue);
    }

    @Override
    public String marshal(Enum v) throws Exception {
        return v == null ? null : v.getClass().getName() + "." + v.name();
    }
}
