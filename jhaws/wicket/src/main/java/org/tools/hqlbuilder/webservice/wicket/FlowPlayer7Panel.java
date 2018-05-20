package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.tools.hqlbuilder.webservice.jquery.ui.flowplayer.FlowPlayer7;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

@SuppressWarnings("serial")
public class FlowPlayer7Panel extends Panel {
	private FlowPlayer7Config config;

	public FlowPlayer7Panel(String id, FlowPlayer7Config config) {
		super(id);
		this.config = config;
		add(createFlowplayercontainer(config));
	}

	protected WebMarkupContainer createFlowplayercontainer(FlowPlayer7Config _config) {
		WebMarkupContainer flowplayercontainer = new WebMarkupContainer("flowplayercontainer");
		flowplayercontainer.add(createFlowplayer(_config));
		flowplayercontainer.add(createFlowplayerActions(_config));
		return flowplayercontainer;
	}

	protected WebMarkupContainer createFlowplayer(FlowPlayer7Config _config) {
		WebMarkupContainer flowplayer = new WebMarkupContainer("flowplayer");
		flowplayer.add(new AttributeModifier("data-swf", FlowPlayer7.urlhls()));
		flowplayer.add(new CssClassNameAppender("fp-slim-toggle"));
		flowplayer.add(new CssClassNameAppender("fp-mute"));
		if (_config.getW() != null && _config.getH() != null) {
			flowplayer.add(new AttributeModifier("data-ratio", (float) (int) _config.getH() / _config.getW()));
		}
		// fixed controls
		// flowplayer.add(new CssClassNameAppender("no-toggle"));
		// angular icons
		// flowplayer.add(new CssClassNameAppender("fp-edgy"));
		// outline icons
		// flowplayer.add(new CssClassNameAppender("fp-outlined"));
		if (Boolean.TRUE.equals(_config.getMinimal())) {
			flowplayer.add(new CssClassNameAppender("fp-minimal"));
		}

		// if (_config.isSplash()) {
		// flowplayer.add(new CssClassNameAppender("is-splash"));
		// if (_config.getSplashFile().exists()) {
		// if (_config.getW() != 0 && _config.getH() != 0) {
		// flowplayer.add(new AttributeModifier("style", ";background:url(" +
		// _config.getSplashUrl()
		// + ") no-repeat;background-size:cover;background-position-x:center"
		// // + ";max-width:" + _config.getW() + "px"
		// // + ";max-height:" + _config.getH() + "px"
		// ));
		// } else {
		// flowplayer.add(new AttributeModifier("style", ";background:url(" +
		// _config.getSplashUrl()
		// + ") no-repeat;background-size:cover;background-position-x:center"));
		// }
		// } else {
		// if (_config.getW() != 0 && _config.getH() != 0) {
		// } else {
		// flowplayer.add(new AttributeModifier("style", ""));
		// }
		// }
		// } else {
		// flowplayer.add(new CssClassNameRemover("is-splash"));
		// if (_config.getW() != null && _config.getH() != null && _config.getW() != 0
		// && _config.getH() != 0) {
		// flowplayer.add(new AttributeModifier("style",
		// ";background-size:cover;background-position-x:center"));
		// } else {
		// flowplayer.add(new AttributeModifier("style", ";background-size:cover"));
		// }
		// }

		WebMarkupContainer videocontainer = new WebMarkupContainer("videocontainer");
		flowplayer.add(videocontainer);

		RepeatingView videosource = new RepeatingView("videosource");
		_config.getSources().stream().forEach(source -> {
			WebMarkupContainer video = new WebMarkupContainer(videosource.newChildId());
			video.add(new AttributeModifier("type", source.getMimetype()));
			video.add(new AttributeModifier("src", source.getUrl()));
			videosource.add(video);
		});
		videocontainer.add(videosource);
		videocontainer.add(new AttributeModifier("preload", "none")); // metadata

		if (_config.getW() != null && _config.getH() != null && _config.getW() != 0 && _config.getH() != 0) {
			videocontainer.add(new AttributeModifier("width", _config.getW()));
			videocontainer.add(new AttributeModifier("height", _config.getH()));
			videosource.add(new AttributeModifier("width", _config.getW()));
			videosource.add(new AttributeModifier("height", _config.getH()));
		}

		return flowplayer;
	}

	protected WebMarkupContainer createFlowplayerActions(FlowPlayer7Config _config) {
		WebMarkupContainer flowplayeractions = new WebMarkupContainer("flowplayeractions");
		return flowplayeractions;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(FlowPlayer7.SKIN_CSS));
		response.render(JavaScriptHeaderItem.forReference(FlowPlayer7.JS));
		response.render(OnDomReadyHeaderItem.forScript(";$('.customflowplayer').flowplayer(" + //
				"{" + //
				// (config.isSplash() ? "," + "splash:true" : "") + //
				// (config.getLoop() ? "," + "loop:true" : "") + //
				"}" + //
				");"));
		// if (config.getLoop()) {
		// response.render(CssHeaderItem.forCSS(
		// ".is-splash.flowplayer .fp-ui, .is-paused.flowplayer .fp-ui { background:
		// none !important; }",
		// "flowplayer_hide_play"));
		// }
	}
}
