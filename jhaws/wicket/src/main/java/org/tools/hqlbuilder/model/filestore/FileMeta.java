package org.tools.hqlbuilder.model.filestore;

public class FileMeta {
    public String name;

    public long size;

    public String url;

    public String thumbnailUrl;

    public String deleteUrl;

    public String deleteType = "DELETE";

    public String getDeleteType() {
        return deleteType;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
