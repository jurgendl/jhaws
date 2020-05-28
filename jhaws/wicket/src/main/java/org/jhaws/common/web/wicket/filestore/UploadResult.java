package org.jhaws.common.web.wicket.filestore;

import java.util.ArrayList;
import java.util.List;

public class UploadResult {
    public List<FileMeta> files = new ArrayList<>();

    public UploadResult() {
        super();
    }

    public UploadResult(List<FileMeta> files) {
        this.files = files;
    }

    public List<FileMeta> getFiles() {
        return files;
    }

    public void setFiles(List<FileMeta> files) {
        this.files = files;
    }
}
