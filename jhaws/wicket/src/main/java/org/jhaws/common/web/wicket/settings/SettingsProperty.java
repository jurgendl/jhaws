package org.jhaws.common.web.wicket.settings;

import javax.xml.bind.annotation.XmlAttribute;

import org.jhaws.common.jaxb.XmlWrapper;

@SuppressWarnings("serial")
public class SettingsProperty<T> extends XmlWrapper<T> {
    @XmlAttribute
    protected String name;

    @XmlAttribute
    protected String type;

    public SettingsProperty() {
        super();
    }

    public SettingsProperty(String name, T value) {
        this(name, value.getClass().getName(), value);
    }

    public SettingsProperty(String name, String type, T value) {
        super(value);
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + "=" + super.toString();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
