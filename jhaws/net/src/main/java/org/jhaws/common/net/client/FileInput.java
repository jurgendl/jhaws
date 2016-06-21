package org.jhaws.common.net.client;

import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.jhaws.common.io.FilePath;

public class FileInput extends Input {
    private static final long serialVersionUID = -5620942678431367341L;

    public FileInput(TagNode inputnode) {
        super(inputnode);
    }

    public FileInput(String id_name) {
        this(id_name, id_name);
    }

    public FileInput(String id, String name) {
        super(InputType.file, id, name);
    }

    public FilePath getFile() {
        return StringUtils.isBlank(this.getValue()) ? null : new FilePath(this.getValue());
    }

    public void setFile(FilePath file) {
        setValue(file == null ? null : file.getAbsolutePath());
    }
}
