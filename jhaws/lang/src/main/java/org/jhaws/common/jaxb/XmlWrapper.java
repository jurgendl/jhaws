package org.jhaws.common.jaxb;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jhaws.common.jaxb.adapters.ArrayAdapter;
import org.jhaws.common.jaxb.adapters.CollectionAdapter;
import org.jhaws.common.jaxb.adapters.DateAdapter;
import org.jhaws.common.jaxb.adapters.DateFormatAdapter;
import org.jhaws.common.jaxb.adapters.EnumAdapter;
import org.jhaws.common.jaxb.adapters.JodaDateTimeAdapter;
import org.jhaws.common.jaxb.adapters.JodaLocalDateAdapter;
import org.jhaws.common.jaxb.adapters.JodaLocalDateTimeAdapter;
import org.jhaws.common.jaxb.adapters.JodaLocalTimeAdapter;
import org.jhaws.common.jaxb.adapters.LocaleAdapter;
import org.jhaws.common.jaxb.adapters.MapAdapter;
import org.jhaws.common.jaxb.adapters.NumberFormatAdapter;
import org.jhaws.common.jaxb.adapters.SqlDateAdapter;
import org.jhaws.common.jaxb.adapters.TimeAdapter;
import org.jhaws.common.jaxb.adapters.TimestampAdapter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

@XmlRootElement(name = "any")
public class XmlWrapper<T> implements Serializable {
    private static final long serialVersionUID = 3422118670925672560L;

    @XmlAttribute(name = "type", required = false)
    private String valueType;

    @XmlAttribute(name = "string", required = false)
    private String string;

    @XmlElement(name = "value", required = false)
    private Object simpleValue;

    @XmlElement(name = "locale", required = false)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    private Locale locale;

    @XmlElement(name = "dform", required = false)
    @XmlJavaTypeAdapter(DateFormatAdapter.class)
    private DateFormat dateFormat;

    @XmlElement(name = "date", required = false)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date date;

    @XmlElement(name = "time", required = false)
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time time;

    @XmlElement(name = "timestamp", required = false)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp timestamp;

    @XmlElement(name = "sqldate", required = false)
    @XmlJavaTypeAdapter(SqlDateAdapter.class)
    private java.sql.Date sqldate;

    @XmlElement(name = "nformat", required = false)
    @XmlJavaTypeAdapter(NumberFormatAdapter.class)
    private NumberFormat numberFormat;

    @XmlElement(name = "col", required = false)
    @XmlJavaTypeAdapter(CollectionAdapter.class)
    private Collection<Object> collection;

    @XmlElement(name = "map", required = false)
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<Object, Object> map;

    @XmlElement(name = "array", required = false)
    @XmlJavaTypeAdapter(ArrayAdapter.class)
    private Object[] array;

    @XmlElement(name = "bytes", required = false)
    private byte[] bytes;

    @XmlElement(name = "jldate", required = false)
    @XmlJavaTypeAdapter(JodaLocalDateAdapter.class)
    private LocalDate localDate;

    @XmlElement(name = "jltime", required = false)
    @XmlJavaTypeAdapter(JodaLocalTimeAdapter.class)
    private LocalTime localTime;

    @XmlElement(name = "jldatetime", required = false)
    @XmlJavaTypeAdapter(JodaLocalDateTimeAdapter.class)
    private LocalDateTime localDateTime;

    @XmlElement(name = "jdatetime", required = false)
    @XmlJavaTypeAdapter(JodaDateTimeAdapter.class)
    private DateTime dateTime;

    @SuppressWarnings("rawtypes")
    @XmlElement(name = "enum", required = false)
    @XmlJavaTypeAdapter(EnumAdapter.class)
    private Enum enumValue;

    private transient Object transientValue;

    public XmlWrapper() {
        super();
    }

    public XmlWrapper(T value) {
        setValue(value);
    }

    @SuppressWarnings("unchecked")
    public <C> C get() {
        return (C) getValue();
    }

    @SuppressWarnings("unchecked")
    @XmlTransient
    public T getValue() {
        if (transientValue == null) {
            if ("Collection".equals(valueType)) {
                transientValue = (T) collection;
            } else if ("Map".equals(valueType)) {
                transientValue = (T) map;
            } else if ("Array".equals(valueType)) {
                transientValue = (T) array;
            } else if ("Bytes".equals(valueType)) {
                transientValue = (T) bytes;
            } else if (string != null) {
                transientValue = string;
            } else if (timestamp != null) {
                transientValue = timestamp;
            } else if (sqldate != null) {
                transientValue = sqldate;
            } else if (time != null) {
                transientValue = time;
            } else if (date != null) {
                transientValue = date;
            } else if (dateFormat != null) {
                transientValue = dateFormat;
            } else if (numberFormat != null) {
                transientValue = numberFormat;
            } else if (locale != null) {
                transientValue = locale;
            } else if (localDateTime != null) {
                transientValue = localDateTime;
            } else if (dateTime != null) {
                transientValue = dateTime;
            } else if (localDate != null) {
                transientValue = localDate;
            } else if (localTime != null) {
                transientValue = localTime;
            } else if (enumValue != null) {
                transientValue = enumValue;
            } else {
                transientValue = simpleValue;
            }
        }

        return (T) transientValue;
    }

    @SuppressWarnings("unchecked")
    public void setValue(T value) {
        transientValue = value;

        valueType = null;

        if (value == null) {
            simpleValue = null;
        } else if (value instanceof Enum) {
            enumValue = (Enum<?>) value;
        } else if (value instanceof String) {
            string = (String) value;
        } else if (value instanceof Timestamp) {
            timestamp = (Timestamp) value;
        } else if (value instanceof java.sql.Date) {
            sqldate = (java.sql.Date) value;
        } else if (value instanceof Time) {
            time = (Time) value;
        } else if (value instanceof Date) {
            date = (Date) value;
        } else if (value instanceof DateFormat) {
            dateFormat = (DateFormat) value;
        } else if (value instanceof NumberFormat) {
            numberFormat = (NumberFormat) value;
        } else if (value instanceof Locale) {
            locale = (Locale) value;
        } else if (value instanceof LocalDate) {
            localDate = (LocalDate) value;
        } else if (value instanceof LocalTime) {
            localTime = (LocalTime) value;
        } else if (value instanceof LocalDateTime) {
            localDateTime = (LocalDateTime) value;
        } else if (value instanceof DateTime) {
            dateTime = (DateTime) value;
        } else if (value instanceof Collection) {
            valueType = "Collection";
            collection = new ArrayList<>((Collection<Object>) value);
        } else if (value instanceof byte[]) {
            valueType = "Bytes";
            bytes = (byte[]) value;
        } else if (value.getClass().isArray()) {
            valueType = "Array";
            array = (Object[]) value;
        } else if (value instanceof Map) {
            valueType = "Map";
            map = (Map<Object, Object>) value;
        } else {
            simpleValue = value;
        }
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getValue() == null ? null : getValue().toString();
    }
}
