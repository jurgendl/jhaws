package org.jhaws.common.web.wicket;

import java.io.Serializable;
import java.util.List;

import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class Html5VideoPanelConfig implements Serializable {
	private List<Html5VideoElement> sources;

	private boolean splash;

	private FilePath splashFile;

	private String splashUrl;

	private Integer w;

	private Integer h;

	private Boolean loop;

	private Boolean mute = Boolean.TRUE;

	public List<Html5VideoElement> getSources() {
		return this.sources;
	}

	public void setSources(List<Html5VideoElement> sources) {
		this.sources = sources;
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

	public Boolean getLoop() {
		return this.loop;
	}

	public void setLoop(Boolean loop) {
		this.loop = loop;
	}

	public Boolean getMute() {
		return this.mute;
	}

	public void setMute(Boolean mute) {
		this.mute = mute;
	}
}
