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

public class FlowPlayerPanel extends Panel {
	private static final long serialVersionUID = -1439746022676240245L;

	private FlowPlayerConfig config;

	public FlowPlayerPanel(String id, FlowPlayerConfig config) {
		super(id);
		this.config = config;
		add(createFlowplayercontainer(config));
	}

	protected WebMarkupContainer createFlowplayercontainer(FlowPlayerConfig config) {
		WebMarkupContainer flowplayercontainer = new WebMarkupContainer("flowplayercontainer");
		flowplayercontainer.add(createFlowplayer(config));
		flowplayercontainer.add(createFlowplayerActions(config));
		return flowplayercontainer;
	}

	protected WebMarkupContainer createFlowplayer(FlowPlayerConfig config) {
		WebMarkupContainer flowplayer = new WebMarkupContainer("flowplayer");

		if (config.getSplash()) {
			flowplayer.add(new CssClassNameAppender("is-splash"));
			if (config.getSplashFile().exists()) {
				if (config.getW() != 0 && config.getH() != 0) {
					flowplayer.add(new AttributeModifier("style",
							";background:url(" + config.getSplashUrl()
									+ ") no-repeat;background-size:cover;background-position-x:center;max-width:"
									+ config.getW() + "px;max-height:" + config.getH() + "px"));
				} else {
					flowplayer.add(new AttributeModifier("style", ";background:url(" + config.getSplashUrl()
							+ ") no-repeat;background-size:cover;background-position-x:center"));
				}
			} else {
				if (config.getW() != 0 && config.getH() != 0) {
					flowplayer.add(new AttributeModifier("style",
							";max-width:" + config.getW() + "px;max-height:" + config.getH() + "px"));
				} else {
					flowplayer.add(new AttributeModifier("style", ""));
				}
			}
		} else {
			flowplayer.add(new CssClassNameRemover("is-splash"));
			if (config.getW() != 0 && config.getH() != 0) {
				flowplayer.add(
						new AttributeModifier("style", ";background-size:cover;background-position-x:center;max-width:"
								+ config.getW() + "px;max-height:" + config.getH() + "px"));
			} else {
				flowplayer.add(new AttributeModifier("style", ";background-size:cover"));
			}
		}

		WebMarkupContainer videocontainer = new WebMarkupContainer("videocontainer");
		flowplayer.add(videocontainer);

		WebMarkupContainer videosource = new WebMarkupContainer("videosource");
		videocontainer.add(videosource);

		if (config.getW() != 0 && config.getH() != 0) {
			videocontainer.add(new AttributeModifier("width", config.getW()));
			videocontainer.add(new AttributeModifier("height", config.getH()));
			videosource.add(new AttributeModifier("width", config.getW()));
			videosource.add(new AttributeModifier("height", config.getH()));
		}

		videosource.add(new AttributeModifier("type", config.getMimetype()));
		videocontainer.add(new AttributeModifier("preload", "none")); // metadata
		videosource.add(new AttributeModifier("src", config.getUrl()));

		return flowplayer;
	}

	protected WebMarkupContainer createFlowplayerActions(FlowPlayerConfig config) {
		WebMarkupContainer flowplayeractions = new WebMarkupContainer("flowplayeractions");
		return flowplayeractions;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(FlowPlayer.SKIN_CSS));
		response.render(JavaScriptHeaderItem.forReference(FlowPlayer.JS));
		response.render(OnDomReadyHeaderItem.forScript(";$('.customflowplayer').flowplayer({swf:'" + FlowPlayer.url()
				+ "'"
				+ (config.getSplash() ? ",splash:true" : "") + (config.getLoop() ? ",loop:true" : "") + "});"));
		if (config.getLoop())
			response.render(CssHeaderItem.forCSS(
					".is-splash.flowplayer .fp-ui, .is-paused.flowplayer .fp-ui { background: none !important; }",
					"flowplayer_hide_play"));
	}
}
