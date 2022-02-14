package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Status {
    @XmlAttribute
    private String item;

    @XmlAttribute
    private String props;

    @XmlAttribute
    private int revision;

    private Commit commit;

    public Commit getCommit() {
        return commit;
    }

    public String getItem() {
        return item;
    }

    public String getProps() {
        return props;
    }

    public int getRevision() {
        return revision;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return "Status [" + (item != null ? "item=" + item + ", " : "") + (props != null ? "props=" + props + ", " : "") + "revision=" + revision + ", " + (commit != null ? "commit=" + commit : "") + "]";
    }
}
