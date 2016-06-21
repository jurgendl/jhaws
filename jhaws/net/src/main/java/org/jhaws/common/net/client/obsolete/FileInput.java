package org.jhaws.common.net.client.obsolete;

import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.jhaws.common.io.IOFile;

@SuppressWarnings("deprecation")
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

	public IOFile getFile() {
		return StringUtils.isBlank(this.getValue()) ? null : new IOFile(this.getValue());
	}

	public void setFile(IOFile file) {
		setValue(file == null ? null : file.getAbsolutePath());
	}
}
