package org.tools.hqlbuilder.webservice.wicket;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class DefaultWebPage extends WebPage {
    protected transient final Logger logger;

    /** default cache duration when deployed: 1 day */
    protected Duration defaultCacheDuration = Duration.ONE_DAY;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        this.logger = LoggerFactory.getLogger(this.getClass());
        Injector.get().inject(this);
        // if (getClass().equals(DefaultWebPage.class)) {
        // setResponsePage(EmptyPage.class, parameters);
        // }
        this.addComponents();
    }

    protected void addComponents() {
        // title
        add(new Label("page.title", getString("page.title")));

        // shortcut icon
        this.add(new WebMarkupContainer("shortcutIcon").add(new AttributeModifier("href", Model.of(WicketApplication.get().getShortcutIcon())))
                .setVisible(StringUtils.isNotBlank(WicketApplication.get().getShortcutIcon())));

        // wicket/ajax debug bars
        this.add(WicketApplication.get().isShowDebugbars() && WicketApplication.get().usesDevelopmentConfig() ? new DebugBar("debug")
                : new EmptyPanel("debug").setVisible(false));

        // check if javascript is enabled
        this.add(new CheckJavaScriptEnabled());

        // check if cookies are enabled
        this.add(new CheckCookiesEnabled());

        // check if ads are not blocked
        try {
            this.add(new CheckAdsEnabled());
        } catch (Exception ex) {
            this.add(new EmptyPanel("check.ads.enabled").setVisible(false));
        }

        // add header response (javascript) down below on page
        if (WicketApplication.get().isJavascriptAtBottom()) {
            this.add(new HeaderResponseContainer("footer-container", "footer-bucket"));
        } else {
            this.add(new EmptyPanel("footer-container").setVisible(false));
        }

        // meta description
        add(new WebMarkupContainer("meta_description").setVisible(false));

        // add google meta tags
        add(new WebMarkupContainer("meta_google_signin_scope"));
        add(new WebMarkupContainer("meta_google_signin_client_id")
                .add(new AttributeModifier("content", WicketApplication.get().getGoogleSigninClientId())));
    }

    protected void addDefaultResources(IHeaderResponse response) {
        // none by default
    }

    protected void addDynamicResources(IHeaderResponse response) {
        // none by default
    }

    protected void addPageResources(IHeaderResponse response) {
        // none by default
    }

    protected void addThemeResources(IHeaderResponse response) {
        // none by default
    }

    protected void addUserResources(IHeaderResponse response) {
        // none by default
    }

    protected void disableCaching(WebResponse response) {
        response.disableCaching();
    }

    protected void enableCaching(WebResponse response) {
        response.enableCaching(this.defaultCacheDuration, WebResponse.CacheScope.PUBLIC);
    }

    public Duration getDefaultCacheDuration() {
        return this.defaultCacheDuration;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.statelessCheck();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        this.addDefaultResources(response);
        this.addPageResources(response);
        this.addUserResources(response);
        this.addDynamicResources(response);
        this.addThemeResources(response);
    }

    public void setDefaultCacheDuration(Duration defaultCacheDuration) {
        this.defaultCacheDuration = defaultCacheDuration;
    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);

        // if page is stateless en in deployment mode, we enable caching
        if (this.isPageStateless() && WicketApplication.get().usesDeploymentConfig()) {
            this.enableCaching(response);
        } else {
            this.disableCaching(response);
        }
    }

    protected void statelessCheck() {
        if (this.getStatelessHint() && WicketApplication.get().usesDevelopmentConfig()) {
            this.visitChildren((component, arg1) -> {
                DefaultWebPage.this.logger.trace("Component " + component.getClass().getName() + " with id " + component.getId());
                if (!component.isStateless()) {
                    DefaultWebPage.this.logger
                            .warn("Component " + component.getClass().getName() + " with id " + component.getId() + " is not stateless");
                }
            });
        }
    }
}