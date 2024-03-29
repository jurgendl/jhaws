package org.jhaws.common.web.wicket.bootstrap;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.AbstractReadOnlyModel;
import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;
import org.jhaws.common.web.wicket.WicketRoot;
import org.jhaws.common.web.wicket.blazy.BLazy;
import org.jhaws.common.web.wicket.bootbox.BootBox;
import org.jhaws.common.web.wicket.bootstrapselect.BootstrapSelect;
import org.jhaws.common.web.wicket.clipboardjs.ClipboardJs;
import org.jhaws.common.web.wicket.colorpicker.BootstrapColorPicker;
import org.jhaws.common.web.wicket.components.ExternalLink;
import org.jhaws.common.web.wicket.confirmation.Bootstrap4Confirmation;
import org.jhaws.common.web.wicket.customfileinput.CustomFileInput;
import org.jhaws.common.web.wicket.datetimepicker.tempusdominus.BootstrapTempusDominusDateTimePicker;
import org.jhaws.common.web.wicket.fontawesome.FontAwesome;
import org.jhaws.common.web.wicket.jquery.JQuery;
import org.jhaws.common.web.wicket.magnify.Magnify;
import org.jhaws.common.web.wicket.materialdesign.Materialdesign;
import org.jhaws.common.web.wicket.moment.MomentJs;
import org.jhaws.common.web.wicket.multiselect.MultiSelect;
import org.jhaws.common.web.wicket.picturefill.PictureFill;
import org.jhaws.common.web.wicket.popoverx.PopoverX;
import org.jhaws.common.web.wicket.qtip.QTip;
import org.jhaws.common.web.wicket.slider.BootstrapSlider;
import org.jhaws.common.web.wicket.spin.Spin;
import org.jhaws.common.web.wicket.spin.Spin.SpinType;
import org.jhaws.common.web.wicket.tags.BootstrapTags;
import org.jhaws.common.web.wicket.tinymce.BootstrapTinyMCE;
import org.jhaws.common.web.wicket.toast.BootstrapToasts;
import org.jhaws.common.web.wicket.waypoints.Waypoints;

@SuppressWarnings("serial")
public abstract class DefaultWebPage extends WebPage {
    public static final String FACTORY = new FilePath(WicketRoot.class, "bootstrap/base-factory.js").readAll();

    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(WicketRoot.class, "bootstrap/base.js");

    public static final CssResourceReference CSS = new CssResourceReference(WicketRoot.class, "bootstrap/base.css");

    public DefaultWebPage() {
        this(new PageParameters());
    }

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        setHtmlLanguageTag();
        addDefaultComponents(parameters, this);
        addComponents(parameters, this);
    }

    protected void setHtmlLanguageTag() {
        // http://apache-wicket.1842946.n4.nabble.com/How-can-i-change-lang-attribute-in-html-markup-tag-td3222720.html
        TransparentWebMarkupContainer htmlTag = new TransparentWebMarkupContainer("html");
        htmlTag.add(new AttributeModifier("lang", getLanguageIsocode()));
        add(htmlTag);
    }

    protected IModel<String> getLanguageIsocode() {
        return new AbstractReadOnlyModel<>() {
            @Override
            public String getObject() {
                return getSession().getLocale().toString();
            }
        };
    }

    protected IModel<String> getPageTitle(PageParameters parameters) {
        return Model.of(getString("page.title"));
    }

    protected IModel<String> getPageKeywords(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getPageDescription(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getPageAuthor(PageParameters parameters) {
        return null;
    }

    protected IModel<URL> getPageAuthorLink(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphType(PageParameters parameters) {
        return null;
    }

    protected IModel<URL> getOpenGraphImage(PageParameters parameters) {
        return null;
    }

    protected IModel<URL> getOpenGraphUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphTitle(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphVideo(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphSiteName(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphLocaleAlternate(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphLocale(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphDeterminer(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphAudio(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenGraphDescription(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageSecureUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageType(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageWidth(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageHeight(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenImageAlt(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoSecureUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoType(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoWidth(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoHeight(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenVideoAlt(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenAudioType(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenAudioSecureUrl(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getOpenAudioUrl(PageParameters parameters) {
        return null;
    }

    protected void addDefaultComponents(PageParameters parameters, MarkupContainer html) {
        // http://www.iacquire.com/blog/18-meta-tags-every-webpage-should-have-in-2013

        // meta tags
        addMetaTags(parameters, html, true);

        // Open Graph
        addOpenGraph(parameters, html, true);

        // twitter
        addTwitter(parameters, html, true);

        // title
        addTitle(parameters, html, "page.title");

        // spinner
        addSpinner(html, "spinnercontainer", "spinner", WicketApplication.get().getSettings().getSpinner());

        // shortcut icon
        addShortcutIcon(html, "shortcutIcon");

        // wicket/ajax debug bars
        addWicketDebuggers(html, "debug");

        // check if javascript is enabled
        addCheckJavascript(html);

        // check if cookies are enabled
        addCheckCookies(html);

        // check if ads are not blocked
        addCheckAddBlock(html);

        // FIXME WICKET UPDATE
        // add header response (javascript) down below on page
        addJavaScriptOnBottom(html);

        // add google meta tags
        addGoogleId(parameters, html, true);
        addGoogleSiteVerification(parameters, html, true);

        // navbar
        addNavigationBar(parameters, html, "navbar");

        // breadcrumb
        addBreadcrumb(parameters, html, "breadcrumb");

        // pagefeedbackmessages
        addFeedbackMessagePanel(parameters, html, "pagefeedbackmessages");

        // statusbar
        addStatusBar(parameters, html, "statusbar", "statusbarcontent");
    }

    protected void addTitle(PageParameters parameters, MarkupContainer html, String id) {
        html.add(new Label(id, getPageTitle(parameters)));
    }

    protected void addGoogleSiteVerification(PageParameters parameters, MarkupContainer html, boolean show) {
        WebMarkupContainer metaContainer = new WebMarkupContainer("google.site.verification");
        if (show) {
            IModel<String> metaModel = getGoogleSiteVerification(parameters);
            if (metaModel != null) {
                metaContainer.add(new AttributeModifier("content", metaModel));
            } else {
                metaContainer.setVisible(false);
            }
        } else {
            metaContainer.setVisible(false);
        }
        html.add(metaContainer);
    }

    protected IModel<String> getGoogleSiteVerification(PageParameters parameters) {
        return null;
    }

    protected void addGoogleId(PageParameters parameters, MarkupContainer html, boolean show) {
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("google.signin.scope");
            if (show) {
                IModel<String> metaModel = getGoogleSigninScope(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("google.signin.client.id");
            if (show) {
                IModel<String> metaModel = getGoogleSigninClientId(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
    }

    protected IModel<String> getGoogleSigninClientId(PageParameters parameters) {
        String googleSigninClientId = WicketApplication.get().getSettings().getGoogleSigninClientId();
        return StringUtils.isBlank(googleSigninClientId) ? null : Model.of(googleSigninClientId);
    }

    protected IModel<String> getGoogleSigninScope(PageParameters parameters) {
        String googleSigninClientId = WicketApplication.get().getSettings().getGoogleSigninClientId();
        return StringUtils.isBlank(googleSigninClientId) ? null : Model.of("profile email");
    }

    protected void addJavaScriptOnBottom(MarkupContainer html) {
        boolean javascriptAtBottom = WicketApplication.get().getSettings().isJavascriptAtBottom();
        if (javascriptAtBottom) {
            html.add(new HeaderResponseContainer("footer-container", "footer-bucket"));
        } else {
            html.add(new EmptyPanel("footer-container").setVisible(false));
        }
    }

    protected void addCheckAddBlock(MarkupContainer html) {
        try {
            html.add(new CheckAdsEnabled());
        } catch (Exception ex) {
            html.add(new EmptyPanel("check.ads.enabled").setVisible(false));
        }
    }

    protected void addCheckCookies(MarkupContainer html) {
        html.add(new CheckCookiesEnabled());
    }

    protected void addCheckJavascript(MarkupContainer html) {
        html.add(new CheckJavaScriptEnabled());
    }

    protected void addWicketDebuggers(MarkupContainer html, String id) {
        html.add(WicketApplication.get().getSettings().isShowDebugbars() && WicketApplication.get().usesDevelopmentConfig() ? new DebugBar(id) : new EmptyPanel(id).setVisible(false));
    }

    protected void addShortcutIcon(MarkupContainer html, String id) {
        html.add(new WebMarkupContainer(id).add(new AttributeModifier("href", Model.of(WicketApplication.get().getSettings().getShortcutIcon()))).setVisible(StringUtils.isNotBlank(WicketApplication.get().getSettings().getShortcutIcon())));
    }

    protected WebMarkupContainer addSpinner(MarkupContainer html, String spinnercontainer, String spinner, String spinnerId) {
        boolean spindebug = false;
        if (spindebug) System.out.println(spinnerId);
        SpinType _spinType;
        try {
            _spinType = StringUtils.isBlank(spinnerId) ? SpinType._default : Arrays.stream(SpinType.values()).filter(st -> spinnerId.equals(st.id())).findAny().orElseGet(() -> SpinType.valueOf(spinnerId));
        } catch (Exception ex) {
            _spinType = SpinType._default;
        }
        SpinType spinType = _spinType;
        if (spindebug) System.out.println(spinType);
        Component spinnerTag = new WebMarkupContainer(spinner) {
            @Override
            public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                String body = Spin.body(spinType);
                if (spindebug) System.out.println(body);
                replaceComponentTagBody(markupStream, openTag, body);
            }
        };
        String spinClass = spinType.id();
        if (spinType.name().startsWith("sk_")) {
            //
        } else if (spinType.name().startsWith("load")) {
            spinClass = spinType.name();
        } else {
            spinClass = "loader loader-" + spinClass + " is-active";
        }
        if (spindebug) System.out.println(spinClass);
        spinnerTag.add(AttributeModifier.replace("class", spinClass));
        WebMarkupContainer spinnercontainerTag = new WebMarkupContainer(spinnercontainer);
        if (spindebug) spinnercontainerTag.add(AttributeModifier.replace("class", ""));
        spinnercontainerTag.add(spinnerTag);
        html.add(spinnercontainerTag);
        return spinnercontainerTag;
    }

    public FeedbackPanel getFeedbackPanel() {
        return FeedbackPanel.class.cast(get("pagefeedbackmessages"));
    }

    protected void addFeedbackMessagePanel(PageParameters parameters, MarkupContainer html, String id) {
        html.add(new BootstrapFencedFeedbackPanel(id));
    }

    protected void addMetaTags(PageParameters parameters, MarkupContainer html, boolean show) {
        {
            WebMarkupContainer pageKeywords = new WebMarkupContainer("page.keywords");
            IModel<String> pageKeywordsModel = getPageKeywords(parameters);
            if (pageKeywordsModel != null) {
                pageKeywords.add(new AttributeModifier("content", pageKeywordsModel));
            } else {
                pageKeywords.setVisible(false);
            }
            html.add(pageKeywords);
        }
        IModel<String> pageDescriptionModel = getPageDescription(parameters);
        {
            WebMarkupContainer pageDescription = new WebMarkupContainer("page.description");
            if (pageDescriptionModel != null) {
                pageDescription.add(new AttributeModifier("content", pageDescriptionModel));
            } else {
                pageDescription.setVisible(false);
            }
            html.add(pageDescription);
        }
        {
            WebMarkupContainer pageRobots = new WebMarkupContainer("page.robots");
            IModel<String> pageRobotsModel = getPageRobots(parameters);
            if (pageRobotsModel != null) {
                pageRobots.add(new AttributeModifier("content", pageRobotsModel));
            } else {
                pageRobots.setVisible(false);
            }
            html.add(pageRobots);
        }
        {
            WebMarkupContainer pageAuthor = new WebMarkupContainer("page.author");
            IModel<String> pageAuthorModel = getPageAuthor(parameters);
            if (pageAuthorModel != null) {
                pageAuthor.add(new AttributeModifier("content", pageAuthorModel));
                IModel<URL> pageAuthorLinkModel = getPageAuthorLink(parameters);
                if (pageAuthorLinkModel != null) {
                    pageAuthor.add(new AttributeModifier("href", pageAuthorLinkModel));
                }
            } else {
                pageAuthor.setVisible(false);
            }
            html.add(pageAuthor);
        }
    }

    /**
     * https://developers.google.com/search/docs/advanced/robots/robots_meta_tag#directives
     */
    protected IModel<String> getPageRobots(PageParameters parameters) {
        return Model.of("index, follow");
    }

    protected void addTwitter(PageParameters parameters, MarkupContainer html, boolean show) {
        // https://developer.twitter.com/en/docs/twitter-for-websites/cards/overview/summary
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.card");
            if (show) {
                IModel<String> metaModel = getTwitterCard(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.site");
            if (show) {
                IModel<String> metaModel = getTwitterSite(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.title");
            if (show) {
                IModel<String> metaModel = getTwitterTitle(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.description");
            if (show) {
                IModel<String> metaModel = getTwitterDescription(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.image");
            if (show) {
                IModel<String> metaModel = getTwitterImage(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("twitter.image.alt");
            if (show) {
                IModel<String> metaModel = getTwitterImageAlt(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
    }

    protected IModel<String> getTwitterImageAlt(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getTwitterImage(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getTwitterDescription(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getTwitterTitle(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getTwitterSite(PageParameters parameters) {
        return null;
    }

    protected IModel<String> getTwitterCard(PageParameters parameters) {
        return null;
    }

    /**
     * "Open Graph Reference Documentation _ og_type.pdf"<br>
     * https://developers.facebook.com/docs/reference/opengraph/<br>
     * https://ogp.me/<br>
     */
    protected void addOpenGraph(PageParameters parameters, MarkupContainer html, boolean show) {
        // og:title - The title of your object as it should appear within the graph,
        // e.g., "The Rock".
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.title");
            if (show) {
                IModel<String> metaModel = getOpenGraphTitle(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:type - The type of your object, e.g., "video.movie". Depending on the type
        // you specify, other properties may also be required.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.type");
            if (show) {
                IModel<String> metaModel = getOpenGraphType(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image - An image URL which should represent your object within the graph.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image");
            if (show) {
                IModel<URL> metaModel = getOpenGraphImage(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel.toString()));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:url - The canonical URL of your object that will be used as its permanent
        // ID in the graph, e.g., "https://www.imdb.com/title/tt0117500/".
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.url");
            if (show) {
                IModel<URL> metaModel = getOpenGraphUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel.toString()));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:description - A one to two sentence description of your object.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.description");
            if (show) {
                IModel<String> metaModel = getOpenGraphDescription(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:audio - A URL to an audio file to accompany this object.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.audio");
            if (show) {
                IModel<String> metaModel = getOpenGraphAudio(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:determiner - The word that appears before this object's title in a
        // sentence. An enum of (a, an, the, "", auto). If auto is chosen, the consumer
        // of your data should
        // chose between "a" or "an". Default is "" (blank).
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.determiner");
            if (show) {
                IModel<String> metaModel = getOpenGraphDeterminer(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:locale - The locale these tags are marked up in. Of the format
        // language_TERRITORY. Default is en_US.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.locale");
            if (show) {
                IModel<String> metaModel = getOpenGraphLocale(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:locale:alternate - An array of other locales this page is available in.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.locale.alternate");
            if (show) {
                IModel<String> metaModel = getOpenGraphLocaleAlternate(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:site_name - If your object is part of a larger web site, the name which
        // should be displayed for the overall site. e.g., "IMDb".
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.site_name");
            if (show) {
                IModel<String> metaModel = getOpenGraphSiteName(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video - A URL to a video file that complements this object.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video");
            if (show) {
                IModel<String> metaModel = getOpenGraphVideo(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:url - Identical to og:image.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.url");
            if (show) {
                IModel<String> metaModel = getOpenImageUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:secure_url - An alternate url to use if the webpage requires HTTPS.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.secure_url");
            if (show) {
                IModel<String> metaModel = getOpenImageSecureUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:type - A MIME type for this image.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.type");
            if (show) {
                IModel<String> metaModel = getOpenImageType(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:width - The number of pixels wide.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.width");
            if (show) {
                IModel<String> metaModel = getOpenImageWidth(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:height - The number of pixels high.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.height");
            if (show) {
                IModel<String> metaModel = getOpenImageHeight(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:image:alt - A description of what is in the image (not a caption). If the
        // page specifies an og:image it should specify og:image:alt.
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.image.alt");
            if (show) {
                IModel<String> metaModel = getOpenImageAlt(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.url");
            if (show) {
                IModel<String> metaModel = getOpenVideoUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video:secure_url
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.secure_url");
            if (show) {
                IModel<String> metaModel = getOpenVideoSecureUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video:type
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.type");
            if (show) {
                IModel<String> metaModel = getOpenVideoType(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video:width
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.width");
            if (show) {
                IModel<String> metaModel = getOpenVideoWidth(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video:height
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.height");
            if (show) {
                IModel<String> metaModel = getOpenVideoHeight(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:video:alt
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.video.alt");
            if (show) {
                IModel<String> metaModel = getOpenVideoAlt(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:audio
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.audio.url");
            if (show) {
                IModel<String> metaModel = getOpenAudioUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:audio:secure_url
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.audio.secure_url");
            if (show) {
                IModel<String> metaModel = getOpenAudioSecureUrl(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
        // og:audio:type
        {
            WebMarkupContainer metaContainer = new WebMarkupContainer("og.audio.type");
            if (show) {
                IModel<String> metaModel = getOpenAudioType(parameters);
                if (metaModel != null) {
                    metaContainer.add(new AttributeModifier("content", metaModel));
                } else {
                    metaContainer.setVisible(false);
                }
            } else {
                metaContainer.setVisible(false);
            }
            html.add(metaContainer);
        }
    }

    @SuppressWarnings("unchecked")
    protected void addNavigationBar(PageParameters parameters, MarkupContainer html, String id) {
        List<NavBarLink> navs = new ArrayList<>();
        navs.add(new NavBarLink("Home", "fa-fw fas fa-home", WicketApplication.get().getHomePage(), null));
        demos(navs);
        navs.add(new NavBarLink("Refresh", "fa-fw fas fa-sync-alt", (Class<? extends WebPage>) getPageClass(), getPageParameters()));
        addNavigationBar(parameters, html, id, navs, false, false, false);
    }

    protected void demos(List<NavBarLink> navs) {
        NavBarLink demos = new NavBarLink("Demo", null, null, null);
        demos.getChildLinks().add(new NavBarLink("settings", "", org.jhaws.common.web.wicket.settings.SettingsPage.class, null));
        demos.getChildLinks().add(new NavBarLink("overview", "", org.jhaws.common.web.wicket.demo.TestPage.class, null));
        demos.getChildLinks().add(new NavBarLink("vue", "", org.jhaws.common.web.wicket.demo.VueTestPage.class, null));
        demos.getChildLinks().add(new NavBarLink("messages", "", org.jhaws.common.web.wicket.demo.TestMessagesPage.class, null));
        demos.getChildLinks().add(new NavBarLink("upload", "", org.jhaws.common.web.wicket.demo.UploadTestPage.class, null));
        //
        demos.getChildLinks().add(new NavBarLink("registration", "", org.jhaws.common.web.wicket.demo.RegistrationPage.class, null));
        demos.getChildLinks().add(new NavBarLink("login", "", org.jhaws.common.web.wicket.pages.LogInPage.class, null));
        demos.getChildLinks().add(new NavBarLink("logout", "", org.jhaws.common.web.wicket.pages.LogOutPage.class, null));
        demos.getChildLinks().add(new NavBarLink("public", "", org.jhaws.common.web.wicket.pages.PublicPage.class, null));
        demos.getChildLinks().add(new NavBarLink("register", "", org.jhaws.common.web.wicket.pages.RegisterPage.class, null));
        navs.add(demos);
    }

    protected Component addNavigationBar(PageParameters parameters, MarkupContainer html, String id, List<NavBarLink> navs, boolean userButton, boolean searchBar, boolean backToTopButton) {
        WebMarkupContainer navbar = new WebMarkupContainer(id);

        ExternalLink navbarbrandlink = new ExternalLink("navbarbrandlink", "#");
        navbarbrandlink.add(new WebMarkupContainer("navbarbrandicon"));
        navbarbrandlink.add(new Label("navbarbrandlabel", ""));
        navbarbrandlink.setVisible(false);
        navbar.add(navbarbrandlink);

        if (navs != null && navs.isEmpty()) {
            navs.add(new NavBarLink("Home", "fa-fw fas fa-home", getClass(), null));
        }

        navbar.add(new ListView<NavBarLink>("navbaritems", navs) {
            @Override
            protected void populateItem(ListItem<NavBarLink> item) {
                // item.add(AttributeAppender.append("class","active"));
                NavBarLink main = item.getModelObject();

                WebMarkupContainer link;
                if (main.getChildLinks().isEmpty()) {
                    link = new BookmarkablePageLink<String>("navbaritemlink", main.getInternalPage(), main.getInternalPageParameters());
                } else {
                    link = new WebMarkupContainer("navbaritemlink");
                }

                WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbaritemicon");
                if (StringUtils.isNotBlank(main.getIcon())) {
                    navbaritemicon.add(AttributeModifier.replace("class", main.getIcon()));
                }
                if (StringUtils.isNotBlank(main.getStyle())) {
                    navbaritemicon.add(new AttributeModifier("style", main.getStyle()));
                }
                link.add(navbaritemicon);
                Label label = new Label("navbaritemlabel", main.getLabel());
                if (StringUtils.isNotBlank(main.getCssClass())) {
                    label.add(new AttributeAppender("class", main.getCssClass(), " "));
                }
                link.add(label);
                // link.add(AttributeAppender.append("class","disabled"));
                item.add(link);

                WebMarkupContainer navbardropdown = new WebMarkupContainer("navbardropdown");
                navbardropdown.add(new AttributeModifier("aria-labelledby", link.getMarkupId()));
                if (main.getChildLinks().isEmpty()) {
                    navbardropdown.setVisible(false);
                } else {
                    item.add(AttributeModifier.append("class", "dropdown"));
                    link.add(AttributeModifier.append("class", "dropdown-toggle"));
                    link.add(new AttributeModifier("data-toggle", "dropdown"));
                    link.add(new AttributeModifier("aria-haspopup", "true"));
                    link.add(new AttributeModifier("aria-expanded", "false"));
                }
                item.add(navbardropdown);
                navbardropdown.add(new ListView<NavBarLink>("navbardropdownitems", main.getChildLinks()) {
                    @Override
                    protected void populateItem(ListItem<NavBarLink> subitem) {
                        // subitem.add(AttributeAppender.append("class","active"));
                        NavBarLink sub = subitem.getModelObject();
                        BookmarkablePageLink<String> sublink = new BookmarkablePageLink<>("navbardropdownitemlink", sub.getInternalPage(), sub.getInternalPageParameters());
                        WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbardropdownitemicon");
                        if (StringUtils.isNotBlank(sub.getIcon())) {
                            navbaritemicon.add(AttributeModifier.replace("class", sub.getIcon()));
                        }
                        if (StringUtils.isNotBlank(sub.getCssClass())) {
                            navbaritemicon.add(new AttributeAppender("class", sub.getCssClass(), " "));
                        }
                        sublink.add(navbaritemicon);
                        Label label = new Label("navbardropdownitemlabel", sub.getLabel());
                        if (StringUtils.isNotBlank(sub.getStyle())) {
                            label.add(new AttributeModifier("style", sub.getStyle()));
                        }
                        sublink.add(label);
                        // sublink.add(AttributeAppender.append("class","disabled"));
                        subitem.add(sublink);
                    }
                });
            }
        });

        // login/logout/user-config menu
        navbar.add(new Button("userButton").setVisible(userButton));

        // searchbar
        navbar.add(new WebMarkupContainer("searchbar").setVisible(searchBar));

        // back to top button
        navbar.add(new Button("backToTopButton").setVisible(backToTopButton));

        html.add(navbar);

        return navbar;
    }

    public static class NavBarLink implements Serializable {
        String externalURL;

        Class<? extends WebPage> internalPage;

        PageParameters internalPageParameters;

        List<NavBarLink> childLinks = new ArrayList<>();

        String icon;

        String label;

        String style;

        String cssClass;

        public NavBarLink() {
            super();
        }

        public NavBarLink(String label, String icon, String externalURL) {
            this.label = label;
            this.icon = icon;
            this.externalURL = externalURL;
        }

        public NavBarLink(String label, String icon, Class<? extends WebPage> internalPage, PageParameters internalPageParameters) {
            this.label = label;
            this.icon = icon;
            this.internalPage = internalPage;
            this.internalPageParameters = internalPageParameters;
        }

        @Override
        public String toString() {
            return label;
        }

        public String getStyle() {
            return this.style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public NavBarLink style(String style) {
            setStyle(style);
            return this;
        }

        public String getExternalURL() {
            return this.externalURL;
        }

        public void setExternalURL(String externalURL) {
            this.externalURL = externalURL;
        }

        public Class<? extends WebPage> getInternalPage() {
            return this.internalPage;
        }

        public void setInternalPage(Class<? extends WebPage> internalPage) {
            this.internalPage = internalPage;
        }

        public PageParameters getInternalPageParameters() {
            return this.internalPageParameters;
        }

        public void setInternalPageParameters(PageParameters internalPageParameters) {
            this.internalPageParameters = internalPageParameters;
        }

        public List<NavBarLink> getChildLinks() {
            return this.childLinks;
        }

        public void setChildLinks(List<NavBarLink> childLinks) {
            this.childLinks = childLinks;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCssClass() {
            return this.cssClass;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }

        public NavBarLink cssClass(String cssClass) {
            setCssClass(cssClass);
            return this;
        }
    }

    protected Component addBreadcrumb(PageParameters parameters, MarkupContainer html, String id) {
        WebMarkupContainer breadcrumb = new WebMarkupContainer(id);
        html.add(breadcrumb);
        breadcrumb.setVisible(false);
        return breadcrumb;
    }

    protected Component addStatusBar(PageParameters parameters, MarkupContainer html, String id, String contentid) {
        Label content = new Label(contentid, " ");
        WebMarkupContainer statusbar = new WebMarkupContainer(id);
        html.add(statusbar.add(content));
        return statusbar;
    }

    protected abstract void addComponents(PageParameters parameters, MarkupContainer html);

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(OnDomReadyHeaderItem.forScript(";var " + MomentJs.PROP_CURRENT_LANGUAGE + "='" + getLocale().getLanguage() + "';"));

        response.render(JavaScriptHeaderItem.forReference(JQuery.getJQueryReference()));

        renderBootstrapTheme(response);
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_IE10FIX));

        response.render(CssHeaderItem.forReference(Materialdesign.CSS));
        response.render(CssHeaderItem.forReference(Materialdesign.CSS_EXTRA));
        response.render(CssHeaderItem.forReference(FontAwesome.CSS5BRANDS));
        response.render(CssHeaderItem.forReference(FontAwesome.CSS5SOLID));
        response.render(CssHeaderItem.forReference(FontAwesome.CSS5REGULAR));
        response.render(CssHeaderItem.forReference(FontAwesome.CSS5SLIM));
        // response.render(CssHeaderItem.forReference(new
        // LessResourceReference(DefaultWebPage.class,
        // "org/tools/hqlbuilder/webservice/bootstrap4/social/brand.less")));
        response.render(CssHeaderItem.forReference(Bootstrap4.BRANDS));

        response.render(CssHeaderItem.forReference(BLazy.CSS));
        response.render(JavaScriptHeaderItem.forReference(BLazy.JS));
        response.render(OnDomReadyHeaderItem.forScript(BLazy.FACTORY));

        response.render(JavaScriptHeaderItem.forReference(PictureFill.JS));
        response.render(OnDomReadyHeaderItem.forScript(PictureFill.FACTORY));

        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_LOCALE));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_I18N));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_PLUGIN_PRECISE_RANGE));
        response.render(OnDomReadyHeaderItem.forScript(MomentJs.FACTORY));

        response.render(CssHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.JS));
        response.render(OnDomReadyHeaderItem.forScript(BootstrapTempusDominusDateTimePicker.FACTORY));

        response.render(CssHeaderItem.forReference(BootstrapColorPicker.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapColorPicker.JS));
        response.render(OnDomReadyHeaderItem.forScript(BootstrapColorPicker.FACTORY));

        response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4Confirmation.JS));

        if (false) {
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_JQUERY));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_LINK));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_CODE));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_LISTS));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_ADVLIST));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_AUTOLINK));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_PRINT));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_SEARCHREPLACE));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_TABLE));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_VISUALCHARS));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_PASTE));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_WORDCOUNT));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_CHARMAP));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_ANCHOR));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_TEXTCOLOR));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_COLORPICKER));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_MEDIA));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_HR));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_IMAGE));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_INSERTDATETIME));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_HELP));
            response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_PLUGIN_PREVIEW));
            response.render(OnDomReadyHeaderItem.forScript(BootstrapTinyMCE.FACTORY));
        }

        response.render(CssHeaderItem.forReference(BootstrapSlider.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapSlider.JS));

        response.render(CssHeaderItem.forReference(BootstrapTags.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTags.JS));

        response.render(CssHeaderItem.forReference(MultiSelect.CSS));
        response.render(JavaScriptHeaderItem.forReference(MultiSelect.JS));
        response.render(OnDomReadyHeaderItem.forScript(MultiSelect.FACTORY));

        response.render(JavaScriptHeaderItem.forReference(CustomFileInput.JS));

        response.render(JavaScriptHeaderItem.forReference(ClipboardJs.JS));
        response.render(OnDomReadyHeaderItem.forScript(ClipboardJs.FACTORY));

        response.render(JavaScriptHeaderItem.forReference(BootstrapToasts.JS));
        response.render(CssHeaderItem.forReference(BootstrapToasts.CSS));

        // response.render(JavaScriptHeaderItem.forReference(BootstrapConfirmation.JS));
        // response.render(OnLoadHeaderItem.forScript(";$('[data-toggle=confirmation]').confirmation({rootSelector:'[data-toggle=confirmation]'});"));

        response.render(CssHeaderItem.forReference(org.jhaws.common.web.wicket.css.WicketCSSRoot.ANIMATE));

        response.render(CssHeaderItem.forReference(Spin.css(WicketApplication.get().getSettings().getSpinner())));

        response.render(CssHeaderItem.forReference(Magnify.CSS));
        response.render(JavaScriptHeaderItem.forReference(Magnify.JS));

        // response.render(CssHeaderItem.forReference(Pace.CSS));
        // response.render(JavaScriptHeaderItem.forReference(Pace.JS));

        response.render(CssHeaderItem.forReference(QTip.CSS));
        response.render(JavaScriptHeaderItem.forReference(QTip.JS));

        response.render(JavaScriptHeaderItem.forReference(PopoverX.JS));
        response.render(CssHeaderItem.forReference(PopoverX.CSS));

        if (false) {
            response.render(JavaScriptHeaderItem.forReference(Waypoints.JS));
            response.render(JavaScriptHeaderItem.forReference(Waypoints.JS_INFINITE));
            response.render(JavaScriptHeaderItem.forReference(Waypoints.JS_INVIEW));
            response.render(JavaScriptHeaderItem.forReference(Waypoints.JS_STICKY));
        }

        response.render(CssHeaderItem.forReference(BootstrapSelect.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapSelect.JS));

        renderBaseCss(response);
        renderBaseJs(response);
        renderBaseJsFactory(response);
    }

    protected void renderBaseJsFactory(IHeaderResponse response) {
        response.render(OnDomReadyHeaderItem.forScript(FACTORY));
    }

    protected void renderBaseJs(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(JS));
    }

    protected void renderBaseCss(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(CSS));
    }

    /** optional, call in renderhead */
    protected void renderCodeStyling(IHeaderResponse response) {
        // response.render(CssHeaderItem.forReference(PrismJs.CSS));
        // response.render(CssHeaderItem.forReference(PrismJs.CSS_OKAIDA));
        // response.render(JavaScriptHeaderItem.forReference(PrismJs.JS_ALL_LANGUAGES_ALL_PLUGINS));
    }

    protected void renderBootstrapTheme(IHeaderResponse response) {
        try {
            String t = WicketApplication.get().getSettings().getTheme().toString();
            if (StringUtils.isNotBlank(t) && !"default".equals(t)) {
                response.render(CssHeaderItem.forReference(Bootstrap4.theme(t)));
            } else {
                response.render(CssHeaderItem.forReference(Bootstrap4.CSS));
            }
        } catch (Exception ex) {
            response.render(CssHeaderItem.forReference(Bootstrap4.CSS));
        }

        response.render(CssHeaderItem.forReference(Bootstrap4.MENLO));
    }
}
