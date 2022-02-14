package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Entry {
    @XmlAttribute
    private String path;

    @XmlAttribute
    private int revision;

    @XmlAttribute
    private String kind;

    private String url;

    @XmlElement(name = "relative-url")
    private String relativeUrl;

    private Repository repository;

    private String name;

    @XmlElement(name = "wc-info")
    private Info wcInfo;

    @XmlElement(name = "wc-status")
    private Status wcStatus;

    @XmlElement(name = "repos-status")
    private Status reposStatus;

    private Commit commit;

    public Commit getCommit() {
        return commit;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public Repository getRepository() {
        return repository;
    }

    public Status getReposStatus() {
        return reposStatus;
    }

    public int getRevision() {
        return revision;
    }

    public String getUrl() {
        return url;
    }

    public Info getWcInfo() {
        return wcInfo;
    }

    public Status getWcStatus() {
        return wcStatus;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setReposStatus(Status reposStatus) {
        this.reposStatus = reposStatus;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWcInfo(Info wcInfo) {
        this.wcInfo = wcInfo;
    }

    public void setWcStatus(Status wcStatus) {
        this.wcStatus = wcStatus;
    }

    @Override
    public String toString() {
        return "Entry [" + (path != null ? "path=" + path + ", " : "") + "revision=" + revision + ", " + (kind != null ? "kind=" + kind + ", " : "") + (url != null ? "url=" + url + ", " : "") + (relativeUrl != null ? "relativeUrl=" + relativeUrl + ", " : "")
                + (repository != null ? "repository=" + repository + ", " : "") + (name != null ? "name=" + name + ", " : "") + (wcInfo != null ? "wcInfo=" + wcInfo + ", " : "") + (wcStatus != null ? "wcStatus=" + wcStatus + ", " : "")
                + (reposStatus != null ? "reposStatus=" + reposStatus + ", " : "") + (commit != null ? "commit=" + commit : "") + "]";
    }
}
