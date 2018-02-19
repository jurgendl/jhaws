package org.jhaws.common.svn.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

public class DateAdapter extends XmlAdapter<String, Date> {
    private static final DateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        }

        return DateAdapter.XML_DATE_FORMAT.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        if (StringUtils.isBlank(v)) {
            return null;
        }

        return DateAdapter.XML_DATE_FORMAT.parse(v);
    }
}