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
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.bootstrap4.bootbox.BootBox;
import org.tools.hqlbuilder.webservice.bootstrap4.clipboardjs.ClipboardJs;
import org.tools.hqlbuilder.webservice.bootstrap4.colorpicker.BootstrapColorPicker;
import org.tools.hqlbuilder.webservice.bootstrap4.customfileinput.CustomFileInput;
import org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus.BootstrapTempusDominusDateTimePicker;
import org.tools.hqlbuilder.webservice.bootstrap4.multiselect.MultiSelect;
import org.tools.hqlbuilder.webservice.bootstrap4.popoverx.PopoverX;
import org.tools.hqlbuilder.webservice.bootstrap4.slider.BootstrapSlider;
import org.tools.hqlbuilder.webservice.bootstrap4.tags.BootstrapTags;
import org.tools.hqlbuilder.webservice.bootstrap4.tinymce.BootstrapTinyMCE;
import org.tools.hqlbuilder.webservice.bootstrap4.toast.BootstrapToasts;
import org.tools.hqlbuilder.webservice.jquery.ui.blazy.BLazy;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.jquery.ui.magnify.Magnify;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.jquery.ui.picturefill.PictureFill;
import org.tools.hqlbuilder.webservice.jquery.ui.qtip.QTip;
import org.tools.hqlbuilder.webservice.jquery.ui.spin.Spin;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome.FontAwesome;
import org.tools.hqlbuilder.webservice.prismjs.PrismJs;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.components.ExternalLink;

@SuppressWarnings("serial")
public abstract class DefaultWebPage extends WebPage {
	protected static final String FACTORY = new FilePath(WicketRoot.class, "wicket/bootstrap/base-factory.js")
			.readAll();

	protected static final JavaScriptResourceReference JS = new JavaScriptResourceReference(WicketRoot.class,
			"wicket/bootstrap/base.js");

	protected static final CssResourceReference CSS = new CssResourceReference(WicketRoot.class,
			"wicket/bootstrap/base.css");

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

	protected String getLanguageIsocode() {
		return getSession().getLocale().toString();
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
	 * "Open Graph Reference Documentation _ og_type.pdf"
	 * https://developers.facebook.com/docs/reference/opengraph/
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
		html.add(new WebMarkupContainer("meta_google_signin_scope")
				.setVisible(StringUtils.isNotBlank(googleSigninClientId)));
		html.add(new WebMarkupContainer("meta_google_signin_client_id")
				.add(new AttributeModifier("content", googleSigninClientId))
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
		html.add(WicketApplication.get().getSettings().isShowDebugbars()
				&& WicketApplication.get().usesDevelopmentConfig() ? new DebugBar(id)
						: new EmptyPanel(id).setVisible(false));
	}

	protected void addShortcutIcon(MarkupContainer html, String id) {
		html.add(new WebMarkupContainer(id)
				.add(new AttributeModifier("href", Model.of(WicketApplication.get().getSettings().getShortcutIcon())))
				.setVisible(StringUtils.isNotBlank(WicketApplication.get().getSettings().getShortcutIcon())));
	}

	protected void addSpinner(MarkupContainer html, String spinnercontainer, String spinner) {
		html.add(new WebMarkupContainer(spinnercontainer)
				.add(new WebMarkupContainer(spinner).add(AttributeModifier.replace("class",
						"loader loader-" + WicketApplication.get().getSettings().getSpinner() + " is-active"))));
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
				pageKeywords.add(new AttributeModifier("content",
						Arrays.stream(getPageKeywords(parameters)).collect(Collectors.joining(","))));
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

	@SuppressWarnings("unchecked")
	protected void addNavigationBar(PageParameters parameters, MarkupContainer html, String id) {
		List<NavBarLink> navs = new ArrayList<>();
		navs.add(new NavBarLink("Home", "fa-fw fas fa-home", WicketApplication.get().getHomePage(), null));
		demos(navs);
		navs.add(new NavBarLink("Refresh", "fa-fw fas fa-sync-alt", (Class<? extends WebPage>) getPageClass(),
				getPageParameters()));
		addNavigationBar(parameters, html, id, navs, false, false, false);
	}

	protected void demos(List<NavBarLink> navs) {
		NavBarLink demos = new NavBarLink("Demo", null, null, null);
		demos.getChildLinks().add(new NavBarLink("settings", "",
				org.tools.hqlbuilder.webservice.wicket.settings.SettingsPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("overview", "", org.tools.hqlbuilder.webservice.demo.TestPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("vue", "", org.tools.hqlbuilder.webservice.demo.VueTestPage.class, null));
		demos.getChildLinks().add(
				new NavBarLink("pagination", "", org.tools.hqlbuilder.webservice.demo.PaginationTestPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("messages", "", org.tools.hqlbuilder.webservice.demo.TestMessagesPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("upload", "", org.tools.hqlbuilder.webservice.demo.UploadTestPage.class, null));
		//
		demos.getChildLinks().add(
				new NavBarLink("registration", "", org.tools.hqlbuilder.webservice.demo.RegistrationPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("login", "", org.tools.hqlbuilder.webservice.wicket.pages.LogInPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("logout", "", org.tools.hqlbuilder.webservice.wicket.pages.LogOutPage.class, null));
		demos.getChildLinks()
				.add(new NavBarLink("public", "", org.tools.hqlbuilder.webservice.wicket.pages.PublicPage.class, null));
		demos.getChildLinks().add(
				new NavBarLink("register", "", org.tools.hqlbuilder.webservice.wicket.pages.RegisterPage.class, null));
		navs.add(demos);
	}

	protected Component addNavigationBar(PageParameters parameters, MarkupContainer html, String id,
			List<NavBarLink> navs, boolean userButton, boolean searchBar, boolean backToTopButton) {
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
					link = new BookmarkablePageLink<String>("navbaritemlink", main.getInternalPage(),
							main.getInternalPageParameters());
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
						BookmarkablePageLink<String> sublink = new BookmarkablePageLink<>("navbardropdownitemlink",
								sub.getInternalPage(), sub.getInternalPageParameters());
						WebMarkupContainer navbaritemicon = new WebMarkupContainer("navbardropdownitemicon");
						if (StringUtils.isNotBlank(sub.getIcon())) {
							navbaritemicon.add(AttributeAppender.replace("class", sub.getIcon()));
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

		public NavBarLink(String label, String icon, Class<? extends WebPage> internalPage,
				PageParameters internalPageParameters) {
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
		response.render(OnDomReadyHeaderItem
				.forScript(";var " + MomentJs.PROP_CURRENT_LANGUAGE + "='" + getLocale().getLanguage() + "';"));

		response.render(JavaScriptHeaderItem.forReference(JQuery.getJQueryReference()));

		renderBootstrapTheme(response);
		response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_POPPER));
		response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS));
		response.render(JavaScriptHeaderItem.forReference(Bootstrap4.JS_IE10FIX));

		response.render(CssHeaderItem.forReference(FontAwesome.CSS5));
		// response.render(CssHeaderItem.forReference(new
		// LessResourceReference(DefaultWebPage.class,
		// "org/tools/hqlbuilder/webservice/bootstrap4/social/brand.less")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(Bootstrap4.class, "social/brand.css")));

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

		response.render(CssHeaderItem.forReference(BootstrapSlider.CSS));
		response.render(JavaScriptHeaderItem.forReference(BootstrapSlider.JS));

		// response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
		response.render(JavaScriptHeaderItem
				.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS_BLOODHOUND));
		response.render(
				JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.bootstrap4.typeahead.TypeAhead.JS));
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

		response.render(CssHeaderItem.forReference(org.tools.hqlbuilder.webservice.css.WicketCSSRoot.ANIMATE));

		response.render(CssHeaderItem.forReference(Spin.css(WicketApplication.get().getSettings().getSpinner())));

		response.render(CssHeaderItem.forReference(Magnify.CSS));
		response.render(JavaScriptHeaderItem.forReference(Magnify.JS));

		// response.render(CssHeaderItem.forReference(Pace.CSS));
		// response.render(JavaScriptHeaderItem.forReference(Pace.JS));

		response.render(CssHeaderItem.forReference(QTip.CSS));
		response.render(JavaScriptHeaderItem.forReference(QTip.JS));

		response.render(JavaScriptHeaderItem.forReference(PopoverX.JS));
		response.render(CssHeaderItem.forReference(PopoverX.CSS));

		response.render(CssHeaderItem.forReference(CSS));
		response.render(JavaScriptHeaderItem.forReference(JS));
		response.render(OnDomReadyHeaderItem.forScript(FACTORY));
	}

	/** optional, call in renderhead */
	protected void renderCodeStyling(IHeaderResponse response) {
		// response.render(CssHeaderItem.forReference(new
		// CssResourceReference(HighlightJs.class, "styles/a11y-dark.css")));
		// response.render(JavaScriptHeaderItem.forReference(HighlightJs.JS));
		response.render(
				CssHeaderItem.forReference(new CssResourceReference(PrismJs.class, "themes/prism-okaidia.css")));
		response.render(JavaScriptHeaderItem.forReference(PrismJs.JS_ALL_LANGUAGES_NO_PLUGINS));
	}

	protected void renderBootstrapTheme(IHeaderResponse response) {
		try {
			String t = WicketApplication.get().getSettings().getTheme().toString();
			if (StringUtils.isNotBlank(t) && !"default".equals(t)) {
				response.render(CssHeaderItem.forReference(Bootstrap4.theme(t)));
			} else {
				throw new NullPointerException();
			}
		} catch (Exception ex) {
			response.render(CssHeaderItem.forReference(Bootstrap4.CSS));
		}

		response.render(CssHeaderItem.forReference(Bootstrap4.MENLO));
	}
}
