package org.jhaws.common.web.wicket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.jhaws.common.web.wicket.videojs.VideoJs7;

import com.fasterxml.jackson.core.JsonProcessingException;

// https://docs.videojs.com/tutorial-embeds.html
// https://docs.videojs.com/
// https://github.com/videojs/video.js/issues/3773
// http://jsbin.com/faxayot/edit?html,output
// https://blog.videojs.com/video-js-5-s-fluid-mode-and-playlist-picker/
// https://codepen.io/dotdreaming/pen/CnLeD
// https://codepen.io/rayrc/post/responsive-html5-video-using-video-js
// http://bgbaden-frauen.ac.at/~infwurst/Hagmann/videojs/VideoJS%20setup%20guide%20to%20scale%20for%20responsive%20design%20on%20all%20browsers%20&%20mobile.html
// https://blog.videojs.com/
@SuppressWarnings("serial")
public class VideoJs7Panel extends Panel {
    protected VideoJs7PanelConfig config;

    protected WebMarkupContainer container;

    protected WebMarkupContainer videocontainer;

    protected WebMarkupContainer video;

    public VideoJs7Panel(String id, VideoJs7PanelConfig config) {
        super(id);
        this.config = config;
        add(createContainer(config));
        setOutputMarkupId(true);
    }

    protected WebMarkupContainer createContainer(VideoJs7PanelConfig _config) {
        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.add(createPlayer(_config));
        container.add(createActions(_config));
        return container;
    }

    protected WebMarkupContainer createPlayer(VideoJs7PanelConfig _config) {
        boolean size = _config.getW() != null && _config.getH() != null && _config.getW() > 0 && _config.getH() > 0;

        videocontainer = new WebMarkupContainer("videocontainer");
        videocontainer.setOutputMarkupId(true);

        video = new WebMarkupContainer("video");
        video.setOutputMarkupId(true);
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
        if (Boolean.TRUE.equals(_config.getLoop())) {
            video.add(new AttributeModifier("loop", "true"));
        }
        if (size) {
            video.add(new AttributeModifier("aspectRatio", _config.getW() + ":" + _config.getH()));
            video.add(new AttributeModifier("width", _config.getW()));
            video.add(new AttributeModifier("height", _config.getH()));
        }
        video.add(new AttributeModifier("fluid", "true"));
        if (Boolean.TRUE.equals(_config.getMute())) {
            video.add(new AttributeModifier("muted", "true"));
        }
        // video.add(new AttributeModifier("data-setup", getSettings()));

        // video.add(new AttributeModifier("fill", "true"));
        // video.add(new AttributeModifier("responsive", "true"));
        // video.add(new AttributeModifier("data-setup",
        // "{fluid:true,fill:true,responsive:true}"));

        // video.add(new AttributeModifier("style", "width:100% !important;
        // padding-bottom:"
        // + (int) (100.0 * config.getH() / _config.getW()) + "% !important"));
        // video.add(new AttributeModifier("style", "width:100%
        // !important;height:100%
        // !important"));

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
        response.render(JavaScriptHeaderItem.forReference(VideoJs7.JS_PREVENT_MULTIPLE));
        String videoWicketId = getVideo().getMarkupId();
        String playerId = "player_" + videoWicketId;
        response.render(OnDomReadyHeaderItem
                .forScript(";var " + playerId + " = videojs('" + videoWicketId + "'," + getSettings() + ");" + playerId + ".muted(" + isVideoDefaultMuted(config, videoWicketId, playerId) + ");" + getPostCreate(config, videoWicketId, playerId) + ";"));
    }

    public String getPostCreate(VideoJs7PanelConfig config, String videoWicketId, String playerId) {
        return "";
    }

    public String isVideoDefaultMuted(VideoJs7PanelConfig config, String videoWicketId, String playerId) {
        return String.valueOf(Boolean.TRUE.equals(config.getMute()));
    }

    protected Map<String, Object> settings = new HashMap<>();

    public void settting(String key, Object value) {
        if (value == null)
            settings.remove(key);
        else
            settings.put(key, value);
    }

    public String getSettings() {
        try {
            String s = WicketApplication.get().getObjectMapper().writeValueAsString(settings);
            return s;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "{}";
        }
    }

    public VideoJs7PanelConfig getConfig() {
        return this.config;
    }

    public WebMarkupContainer getContainer() {
        return this.container;
    }

    public WebMarkupContainer getVideocontainer() {
        return this.videocontainer;
    }

    public WebMarkupContainer getVideo() {
        return this.video;
    }
}
