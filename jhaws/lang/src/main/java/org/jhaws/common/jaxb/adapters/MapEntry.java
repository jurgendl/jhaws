package org.jhaws.common.jaxb.adapters;

import javax.xml.bind.annotation.XmlElement;

import org.jhaws.common.jaxb.XmlWrapper;

public class MapEntry {
    @XmlElement(name = "key", required = false)
    XmlWrapper<?> key;

    @XmlElement(name = "val", required = false)
    XmlWrapper<?> value;

    private MapEntry() {
        super();
    }

    public MapEntry(Object key, Object val) {
        this.key = new XmlWrapper<>(key);
        this.value = new XmlWrapper<>(val);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
