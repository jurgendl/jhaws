package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class Target implements Iterable<Entry> {
    @XmlAttribute
    private String path;

    private List<Entry> entry;

    private Against against;

    public Against getAgainst() {
        return against;
    }

    @SuppressWarnings("unchecked")
    public List<Entry> getEntry() {
        if (entry == null) {
            return Collections.emptyList();
        }
        return entry;
    }

    public String getPath() {
        return path;
    }

    public void setAgainst(Against against) {
        this.against = against;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Iterator<Entry> iterator() {
        return getEntry().iterator();
    }

    @Override
    public String toString() {
        return "Target [" + (path != null ? "path=" + path + ", " : "") + (against != null ? "against=" + against : "") + "]";
    }
}
