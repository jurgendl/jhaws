package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlElement;

public class Info {
    @XmlElement(name = "wcroot-abspath")
    private String wcrootAbspath;

    private String schedule;

    private String depth;

    public String getDepth() {
        return depth;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getWcrootAbspath() {
        return wcrootAbspath;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setWcrootAbspath(String wcrootAbspath) {
        this.wcrootAbspath = wcrootAbspath;
    }

    @Override
    public String toString() {
        return "Info [" + (wcrootAbspath != null ? "wcrootAbspath=" + wcrootAbspath + ", " : "")
                + (schedule != null ? "schedule=" + schedule + ", " : "") + (depth != null ? "depth=" + depth : "") + "]";
    }
}
