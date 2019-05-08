package org.tools.hqlbuilder.webservice.wicket.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "web-settings")
public class WebSettings implements Serializable {
    @XmlAttribute
    private String id;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property", required = false)
    private List<SettingsProperty<?>> properties;

    @XmlElementWrapper(name = "childSettings")
    @XmlElement(name = "childSetting", required = false)
    private List<WebSettings> childSettings;

    public WebSettings() {
        super();
    }

    public WebSettings(String id) {
        this();
        this.id = id;
    }

    public List<SettingsProperty<?>> getProperties() {
        if (properties == null) properties = new ArrayList<>();
        return this.properties;
    }

    public void setProperties(List<SettingsProperty<?>> properties) {
        this.properties = properties;
    }

    public List<WebSettings> getChildSettings() {
        if (childSettings == null) childSettings = new ArrayList<>();
        return this.childSettings;
    }

    public void setChildSettings(List<WebSettings> childSettings) {
        this.childSettings = childSettings;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WebSettings get(String id) {
        if (childSettings == null || id == null) return null;
        return childSettings.stream().filter(c -> id.equals(c.getId())).findAny().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> SettingsProperty<T> property(String id) {
        if (properties == null || id == null) return null;
        return (SettingsProperty<T>) properties.stream().filter(c -> id.equals(c.getName())).findAny().orElse(null);
    }
}
