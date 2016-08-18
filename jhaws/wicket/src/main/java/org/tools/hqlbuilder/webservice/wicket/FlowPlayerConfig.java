package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;

import org.jhaws.common.io.FilePath;

public class FlowPlayerConfig implements Serializable {
	private static final long serialVersionUID = -3939672788736407899L;
	private FilePath file;
	private String url;
	private boolean splash;
	private FilePath splashFile;
	private String splashUrl;
	private Integer w;
	private Integer h;
	private String mimetype;
	private boolean loop;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getSplash() {
		return splash;
	}

	public void setSplash(boolean splash) {
		this.splash = splash;
	}

	public FilePath getSplashFile() {
		return splashFile;
	}

	public void setSplashFile(FilePath splashFile) {
		this.splashFile = splashFile;
	}

	public FilePath getFile() {
		return file;
	}

	public void setFile(FilePath file) {
		this.file = file;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getSplashUrl() {
		return splashUrl;
	}

	public void setSplashUrl(String splashUrl) {
		this.splashUrl = splashUrl;
	}

	public boolean getLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
}
