package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.libraries.videojs.VideoJs7;

@SuppressWarnings("serial")
public class VideoJs7Panel extends Panel {
	public static class VideoJs7PanelConfig implements Serializable {
		private List<Html5VideoElement> sources;

		private boolean splash;

		private FilePath splashFile;

		private String splashUrl;

		private Integer w;

		private Integer h;

		public List<Html5VideoElement> getSources() {
			return this.sources;
		}

		public void setSources(List<Html5VideoElement> sources) {
			this.sources = sources;
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
	}

	@SuppressWarnings("unused")
	private VideoJs7PanelConfig config;

	public VideoJs7Panel(String id, VideoJs7PanelConfig config) {
		super(id);
		this.config = config;
		add(createContainer(config));
	}

	protected WebMarkupContainer createContainer(VideoJs7PanelConfig _config) {
		WebMarkupContainer container = new WebMarkupContainer("container");
		container.add(createPlayer(_config));
		container.add(createActions(_config));
		return container;
	}

	protected WebMarkupContainer createPlayer(VideoJs7PanelConfig _config) {
		boolean size = _config.getW() != null && _config.getH() != null && _config.getW() > 0 && _config.getH() > 0;

		WebMarkupContainer videocontainer = new WebMarkupContainer("videocontainer");

		WebMarkupContainer video = new WebMarkupContainer("video");
		videocontainer.add(video);

		RepeatingView sourceRepeater = new RepeatingView("source");
		video.add(sourceRepeater);
		_config.getSources().forEach(_source -> {
			WebMarkupContainer source = new WebMarkupContainer(sourceRepeater.newChildId());
			sourceRepeater.add(source);
			source.add(new AttributeModifier("src", _source.getUrl()));
			if (StringUtils.isNotBlank(_source.getMimetype())) {
				source.add(new AttributeModifier("type", _source.getMimetype()));
			}
		});
		video.add(new AttributeModifier("preload", "none")); // metadata
		if (StringUtils.isNotBlank(_config.getSplashUrl())) {
			video.add(new AttributeModifier("poster", _config.getSplashUrl()));
		}
		video.add(new AttributeModifier("loop", "true"));
		if (size) {
			video.add(new AttributeModifier("aspectRatio", _config.getW() + ":" + _config.getH()));
		}
		video.add(new AttributeModifier("fluid", "true"));

		return videocontainer;
	}

	protected WebMarkupContainer createActions(VideoJs7PanelConfig _config) {
		WebMarkupContainer actions = new WebMarkupContainer("actions");
		return actions;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(VideoJs7.CSS));
		response.render(JavaScriptHeaderItem.forReference(VideoJs7.JS));
	}
}
