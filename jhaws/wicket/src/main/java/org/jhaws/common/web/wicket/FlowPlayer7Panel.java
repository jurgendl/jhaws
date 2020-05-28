package org.jhaws.common.web.wicket;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.jhaws.common.web.wicket.flowplayer.FlowPlayer7;

@SuppressWarnings("serial")
public class FlowPlayer7Panel extends Panel {
    @SuppressWarnings("unused")
    private FlowPlayer7Config config;

    public FlowPlayer7Panel(String id, FlowPlayer7Config config) {
        super(id);
        this.config = config;
        add(createContainer(config));
    }

    protected WebMarkupContainer createContainer(FlowPlayer7Config _config) {
        WebMarkupContainer flowplayercontainer = new WebMarkupContainer("flowplayercontainer");
        flowplayercontainer.add(createPlayer(_config));
        flowplayercontainer.add(createActions(_config));
        return flowplayercontainer;
    }

    protected WebMarkupContainer createPlayer(FlowPlayer7Config _config) {
        boolean size = _config.getW() != null && _config.getH() != null && _config.getW() > 0 && _config.getH() > 0;

        WebMarkupContainer flowplayer = new WebMarkupContainer("flowplayer");
        flowplayer.add(new AttributeModifier("data-swf", FlowPlayer7.urlhls()));

        // flowplayer.add(AttributeAppender.append("class","fp-slim-toggle"));
        // flowplayer.add(AttributeAppender.append("class","fp-mute"));
        // flowplayer.add(AttributeAppender.append("class","is-closeable"));
        if (size) {
            flowplayer.add(new AttributeModifier("data-ratio", (float) _config.getH() / _config.getW()));
        }
        // fixed controls
        // flowplayer.add(AttributeAppender.append("class","no-toggle"));
        // angular icons
        // flowplayer.add(AttributeAppender.append("class","fp-edgy"));
        // outline icons
        // flowplayer.add(AttributeAppender.append("class","fp-outlined"));
        // if (Boolean.TRUE.equals(_config.getMinimal())) {
        // flowplayer.add(AttributeAppender.append("class","fp-minimal"));
        // }

        // if (_config.getSplashFile().exists()) {
        // flowplayer.add(new AttributeModifier("poster", _config.getSplashUrl()));
        // }

        if (_config.isSplash()) {
            flowplayer.add(AttributeModifier.append("class", "is-splash"));
            if (_config.getSplashFile().exists()) {
                if (size) {
                    flowplayer.add(new AttributeModifier("style",
                            ";background:url(" + _config.getSplashUrl() + ") no-repeat;background-size:cover;background-position-x:center"));
                } else {
                    flowplayer.add(new AttributeModifier("style",
                            ";background:url(" + _config.getSplashUrl() + ") no-repeat;background-size:cover;background-position-x:center"));
                }
            } else {
                if (size) {} else {
                    flowplayer.add(new AttributeModifier("style", ""));
                }
            }
        } else {
            flowplayer.add(AttributeRemover.remove("class", "is-splash"));
            if (size) {
                flowplayer.add(new AttributeModifier("style", ";background-size:cover;background-position-x:center"));
            } else {
                flowplayer.add(new AttributeModifier("style", ";background-size:cover"));
            }
        }

        WebMarkupContainer videocontainer = new WebMarkupContainer("videocontainer");
        flowplayer.add(videocontainer);

        RepeatingView videosource = new RepeatingView("videosource");
        _config.getSources().stream().forEach(source -> {
            WebMarkupContainer video = new WebMarkupContainer(videosource.newChildId());
            video.add(new AttributeModifier("src", source.getUrl()));
            if (StringUtils.isNotBlank(source.getMimetype())) {
                video.add(new AttributeModifier("type", source.getMimetype()));
            }
            videosource.add(video);
        });
        videocontainer.add(videosource);
        videocontainer.add(new AttributeModifier("preload", "none")); // metadata

        if (Boolean.TRUE.equals(_config.getLoop())) {
            videocontainer.add(new AttributeModifier("loop", "true"));
        }

        if (size) {
            videocontainer.add(new AttributeModifier("width", _config.getW()));
            videocontainer.add(new AttributeModifier("height", _config.getH()));
            videosource.add(new AttributeModifier("width", _config.getW()));
            videosource.add(new AttributeModifier("height", _config.getH()));
        }

        return flowplayer;
    }

    protected WebMarkupContainer createActions(FlowPlayer7Config _config) {
        WebMarkupContainer flowplayeractions = new WebMarkupContainer("flowplayeractions");
        return flowplayeractions;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FlowPlayer7.SKIN_CSS));
        response.render(CssHeaderItem.forCSS(".flowplayer .fp-header .fp-share { display: none; }", "flowplayer_override"));
        response.render(JavaScriptHeaderItem.forReference(FlowPlayer7.JS_HLS));
        response.render(OnDomReadyHeaderItem.forScript(";$('.customflowplayer').flowplayer(" + //
                "{" + //
                "}" + //
                ");"));
    }
}
