package org.jhaws.common.io.jaxb.pom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project", namespace = "http://maven.apache.org/POM/4.0.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pom {
    private PomParent parent;

    private PomVersion version;

    public PomParent getParent() {
        return this.parent;
    }

    public void setParent(PomParent parent) {
        this.parent = parent;
    }

    public PomVersion getVersion() {
        return this.version;
    }

    public void setVersion(PomVersion version) {
        this.version = version;
    }
}