package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

public class SocialPanel extends Panel {
    private static final long serialVersionUID = -298324858422498953L;

    private SocialPanelSettings settings;

    public SocialPanel(String id, IModel<Social> model) {
        this(id, model, null, null, new SocialPanelSettings());
    }

    public SocialPanel(String id, IModel<Social> model, SocialPanelSettings settings) {
        this(id, model, null, null, settings);
    }

    public SocialPanel(String panelId, IModel<Social> model, IModel<String> title, IModel<String> url, SocialPanelSettings settings) {
        super(panelId, model);
        this.settings = settings;

        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);

        Social socialId = model.getObject();

        String titleString;
        if (title == null || StringUtils.isBlank(title.getObject())) {
            titleString = socialId.getName();
        } else {
            titleString = title.getObject();
        }

        String urlString;
        if (url == null || StringUtils.isBlank(url.getObject())) {
            urlString = socialId.getUrl();
        } else {
            urlString = url.getObject();
        }

        WebMarkupContainer socialbutton = new ExternalLink("socialbtn", "");
        socialbutton.setOutputMarkupId(false);
        add(socialbutton);

        if (StringUtils.isNotBlank(titleString)) {
            socialbutton.add(new AttributeAppender("title", titleString));
        }
        if (StringUtils.isNotBlank(urlString)) {
            if (socialbutton instanceof AbstractLink) {
                ExternalLink.class.cast(socialbutton).setDefaultModelObject(urlString);
            } else {
                socialbutton.add(new AttributeAppender("onclick", "location.href=\"" + urlString + "\""));
            }
        }

        switch (settings.getForm()) {
            case bar:
                socialbutton.add(new CssClassNameAppender("socialbar"));
                break;
            case pin:
                socialbutton.add(new CssClassNameAppender("socialpin"));
                break;
            default:
            case button:
                socialbutton.add(new CssClassNameAppender("socialbtn"));
                break;
        }

        socialbutton.add(new CssClassNameAppender(socialId + "-color"));

        WebMarkupContainer icon = new WebMarkupContainer("icon");
        socialbutton.add(icon);
        WebComponent label = new Label("label", titleString);
        label.setEscapeModelStrings(false);
        socialbutton.add(label);
        switch (settings.getForm()) {
            case bar:
                icon.add(new CssClassNameAppender("zocial-" + socialId));
                icon.add(new AttributeAppender("data-text", socialId));
                break;
            case pin:
                socialbutton.add(new CssClassNameAppender("zocial-" + socialId));
                socialbutton.add(new AttributeAppender("data-text", socialId));
                socialbutton.add(icon.setVisible(false));
                socialbutton.add(label.setVisible(false));
                break;
            default:
            case button:
                socialbutton.add(new CssClassNameAppender("zocial-" + socialId));
                socialbutton.add(new AttributeAppender("data-text", socialId));
                socialbutton.add(icon.setVisible(false));
                socialbutton.add(label.setVisible(false));
                break;
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_SOCIAL_CSS));
        response.render(CssHeaderItem.forReference(settings.isFading() ? WeLoveIcons.SOCIAL_COLORS_HOVER_CSS : WeLoveIcons.SOCIAL_COLORS_CSS));
    }

    public static enum Social {
        // generic
        guest(null, "guest"), //
        cal(null, "calendar"), //
        call(null, "call"), //
        cart(null, "cart"), //
        email(null, "e-mail"), //
        podcast(null, "podcast"), //
        print(null, "print"), //
        rss(null, "rss"), //
        // branded
        stripe("http://stripe.com", "Stripe"), //
        vk("http://vk.com", "VK"), //
        smashing("http://www.smashingmagazine.com", "Smashing Magazine"), //
        pocket("http://getpocket.com", "Pocket"), //
        opentable("http://www.opentable.com", "opentable"), //
        ninetyninedesigns("http://99designs.com", "99designs"), //
        html5("http://www.w3.org/TR/html5", "HTML5"), //
        ie("http://www.microsoft.com/windows/ie", "Internet Explorer"), //
        // acrobat("http://www.adobe.com/Acrobat", "Acrobat"), //
        amazon("http://www.amazon.com", "Amazon"), //
        android("http://www.android.com", "Android"), //
        angellist("http://angel.co", "AngelList"), //
        aol("https://www.aim.com", "AIM"), //
        appnet("http://app.net", "App.net"), //
        // appstore("http://http://store.apple.com", "Apple Store"), //
        bitbucket("http://bitbucket.org", "Bitbucket"), //
        bitcoin("http://bitcoin.org", "Bitcoin"), //
        blogger("http://www.blogger.com", "Blogger"), //
        buffer("http://bufferapp.com", "Buffer"), //
        chrome("http://chrome.google.com", "Chrome"), //
        // cloudapp("http://www.apple.com/icloud", "iCloud"), //
        creativecommons("http://creativecommons.org", "Creative Commons"), //
        delicious("http://delicious.com", "Delicious"), //
        digg("http://digg.com", "Digg"), //
        disqus("http://disqus.com", "Disqus"), //
        dribbble("http://dribbble.com", "Dribbble"), //
        dropbox("http://www.dropbox.com", "Dropbox"), //
        drupal("http://www.drupal.org", "Drupal"), //
        dwolla("http://www.dwolla.com", "Dwolla"), //
        eventbrite("http://www.eventbrite.com", "Eventbrite"), //
        eventful("http://eventful.com", "Eventful"), //
        evernote("http://www.evernote.com", "Evernote"), //
        facebook("http://www.facebook.com", "Facebook"), //
        fivehundredpx("http://500px.com", "5&infin;px"), //
        flattr("http://flattr.com", "Flattr"), //
        flickr("http://www.flickr.com", "Flickr"), //
        foursquare("http://foursquare.com", "Foursquare"), //
        github("http://github.com", "GitHub"), //
        gmail("http://mail.google.com", "Gmail"), //
        google("http://www.google.com", "Google"), //
        googleplay("http://play.google.com", "Google Play"), //
        googleplus("http://plus.google.com", "Google+"), //
        gowalla("http://blog.gowalla.com", "Gowalla"), //
        grooveshark("http://grooveshark.com", "Grooveshark"), //
        instagram("http://instagram.com", "Instagram"), //
        instapaper("http://www.instapaper.com", "Instapaper"), //
        intensedebate("http://www.intensedebate.com", "IntenseDebate"), //
        // itunes("http://www.apple.com/itunes", "iTunes‎"), //
        klout("http://klout.com", "Klout"), //
        lanyrd("http://lanyrd.com", "Lanyrd"), //
        lastfm("http://www.last.fm", "Last.fm"), //
        linkedin("http://www.linkedin.com", "LinkedIn"), //
        // macstore("http://www.apple.com/mac", "Apple - Mac"), //
        meetup("http://www.meetup.com", "Meetup"), //
        myspace("http://myspace.com", "Myspace"), //
        openid("http://openid.net", "OpenID"), //
        paypal("http://www.paypal.com", "PayPal"), //
        pinboard("http://pinboard.in", "Pinboard"), //
        pinterest("http://www.pinterest.com", "Pinterest"), //
        plancast("http://plancast.com", "Plancast"), //
        plurk("http://www.plurk.com", "Plurk"), //
        quora("http://www.quora.com", "Quora"), //
        reddit("http://www.reddit.com", "reddit"), //
        scribd("http://www.scribd.com", "Scribd"), //
        skype("http://www.skype.com", "Skype"), //
        songkick("http://www.songkick.com", "Songkick"), //
        soundcloud("http://soundcloud.com", "SoundCloud"), //
        spotify("http://www.spotify.com", "Spotify"), //
        statusnet("http://status.net", "StatusNet"), //
        steam("http://steamcommunity.com", "Steam"), //
        stumbleupon("http://www.stumbleupon.com", "StumbleUpon"), //
        tumblr("http://www.tumblr.com", "Tumblr"), //
        twitter("http://twitter.com", "Twitter"), //
        viadeo("http://www.viadeo.com", "Viadeo.com"), //
        vimeo("http://vimeo.com", "Vimeo"), //
        weibo("http://weibo.com", "Sina Visitor System"), //
        wikipedia("http://www.wikipedia.org", "Wikipedia"), //
        windows("http://windows.microsoft.com", "Windows"), //
        wordpress("http://www.wordpress.com", "WordPress.com"), //
        xing("http://www.xing.com", "XING"), //
        yahoo("http://www.yahoo.com", "Yahoo"), //
        yelp("http://www.yelp.com", "Yelp"), //
        youtube("http://www.youtube.com", "YouTube");//
        // forrst("", ""), // taken over
        // eventasaurus("", ""), // dead
        // posterous("", ""), //dead

        private final String url;

        private final String name;

        Social(String url, String name) {
            this.url = url;
            this.name = name;
        }

        public String getUrl() {
            return this.url;
        }

        public String getName() {
            return this.name;
        }
    }
}
