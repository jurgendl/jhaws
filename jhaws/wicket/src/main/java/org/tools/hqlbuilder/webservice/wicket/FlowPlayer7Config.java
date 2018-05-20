package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;
import java.util.List;

import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class FlowPlayer7Config implements Serializable {
	public static class FlowPlayer7Video implements Serializable {
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

	private List<FlowPlayer7Video> sources;

	private Boolean minimal;

	private boolean splash;

	private FilePath splashFile;

	private String splashUrl;

	private Integer w;

	private Integer h;

	private boolean loop;

	public List<FlowPlayer7Video> getSources() {
		return this.sources;
	}

	public void setSources(List<FlowPlayer7Video> sources) {
		this.sources = sources;
	}

	public Boolean getMinimal() {
		return this.minimal;
	}

	public void setMinimal(Boolean minimal) {
		this.minimal = minimal;
	}

	public boolean isSplash() {
		return this.splash;
	}

	public void setSplash(boolean splash) {
		this.splash = splash;
	}

	public FilePath getSplashFile() {
		return this.splashFile;
	}

	public void setSplashFile(FilePath splashFile) {
		this.splashFile = splashFile;
	}

	public String getSplashUrl() {
		return this.splashUrl;
	}

	public void setSplashUrl(String splashUrl) {
		this.splashUrl = splashUrl;
	}

	public Integer getW() {
		return this.w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return this.h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public boolean isLoop() {
		return this.loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
}
