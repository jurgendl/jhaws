package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.bootstrap4.colorpicker.BootstrapColorPicker;
import org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus.BootstrapTempusDominusDateTimePicker;
import org.tools.hqlbuilder.webservice.bootstrap4.slider.BootstrapSlider;
import org.tools.hqlbuilder.webservice.bootstrap4.tags.BootstrapTags;
import org.tools.hqlbuilder.webservice.bootstrap4.tinymce.BootstrapTinyMCE;
import org.tools.hqlbuilder.webservice.jquery.ui.blazy.BLazy;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome.FontAwesome;
import org.tools.hqlbuilder.webservice.wicket.components.ExternalLink;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameModifier;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings("serial")
public abstract class DefaultNewWebPage extends WebPage {
    public DefaultNewWebPage(PageParameters parameters) {
        super(parameters);
        // // html language
        // WebMarkupContainer html = new WebMarkupContainer("html");
        // add(html.add(new AttributeModifier("lang", getSession().getLocale().getLanguage())));
        addDefaultComponents(this);
        addComponents(this);
    }

    protected void addDefaultComponents(MarkupContainer html) {
        // title
        html.add(new Label("page.title", getString("page.title")));

        // shortcut icon
        html.add(new WebMarkupContainer("shortcutIcon").add(new AttributeModifier("href", Model.of(WicketApplication.get().getShortcutIcon())))
                .setVisible(StringUtils.isNotBlank(WicketApplication.get().getShortcutIcon())));

        // wicket/ajax debug bars
        html.add(WicketApplication.get().isShowDebugbars() && WicketApplication.get().usesDevelopmentConfig() ? new DebugBar("debug")
                : new EmptyPanel("debug").setVisible(false));

        // check if javascript is enabled
        html.add(new CheckJavaScriptEnabled());

        // check if cookies are enabled
        html.add(new CheckCookiesEnabled2());

        // check if ads are not blocked
        try {
            html.add(new CheckAdsEnabled());
        } catch (Throwable ex) {
            html.add(new EmptyPanel("check.ads.enabled").setVisible(false));
        }

        // add header response (javascript) down below on page
        if (WicketApplication.get().isJavascriptAtBottom()) {
            html.add(new HeaderResponseContainer("footer-container", "footer-bucket"));
        } else {
            html.add(new EmptyPanel("footer-container").setVisible(false));
        }

        // meta description
        html.add(new WebMarkupContainer("meta_description").setVisible(false));

        // add google meta tags
        html.add(new WebMarkupContainer("meta_google_signin_scope"));
        html.add(new WebMarkupContainer("meta_google_signin_client_id")
                .add(new AttributeModifier("content", WicketApplication.get().getGoogleSigninClientId())));

        // navbar
        addNavigationBar(html, "navbar");

        // breadcrumb
        addBreadcrumb(html, "breadcrumb");

        // statusbar
        addStatusBar(html, "statusbar", "statusbarcontent");
    }

    protected void addNavigationBar(MarkupContainer html, String id) {
        addNavigationBar(html, id, new ArrayList<>());
    }

    protected void addNavigationBar(MarkupContainer html, String id, List<NavBarLink> navs) {
        WebMarkupContainer navbar = new WebMarkupContainer(id);

        ExternalLink navbarbrandlink = new ExternalLink("navbarbrandlink", "#");
        navbarbrandlink.add(new WebMarkupContainer("navbarbrandicon"));
        navbarbrandlink.add(new Label("navbarbrandlabel", ""));
        navbarbrandlink.setVisible(false);
        navbar.add(navbarbrandlink);

        navbar.add(new ListView<NavBarLink>("navbaritems", navs) {
            @Override
            protected void populateItem(ListItem<NavBarLink> item) {
                // item.add(new CssClassNameAppender("active"));
                NavBarLink main = item.getModelObject();
                BookmarkablePageLink<String> link = new BookmarkablePageLink<String>("navbaritemlink", main.getInternalPage(),
                        main.getInternalPageParameters());
                WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbaritemicon");
                if (StringUtils.isNotBlank(main.getIcon())) {
                    navbaritemicon.add(new CssClassNameModifier(main.getIcon()));
                }
                link.add(navbaritemicon);
                link.add(new Label("navbaritemlabel", main.getLabel()));
                // link.add(new CssClassNameAppender("disabled"));
                item.add(link);

                WebMarkupContainer navbardropdown = new WebMarkupContainer("navbardropdown");
                if (main.getChildLinks().isEmpty()) {
                    item.add(new CssClassNameRemover("dropdown"));
                    link.add(new CssClassNameRemover("dropdown-toggle"));
                    link.add(new AttributeModifier("data-toggle", null));
                    link.add(new AttributeModifier("aria-haspopup", null));
                    link.add(new AttributeModifier("aria-expanded", null));
                    navbardropdown.setVisible(false);
                }
                item.add(navbardropdown);
                navbardropdown.add(new ListView<NavBarLink>("navbardropdownitems", main.getChildLinks()) {
                    @Override
                    protected void populateItem(ListItem<NavBarLink> subitem) {
                        // subitem.add(new CssClassNameAppender("active"));
                        NavBarLink sub = subitem.getModelObject();
                        BookmarkablePageLink<String> sublink = new BookmarkablePageLink<String>("navbardropdownitemlink", sub.getInternalPage(),
                                sub.getInternalPageParameters());
                        WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbardropdownitemicon");
                        if (StringUtils.isNotBlank(sub.getIcon())) {
                            navbaritemicon.add(new CssClassNameModifier(sub.getIcon()));
                        }
                        sublink.add(navbaritemicon);
                        sublink.add(new Label("navbardropdownitemlabel", sub.getLabel()));
                        // sublink.add(new CssClassNameAppender("disabled"));
                        subitem.add(sublink);
                    }
                });
            }
        });

        navbar.add(new WebMarkupContainer("searchbar").setVisible(false));

        html.add(navbar);
    }

    public static class NavBarLink implements Serializable {
        String externalURL;

        Class<? extends WebPage> internalPage;

        PageParameters internalPageParameters;

        List<NavBarLink> childLinks = new ArrayList<>();

        String icon;

        String label;

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

    protected WebMarkupContainer addBreadcrumb(MarkupContainer html, String id) {
        WebMarkupContainer breadcrumb = new WebMarkupContainer(id);
        html.add(breadcrumb);
        return breadcrumb;
    }

    protected WebMarkupContainer addStatusBar(MarkupContainer html, String id, String contentid) {
        WebMarkupContainer content = new WebMarkupContainer(contentid);
        html.add(new WebMarkupContainer(id).add(content));
        return content;
    }

    abstract protected void addComponents(MarkupContainer html);

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }

        response.render(OnDomReadyHeaderItem.forScript(";var " + MomentJs.PROP_CURRENT_LANGUAGE + "='" + getLocale().getLanguage() + "';"));

        response.render(JavaScriptHeaderItem.forReference(JQuery.getJQueryReference()/* JQuery3.JS */));

        response.render(CssHeaderItem.forReference(Bootstrap4.CSS));
        response.render(CssHeaderItem.forReference(Bootstrap4.CSS_GRID));
        response.render(CssHeaderItem.forReference(Bootstrap4.CSS_REBOOT));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_POPPER));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS));
        response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_IE10FIX));
        response.render(Bootstrap4.FACTORY);

        response.render(CssHeaderItem.forReference(FontAwesome.CSS));

        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
        // response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_SOCIAL_CSS));
        // response.render(CssHeaderItem.forReference(WeLoveIcons.SOCIAL_COLORS_CSS));
        // response.render(CssHeaderItem.forReference(WeLoveIcons.SOCIAL_COLORS_HOVER_CSS));

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

        response.render(CssHeaderItem.forReference(BLazy.CSS));
        response.render(JavaScriptHeaderItem.forReference(BLazy.JS));
        response.render(BLazy.FACTORY);

        response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_JQUERY));
        response.render(BootstrapTinyMCE.FACTORY);

        response.render(CssHeaderItem.forReference(BootstrapSlider.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapSlider.JS));

        response.render(CssHeaderItem.forReference(BootstrapTags.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTags.JS));

        response.render(CssHeaderItem.forReference(new CssResourceReference(DefaultNewWebPage.class, "DefaultNewWebPage.css")));
        // response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(DefaultNewWebPage.class, "DefaultNewWebPage.js")));
        // response.render(OnDomReadyHeaderItem.forScript(new FilePath(DefaultNewWebPage.class, "DefaultNewWebPage-factory.js").readAll()));
    }
}
