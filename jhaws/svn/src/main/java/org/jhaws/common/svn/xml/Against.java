package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Against {
    @XmlAttribute
    private int revision;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return "Against [revision=" + revision + "]";
    }
}
