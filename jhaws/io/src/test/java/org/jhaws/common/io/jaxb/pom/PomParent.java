package org.jhaws.common.io.jaxb.pom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class PomParent {
    private PomVersion version;

    public PomVersion getVersion() {
        return this.version;
    }

    public void setVersion(PomVersion version) {
        this.version = version;
    }
}