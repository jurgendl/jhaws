package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;

import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class Html5VideoElement implements Serializable {
	private String url;

	private String mimetype;

	private FilePath file;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMimetype() {
		return this.mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public FilePath getFile() {
		return this.file;
	}

	public void setFile(FilePath file) {
		this.file = file;
	}
}
