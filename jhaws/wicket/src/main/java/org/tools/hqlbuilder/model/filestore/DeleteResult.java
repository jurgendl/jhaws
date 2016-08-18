package org.tools.hqlbuilder.model.filestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteResult {
	public List<Map<String, Boolean>> files = new ArrayList<>();

	public List<Map<String, Boolean>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, Boolean>> files) {
		this.files = files;
	}
}
