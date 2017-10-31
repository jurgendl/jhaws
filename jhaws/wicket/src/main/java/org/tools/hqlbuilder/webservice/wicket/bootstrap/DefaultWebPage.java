package org.tools.hqlbuilder.webservice.wicket.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.bootstrap4.colorpicker.BootstrapColorPicker;
import org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus.BootstrapTempusDominusDateTimePicker;
import org.tools.hqlbuilder.webservice.bootstrap4.multiselect.MultiSelect;
import org.tools.hqlbuilder.webservice.bootstrap4.slider.BootstrapSlider;
import org.tools.hqlbuilder.webservice.bootstrap4.tags.BootstrapTags;
import org.tools.hqlbuilder.webservice.bootstrap4.tinymce.BootstrapTinyMCE;
import org.tools.hqlbuilder.webservice.jquery.ui.blazy.BLazy;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.jquery.ui.picturefill.PictureFill;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome.FontAwesome;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.components.ExternalLink;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameModifier;

@SuppressWarnings("serial")
public abstract class DefaultWebPage extends WebPage {
    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        // // html language
        // WebMarkupContainer html = new WebMarkupContainer("html");
        // add(html.add(new AttributeModifier("lang",
        // getSession().getLocale().getLanguage())));
        addDefaultComponents(parameters, this);
        addComponents(parameters, this);
    }

    protected void addDefaultComponents(PageParameters parameters, MarkupContainer html) {
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
        html.add(new CheckCookiesEnabled());

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
        addNavigationBar(html, id, new ArrayList<>(), false, false, false);
    }

    protected void addNavigationBar(MarkupContainer html, String id, List<NavBarLink> navs, boolean userButton, boolean searchBar,
            boolean backToTopButton) {
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
                    navbardropdown.setVisible(false);
                } else {
                    item.add(new CssClassNameAppender("dropdown"));
                    link.add(new CssClassNameAppender("dropdown-toggle"));
                    link.add(new AttributeModifier("data-toggle", "dropdown"));
                    link.add(new AttributeModifier("aria-haspopup", "true"));
                    link.add(new AttributeModifier("aria-expanded", "false"));
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

        // login/logout/user-config menu
        navbar.add(new Button("userButton").setVisible(userButton));

        // searchbar
        navbar.add(new WebMarkupContainer("searchbar").setVisible(searchBar));

        // back to top button
        navbar.add(new Button("backToTopButton").setVisible(backToTopButton));

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

    protected Component addBreadcrumb(MarkupContainer html, String id) {
        WebMarkupContainer breadcrumb = new WebMarkupContainer(id);
        html.add(breadcrumb);
        return breadcrumb;
    }

    protected WebComponent addStatusBar(MarkupContainer html, String id, String contentid) {
        Label content = new Label(contentid, " ");
        html.add(new WebMarkupContainer(id).add(content));
        return content;
    }

    abstract protected void addComponents(PageParameters parameters, MarkupContainer html);

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

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

        response.render(CssHeaderItem.forReference(BLazy.CSS));
        response.render(JavaScriptHeaderItem.forReference(BLazy.JS));
        response.render(BLazy.FACTORY);

        response.render(JavaScriptHeaderItem.forReference(PictureFill.JS));
        response.render(PictureFill.FACTORY);

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

        response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTinyMCE.JS_JQUERY));
        response.render(BootstrapTinyMCE.FACTORY);

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

        response.render(CssHeaderItem.forReference(new CssResourceReference(DefaultWebPage.class, "DefaultWebPage.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(DefaultWebPage.class, "DefaultWebPage.js")));
        response.render(OnDomReadyHeaderItem.forScript(new FilePath(DefaultWebPage.class, "DefaultWebPage-factory.js").readAll()));
    }
}
