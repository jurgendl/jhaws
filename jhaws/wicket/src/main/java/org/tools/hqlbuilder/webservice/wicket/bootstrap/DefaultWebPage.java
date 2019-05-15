package org.tools.hqlbuilder.webservice.wicket.bootstrap;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.bootstrap4.bootbox.BootBox;
import org.tools.hqlbuilder.webservice.bootstrap4.clipboardjs.ClipboardJs;
import org.tools.hqlbuilder.webservice.bootstrap4.colorpicker.BootstrapColorPicker;
import org.tools.hqlbuilder.webservice.bootstrap4.customfileinput.CustomFileInput;
import org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus.BootstrapTempusDominusDateTimePicker;
import org.tools.hqlbuilder.webservice.bootstrap4.multiselect.MultiSelect;
import org.tools.hqlbuilder.webservice.bootstrap4.slider.BootstrapSlider;
import org.tools.hqlbuilder.webservice.bootstrap4.tags.BootstrapTags;
import org.tools.hqlbuilder.webservice.bootstrap4.tinymce.BootstrapTinyMCE;
import org.tools.hqlbuilder.webservice.bootstrap4.toast.BootstrapToasts;
import org.tools.hqlbuilder.webservice.jquery.ui.blazy.BLazy;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.jquery.ui.magnify.Magnify;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.jquery.ui.picturefill.PictureFill;
import org.tools.hqlbuilder.webservice.jquery.ui.spin.Spin;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome.FontAwesome;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.components.ExternalLink;

@SuppressWarnings("serial")
public abstract class DefaultWebPage extends WebPage {
    public DefaultWebPage() {
        this(new PageParameters());
    }

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        // // html language
        // WebMarkupContainer html = new WebMarkupContainer("html");
        // add(html.add(new AttributeModifier("lang",
        // getSession().getLocale().getLanguage())));
        addDefaultComponents(parameters, this);
        addComponents(parameters, this);
    }

    protected String getPageTitle(PageParameters parameters) {
        return getString("page.title");
    }

    protected String[] getPageKeywords(PageParameters parameters) {
        return null;
    }

    protected String getPageDescription(PageParameters parameters) {
        return null;
    }

    protected String getPageAuthor(PageParameters parameters) {
        return null;
    }

    protected URL getPageAuthorLink(PageParameters parameters) {
        return null;
    }

    /**
     * "Open Graph Reference Documentation _ og_type.pdf" https://developers.facebook.com/docs/reference/opengraph/
     */
    protected String getOgType(PageParameters parameters) {
        return null;
    }

    protected URL getOgImage(PageParameters parameters) {
        return null;
    }

    protected URL getOgUrl(PageParameters parameters) {
        return null;
    }

    protected void addDefaultComponents(PageParameters parameters, MarkupContainer html) {
        // http://www.iacquire.com/blog/18-meta-tags-every-webpage-should-have-in-2013

        // meta tags
        addMetaTags(parameters, html);

        // Facebook Open Graph
        addFacebookOpenGraph(parameters, html, true);

        // title
        addTitle(parameters, html, "page.title");

        // spinner
        addSpinner(html, "spinnercontainer", "spinner");

        // shortcut icon
        addShortcutIcon(html, "shortcutIcon");

        // wicket/ajax debug bars
        addWicketDebuggers(html, "debug");

        // check if javascript is enabled
        addCheckJavascript(html);

        // check if cookies are enabled
        addCheckCookies(html);

        // check if ads are not blocked
        addCkechAddBlock(html);

        // add header response (javascript) down below on page
        addJavasScriptOnBottom(html);

        // meta description
        addMetaDescription(html);

        // add google meta tags
        addGoogleId(html);

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

    protected void addGoogleId(MarkupContainer html) {
        String googleSigninClientId = WicketApplication.get().getSettings().getGoogleSigninClientId();
        html.add(new WebMarkupContainer("meta_google_signin_scope").setVisible(StringUtils.isNotBlank(googleSigninClientId)));
        html.add(new WebMarkupContainer("meta_google_signin_client_id").add(new AttributeModifier("content", googleSigninClientId))
                .setVisible(StringUtils.isNotBlank(googleSigninClientId)));
    }

    protected void addMetaDescription(MarkupContainer html) {
        html.add(new WebMarkupContainer("meta_description").setVisible(false));
    }

    protected void addJavasScriptOnBottom(MarkupContainer html) {
        if (WicketApplication.get().getSettings().isJavascriptAtBottom()) {
            html.add(new HeaderResponseContainer("footer-container", "footer-bucket"));
        } else {
            html.add(new EmptyPanel("footer-container").setVisible(false));
        }
    }

    protected void addCkechAddBlock(MarkupContainer html) {
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
        html.add(WicketApplication.get().getSettings().isShowDebugbars() && WicketApplication.get().usesDevelopmentConfig() ? new DebugBar(id)
                : new EmptyPanel(id).setVisible(false));
    }

    protected void addShortcutIcon(MarkupContainer html, String id) {
        html.add(new WebMarkupContainer(id).add(new AttributeModifier("href", Model.of(WicketApplication.get().getSettings().getShortcutIcon())))
                .setVisible(StringUtils.isNotBlank(WicketApplication.get().getSettings().getShortcutIcon())));
    }

    protected void addSpinner(MarkupContainer html, String spinnercontainer, String spinner) {
        html.add(new WebMarkupContainer(spinnercontainer).add(new WebMarkupContainer(spinner)
                .add(AttributeModifier.replace("class", "loader loader-" + WicketApplication.get().getSettings().getSpinner() + " is-active"))));
    }

    public FeedbackPanel getFeedbackPanel() {
        return FeedbackPanel.class.cast(get("pagefeedbackmessages"));
    }

    protected void addFeedbackMessagePanel(PageParameters parameters, MarkupContainer html, String id) {
        html.add(new BootstrapFencedFeedbackPanel(id));
    }

    protected void addMetaTags(PageParameters parameters, MarkupContainer html) {
        {
            WebMarkupContainer pageKeywords = new WebMarkupContainer("page.keywords");
            if (getPageKeywords(parameters) != null) {
                pageKeywords.add(new AttributeModifier("content", Arrays.stream(getPageKeywords(parameters)).collect(Collectors.joining(","))));
            } else {
                pageKeywords.setVisible(false);
            }
            html.add(pageKeywords);
        }
        {
            WebMarkupContainer pageDescription = new WebMarkupContainer("page.description");
            if (StringUtils.isNotBlank(getPageDescription(parameters))) {
                pageDescription.add(new AttributeModifier("content", getPageDescription(parameters)));
            } else {
                pageDescription.setVisible(false);
            }
            html.add(pageDescription);
        }
        {
            WebMarkupContainer pageAuthor = new WebMarkupContainer("page.author");
            if (StringUtils.isNotBlank(getPageAuthor(parameters))) {
                pageAuthor.add(new AttributeModifier("content", getPageAuthor(parameters)));
                if (getPageAuthorLink(parameters) != null) {
                    pageAuthor.add(new AttributeModifier("href", getPageAuthorLink(parameters).toString()));
                }
            } else {
                pageAuthor.setVisible(false);
            }
            html.add(pageAuthor);
        }
    }

    protected void addFacebookOpenGraph(PageParameters parameters, MarkupContainer html, boolean show) {
        {
            WebMarkupContainer ogTitle = new WebMarkupContainer("og.title");
            if (show && StringUtils.isNotBlank(getPageTitle(parameters))) {
                ogTitle.add(new AttributeModifier("content", getPageTitle(parameters)));
            } else {
                ogTitle.setVisible(false);
            }
            html.add(ogTitle);
        }
        {
            WebMarkupContainer ogType = new WebMarkupContainer("og.type");
            if (show && StringUtils.isNotBlank(getOgType(parameters))) {
                ogType.add(new AttributeModifier("content", getOgType(parameters)));
            } else {
                ogType.setVisible(false);
            }
            html.add(ogType);
        }
        {
            WebMarkupContainer ogImage = new WebMarkupContainer("og.image");
            if (show && getOgImage(parameters) != null) {
                ogImage.add(new AttributeModifier("content", getOgImage(parameters).toString()));
            } else {
                ogImage.setVisible(false);
            }
            html.add(ogImage);
        }
        {
            WebMarkupContainer ogUrl = new WebMarkupContainer("og.url");
            if (show && getOgUrl(parameters) != null) {
                ogUrl.add(new AttributeModifier("content", getOgUrl(parameters).toString()));
            } else {
                ogUrl.setVisible(false);
            }
            html.add(ogUrl);
        }
        {
            WebMarkupContainer ogDescription = new WebMarkupContainer("og.description");
            if (show && StringUtils.isNotBlank(getPageDescription(parameters))) {
                ogDescription.add(new AttributeModifier("content", getPageDescription(parameters)));
            } else {
                ogDescription.setVisible(false);
            }
            html.add(ogDescription);
        }
    }

    protected void addNavigationBar(PageParameters parameters, MarkupContainer html, String id) {
        addNavigationBar(parameters, html, id, new ArrayList<>(), false, false, false);
    }

    protected Component addNavigationBar(PageParameters parameters, MarkupContainer html, String id, List<NavBarLink> navs, boolean userButton,
            boolean searchBar, boolean backToTopButton) {
        WebMarkupContainer navbar = new WebMarkupContainer(id);

        ExternalLink navbarbrandlink = new ExternalLink("navbarbrandlink", "#");
        navbarbrandlink.add(new WebMarkupContainer("navbarbrandicon"));
        navbarbrandlink.add(new Label("navbarbrandlabel", ""));
        navbarbrandlink.setVisible(false);
        navbar.add(navbarbrandlink);

        if (navs == null || navs.isEmpty()) {
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
                    navbaritemicon.add(AttributeAppender.replace("class", main.getIcon()));
                }
                if (StringUtils.isNotBlank(main.getStyle())) {
                    navbaritemicon.add(new AttributeModifier("style", main.getStyle()));
                }
                link.add(navbaritemicon);
                link.add(new Label("navbaritemlabel", main.getLabel()));
                // link.add(AttributeAppender.append("class","disabled"));
                item.add(link);

                WebMarkupContainer navbardropdown = new WebMarkupContainer("navbardropdown");
                navbardropdown.add(new AttributeModifier("aria-labelledby", link.getMarkupId()));
                if (main.getChildLinks().isEmpty()) {
                    navbardropdown.setVisible(false);
                } else {
                    item.add(AttributeAppender.append("class", "dropdown"));
                    link.add(AttributeAppender.append("class", "dropdown-toggle"));
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
                        BookmarkablePageLink<String> sublink = new BookmarkablePageLink<String>("navbardropdownitemlink", sub.getInternalPage(),
                                sub.getInternalPageParameters());
                        WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbardropdownitemicon");
                        if (StringUtils.isNotBlank(sub.getIcon())) {
                            navbaritemicon.add(AttributeAppender.replace("class", sub.getIcon()));
                        }
                        if (StringUtils.isNotBlank(sub.getStyle())) {
                            navbaritemicon.add(new AttributeModifier("style", sub.getStyle()));
                        }
                        sublink.add(navbaritemicon);
                        sublink.add(new Label("navbardropdownitemlabel", sub.getLabel()));
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

    abstract protected void addComponents(PageParameters parameters, MarkupContainer html);

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(OnDomReadyHeaderItem.forScript(";var " + MomentJs.PROP_CURRENT_LANGUAGE + "='" + getLocale().getLanguage() + "';"));

        response.render(JavaScriptHeaderItem.forReference(JQuery.getJQueryReference()));

        response.render(CssHeaderItem.forReference(Bootstrap4.CSS));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_POPPER));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_IE10FIX));
        response.render(Bootstrap4.factory());

        response.render(CssHeaderItem.forReference(FontAwesome.CSS5));
        // response.render(CssHeaderItem.forReference(new
        // LessResourceReference(DefaultWebPage.class,
        // "org/tools/hqlbuilder/webservice/bootstrap4/social/brand.less")));
        response.render(CssHeaderItem.forReference(new CssResourceReference(Bootstrap4.class, "social/brand.css")));

        response.render(CssHeaderItem.forReference(BLazy.CSS));
        response.render(JavaScriptHeaderItem.forReference(BLazy.JS));
        response.render(BLazy.FACTORY);

        response.render(JavaScriptHeaderItem.forReference(PictureFill.JS));
        response.render(PictureFill.FACTORY);

        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_LOCALE));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_I18N));
        response.render(JavaScriptHeaderItem.forReference(MomentJs.JS_PLUGIN_PRECISE_RANGE));
        response.render(MomentJs.FACTORY);

        response.render(CssHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.JS));
        response.render(BootstrapTempusDominusDateTimePicker.FACTORY);

        response.render(CssHeaderItem.forReference(BootstrapColorPicker.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapColorPicker.JS));
        response.render(BootstrapColorPicker.FACTORY);

        response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));

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

        response.render(CssHeaderItem.forReference(BootstrapSlider.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapSlider.JS));

        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS_BLOODHOUND));
        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.bootstrap4.typeahead.TypeAhead.JS));

        response.render(CssHeaderItem.forReference(BootstrapTags.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTags.JS));

        response.render(CssHeaderItem.forReference(MultiSelect.CSS));
        response.render(JavaScriptHeaderItem.forReference(MultiSelect.JS));
        response.render(MultiSelect.JS_FACTORY);

        response.render(JavaScriptHeaderItem.forReference(CustomFileInput.JS));

        response.render(JavaScriptHeaderItem.forReference(ClipboardJs.JS));
        response.render(OnDomReadyHeaderItem.forScript(ClipboardJs.factory()));

        response.render(JavaScriptHeaderItem.forReference(BootstrapToasts.JS));
        response.render(CssHeaderItem.forReference(BootstrapToasts.CSS));

        // response.render(JavaScriptHeaderItem.forReference(BootstrapConfirmation.JS));
        // response.render(OnLoadHeaderItem.forScript(";$('[data-toggle=confirmation]').confirmation({rootSelector:'[data-toggle=confirmation]'});"));

        response.render(CssHeaderItem.forReference(org.tools.hqlbuilder.webservice.css.WicketCSSRoot.ANIMATE));

        response.render(CssHeaderItem.forReference(Spin.css(WicketApplication.get().getSettings().getSpinner())));

        response.render(CssHeaderItem.forReference(Magnify.MAGNIFY_CSS));
        response.render(JavaScriptHeaderItem.forReference(Magnify.MAGNIFY_JS));

        response.render(CssHeaderItem.forReference(new CssResourceReference(DefaultWebPage.class, "DefaultWebPage.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(DefaultWebPage.class, "DefaultWebPage.js")));
        response.render(OnDomReadyHeaderItem.forScript(new FilePath(DefaultWebPage.class, "DefaultWebPage-factory.js").readAll()));
    }
}
