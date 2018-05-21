package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.jquery.ui.flowplayer.FlowPlayer;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings("serial")
public class FlowPlayerPanel extends Panel {
	private FlowPlayerConfig config;

	public FlowPlayerPanel(String id, FlowPlayerConfig config) {
		super(id);
		this.config = config;
		add(createContainer(config));
	}

	protected WebMarkupContainer createContainer(FlowPlayerConfig _config) {
		WebMarkupContainer flowplayercontainer = new WebMarkupContainer("flowplayercontainer");
		flowplayercontainer.add(createPlayer(_config));
		flowplayercontainer.add(createActions(_config));
		return flowplayercontainer;
	}

	protected WebMarkupContainer createPlayer(FlowPlayerConfig _config) {
		WebMarkupContainer flowplayer = new WebMarkupContainer("flowplayer");

		if (_config.getSplash()) {
			flowplayer.add(new CssClassNameAppender("is-splash"));
			if (_config.getSplashFile().exists()) {
				if (_config.getW() != 0 && _config.getH() != 0) {
					flowplayer.add(new AttributeModifier("style", ";background:url(" + _config.getSplashUrl()
							+ ") no-repeat;background-size:cover;background-position-x:center"
					// + ";max-width:" + _config.getW() + "px"
					// + ";max-height:" + _config.getH() + "px"
					));
				} else {
					flowplayer.add(new AttributeModifier("style", ";background:url(" + _config.getSplashUrl()
							+ ") no-repeat;background-size:cover;background-position-x:center"));
				}
			} else {
				if (_config.getW() != 0 && _config.getH() != 0) {
					// flowplayer.add(new AttributeModifier("style",
					// ";max-width:" + _config.getW() + "px"
					// + ";max-height:" + _config.getH() + "px"
					// ));
				} else {
					flowplayer.add(new AttributeModifier("style", ""));
				}
			}
		} else {
			flowplayer.add(new CssClassNameRemover("is-splash"));
			if (_config.getW() != null && _config.getH() != null && _config.getW() != 0 && _config.getH() != 0) {
				flowplayer.add(new AttributeModifier("style", ";background-size:cover;background-position-x:center"
				// + ";max-width:" + _config.getW() + "px"
				// + ";max-height:" + _config.getH() + "px"
				));
			} else {
				flowplayer.add(new AttributeModifier("style", ";background-size:cover"));
			}
		}

		WebMarkupContainer videocontainer = new WebMarkupContainer("videocontainer");
		flowplayer.add(videocontainer);

		WebMarkupContainer videosource = new WebMarkupContainer("videosource");
		videocontainer.add(videosource);

		if (_config.getW() != null && _config.getH() != null && _config.getW() != 0 && _config.getH() != 0) {
			videocontainer.add(new AttributeModifier("width", _config.getW()));
			videocontainer.add(new AttributeModifier("height", _config.getH()));
			videosource.add(new AttributeModifier("width", _config.getW()));
			videosource.add(new AttributeModifier("height", _config.getH()));
		}

		videosource.add(new AttributeModifier("type", _config.getMimetype()));
		videocontainer.add(new AttributeModifier("preload", "none")); // metadata
		videosource.add(new AttributeModifier("src", _config.getUrl()));

		return flowplayer;
	}

	protected WebMarkupContainer createActions(FlowPlayerConfig _config) {
		WebMarkupContainer flowplayeractions = new WebMarkupContainer("flowplayeractions");
		return flowplayeractions;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		// response.render(CssHeaderItem.forCSS(
		// ". a[href=\"http://flowplayer.org\"] { display: none !important; } ",
		// "fphl"));
		response.render(CssHeaderItem.forReference(FlowPlayer.SKIN_CSS));
		response.render(JavaScriptHeaderItem.forReference(FlowPlayer.JS));
		response.render(OnDomReadyHeaderItem.forScript(";$('.customflowplayer').flowplayer(" + //
				"{"//
				+ "swf:'" + FlowPlayer.url() + "'" + //
				(config.getSplash() ? "," + "splash:true" : "") + //
				(config.getLoop() ? "," + "loop:true" : "") + //
				"}" + //
				");"));
		if (config.getLoop()) {
			response.render(CssHeaderItem.forCSS(
					".is-splash.flowplayer .fp-ui, .is-paused.flowplayer .fp-ui { background: none !important; }",
					"flowplayer_hide_play"));
		}
	}
}
