package org.jhaws.common.net.client.forms;

import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.jhaws.common.io.IOFile;

/**
 * FileInput
 */
public class FileInput extends Input {

    private static final long serialVersionUID = -5620942678431367341L;

    /**
     * Creates a new FileInput object.
     *
     * @param inputnode
     */
    public FileInput(TagNode inputnode) {
        super(inputnode);
    }

    /**
     * get file
     *
     * @return
     */
    public IOFile getFile() {
        return StringUtils.isBlank(this.getValue()) ? null : new IOFile(this.getValue());
    }
}
