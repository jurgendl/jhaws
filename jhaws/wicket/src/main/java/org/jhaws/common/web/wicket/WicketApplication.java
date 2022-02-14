package org.jhaws.common.web.wicket;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.ISessionListener;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.csp.CSPHeaderConfiguration;
import org.apache.wicket.csp.ContentSecurityPolicySettings;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.ResourceIsolationRequestCycleListener;
import org.apache.wicket.protocol.http.ResourceIsolationRequestCycleListener.CsrfAction;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.apache.wicket.settings.DebugSettings;
import org.apache.wicket.settings.ExceptionSettings;
import org.apache.wicket.settings.MarkupSettings;
import org.apache.wicket.settings.RequestLoggerSettings;
import org.apache.wicket.settings.ResourceSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Filters.AudioFilter;
import org.jhaws.common.io.FilePath.Filters.ImageFilter;
import org.jhaws.common.io.FilePath.Filters.VideoFilter;
import org.jhaws.common.web.resteasy.CustomObjectMapper;
import org.jhaws.common.web.wicket.css.WicketCSSRoot;
import org.jhaws.common.web.wicket.forms.bootstrap.TextFieldPanel;
import org.jhaws.common.web.wicket.icons.WicketIconsRoot;
import org.jhaws.common.web.wicket.js.GoogleLogin;
import org.jhaws.common.web.wicket.js.WicketJSRoot;
import org.jhaws.common.web.wicket.moment.MomentJs;
import org.jhaws.common.web.wicket.settings.WicketAppSettings;
import org.jhaws.common.web.wicket.tinymce.BootstrapTinyMCE;
import org.jhaws.common.web.wicket.waypoints.Waypoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.htmlcompressor.HtmlCompressingMarkupFactory;
import org.wicketstuff.logback.ConfiguratorPage;

import com.fasterxml.jackson.databind.ObjectMapper;

// getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
// getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class);
// getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
// getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
public class WicketApplication extends /* AuthenticatedWebApplication */ WebApplication implements InitializingBean {
    public final class DateTimeConverter extends DateConverter {
        private static final long serialVersionUID = -6075171947424780395L;

        @Override
        public DateFormat getDateFormat(Locale locale) {
            return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        }
    }

    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
    }

    protected static final Logger logger = LoggerFactory.getLogger(WicketApplication.class);

    @SpringBean(name = "pagesPackages", required = false)
    protected String pagesPackages = "org.jhaws.common.web.wicket.pages";

    @SpringBean(name = "webProperties", required = false)
    protected transient Properties webProperties;

    @SpringBean(name = "webSettingsFile", required = false)
    protected String webSettingsFile = "";

    public WicketSession createSession(Request request, Response response) {
        return new WicketSession(request);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return org.jhaws.common.web.wicket.bootstrap.DefaultWebPage.class;
    }

    public Properties getWebProperties() {
        return this.webProperties;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        WicketAppSettings s = getSettings();
        boolean deployed = this.usesDeploymentConfig();
        springInjection();
        gatherBrowserInfo(s.isGatherBrowserInfo());
        markupSettings(getMarkupSettings(), deployed);
        requestLoggerSettings(getRequestLoggerSettings(), deployed);
        debugSettings(s, getDebugSettings(), deployed);
        resourceSettings(s, getResourceSettings(), deployed);
        javascriptAtBottom(WicketApplication.get().getSettings().isJavascriptAtBottom());
        initStore();
        statelessChecker(deployed);
        csp(enableCSP);
        csrf(enableCsrf);
        addBundles();
        mountImages();
        mountResources();
        mountPages();
        exceptionHandling(deployed);
        packageResourceGuard();
        sessionListener();
    }

    protected void markupSettings(MarkupSettings markupSettings, boolean deployed) {
        // markup settings
        markupSettings.setStripComments(deployed);

        markupSettings.setCompressWhitespace(deployed);

        // breaks layout if not on
        markupSettings.setStripWicketTags(true);

        if (deployed) {
            markupSettings.setMarkupFactory(new HtmlCompressingMarkupFactory());
        }
    }

    protected void requestLoggerSettings(RequestLoggerSettings requestLoggerSettings, boolean deployed) {
        // request logger settings
        requestLoggerSettings.setRecordSessionSize(!deployed);
        requestLoggerSettings.setRequestLoggerEnabled(!deployed);
    }

    protected void resourceSettings(WicketAppSettings s, ResourceSettings resourceSettings, boolean deployed) {
        // resource settings
        resourceSettings.setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
        resourceSettings.setUseMinifiedResources(deployed);
        resourceSettings.setEncodeJSessionId(deployed);
        resourceSettings.setDefaultCacheDuration(s.getCacheDuration() != null ? (Duration.ofSeconds(s.getCacheDuration())) : (!deployed ? Duration.ZERO : WebResponse.MAX_CACHE_DURATION));
        if (deployed) {
            // // minify your resources on deploy
            // getResourceSettings()
            // .setJavaScriptCompressor(new
            // GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
            // getResourceSettings().setCssCompressor(new
            // de.agilecoders.wicket.extensions.javascript.YuiCssCompressor());
        }
    }

    protected void debugSettings(WicketAppSettings s, DebugSettings debugSettings, boolean deployed) {
        // debug settings
        debugSettings.setAjaxDebugModeEnabled(s.isShowDebugbars() && !deployed);
        debugSettings.setComponentUseCheck(!deployed);
        debugSettings.setDevelopmentUtilitiesEnabled(!deployed);
        debugSettings.setOutputMarkupContainerClassName(!deployed);
        debugSettings.setDevelopmentUtilitiesEnabled(!deployed);
        // getDebugSettings().setOutputComponentPath(!deployed);
    }

    protected void sessionListener() {
        ISessionListener sl = new ISessionListener() {
            @Override
            public void onCreated(Session session) {
                logger.trace("new session created {}", session);
            }

            @Override
            public void onUnbound(String sessionId) {
                //
            }
        };
        getSessionListeners().add(sl);
    }

    protected void javascriptAtBottom(boolean javascriptAtBottom) {
        // http://tomaszdziurko.com/2017/02/forcing-wicket-place-javascript-files-bottom/
        // to put javascript down on the page (DefaultWebPage.html must contain
        // wicket:id='footer-bucket'
        if (false && javascriptAtBottom) {
            this.setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("footer-bucket"));
        }
    }

    protected void gatherBrowserInfo(boolean gatherBrowserInfo) {
        // gather browser info
        if (gatherBrowserInfo) {
            this.getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
            // =>
            // ((WebClientInfo)WebRequestCycle.get().getClientInfo()).getProperties().isJavaEnabled()
        }
    }

    protected void springInjection() {
        // spring injector
        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        Injector.get().inject(this);
    }

    protected void packageResourceGuard() {
        // http://wicketguide.comsysto.com/guide/chapter19.html#chapter19_4
        IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
            guard.addPattern("+*.scss");
            guard.addPattern("+*.less");
            new ImageFilter().getExt().forEach(ext -> guard.addPattern("+*." + ext));
            new VideoFilter().getExt().forEach(ext -> guard.addPattern("+*." + ext));
            new AudioFilter().getExt().forEach(ext -> guard.addPattern("+*." + ext));
            guard.addPattern("+*.map");
            guard.addPattern("+*.tag");
            guard.addPattern("+*.woff2");
        }
    }

    protected void exceptionHandling(boolean deployed) {
        this.getExceptionSettings().setUnexpectedExceptionDisplay(!deployed ? ExceptionSettings.SHOW_EXCEPTION_PAGE : ExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
        /* getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class) ; getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class );
         * getApplicationSettings().setInternalErrorPage(MyInternalErrorPage. class); */
    }

    protected void statelessChecker(boolean deployed) {
        // stateless checker
        if (!deployed) {
            this.getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
        }
    }

    protected boolean enableCsrf = false;

    protected void csrf(boolean enableCsrf) {
        if (enableCsrf) {
            // https://ci.apache.org/projects/wicket/apidocs/9.x/org/apache/wicket/protocol/http/CsrfPreventionRequestCycleListener.html
            // CsrfPreventionRequestCycleListener
            // csrfPreventionRequestCycleListener = new
            // CsrfPreventionRequestCycleListener();
            // if (csrfAddLocalAsAcceptedOrigin) {
            // csrfPreventionRequestCycleListener.addAcceptedOrigin("local.host.com");
            // }
            // getRequestCycleListeners().add(csrfPreventionRequestCycleListener);

            // https://stackoverflow.com/questions/64675854/apache-wicket-9-1-csrf-enabled-formtester-submits-blocked-by-resource-isolation/64688636#64688636
            ResourceIsolationRequestCycleListener resourceIsolationRequestCycleListener = new ResourceIsolationRequestCycleListener();
            resourceIsolationRequestCycleListener.setDisallowedOutcomeAction(CsrfAction.ABORT);
            resourceIsolationRequestCycleListener.setUnknownOutcomeAction(CsrfAction.ABORT);
            // resourceIsolationRequestCycleListener.addExemptedPaths("");
            getRequestCycleListeners().add(resourceIsolationRequestCycleListener);
        }
    }

    protected void addBundles() {
        this.getResourceBundles().addJavaScriptBundle(WicketJSRoot.class, "tinymce-bundle.js", new JavaScriptResourceReference[] { //
                BootstrapTinyMCE.JS, //
                BootstrapTinyMCE.JS_JQUERY, //
                BootstrapTinyMCE.JS_PLUGIN_LINK, //
                BootstrapTinyMCE.JS_PLUGIN_CODE, //
                BootstrapTinyMCE.JS_PLUGIN_LISTS, //
                BootstrapTinyMCE.JS_PLUGIN_ADVLIST, //
                BootstrapTinyMCE.JS_PLUGIN_AUTOLINK, //
                BootstrapTinyMCE.JS_PLUGIN_PRINT, //
                BootstrapTinyMCE.JS_PLUGIN_SEARCHREPLACE, //
                BootstrapTinyMCE.JS_PLUGIN_TABLE, //
                BootstrapTinyMCE.JS_PLUGIN_VISUALCHARS, //
                BootstrapTinyMCE.JS_PLUGIN_PASTE, //
                BootstrapTinyMCE.JS_PLUGIN_WORDCOUNT, //
                BootstrapTinyMCE.JS_PLUGIN_CHARMAP, //
                BootstrapTinyMCE.JS_PLUGIN_ANCHOR, //
                BootstrapTinyMCE.JS_PLUGIN_TEXTCOLOR, //
                BootstrapTinyMCE.JS_PLUGIN_COLORPICKER, //
                BootstrapTinyMCE.JS_PLUGIN_MEDIA, //
                BootstrapTinyMCE.JS_PLUGIN_HR, //
                BootstrapTinyMCE.JS_PLUGIN_IMAGE, //
                BootstrapTinyMCE.JS_PLUGIN_INSERTDATETIME, //
                BootstrapTinyMCE.JS_PLUGIN_HELP, //
                BootstrapTinyMCE.JS_PLUGIN_PREVIEW//
        });

        this.getResourceBundles().addJavaScriptBundle(WicketJSRoot.class, "waypoints-bundle.js", new JavaScriptResourceReference[] { //
                Waypoints.JS, Waypoints.JS_INFINITE, Waypoints.JS_INVIEW, Waypoints.JS_STICKY//
        });

        this.getResourceBundles().addJavaScriptBundle(WicketJSRoot.class, "momentjs-bundle.js", new JavaScriptResourceReference[] { //
                MomentJs.JS, MomentJs.JS_I18N, MomentJs.JS_PLUGIN_PRECISE_RANGE, MomentJs.JS_LOCALE//
        });
    }

    protected void initStore() {
        // WICKET UPDATE
        // WicketAppSettings s = getSettings();
        // if (this.usesDevelopmentConfig()) {
        // if (!s.isDiskStore()) {
        // this.setPageManagerProvider(new DefaultPageManagerProvider(this) {
        // @Override
        // protected IDataStore newDataStore() {
        // return new HttpSessionDataStore(WicketApplication.this.getPageManagerContext(),
        // new PageNumberEvictionStrategy(20));
        // }
        // });
        // } else {
        // DebugDiskDataStore.register(this);
        // }
        // } else {
        // if (!s.isDiskStore()) {
        // this.setPageManagerProvider(new DefaultPageManagerProvider(this) {
        // @Override
        // protected IDataStore newDataStore() {
        // return new HttpSessionDataStore(WicketApplication.this.getPageManagerContext(),
        // new PageNumberEvictionStrategy(20));
        // }
        // });
        // } // no else
        // }
    }

    protected void mountImages() {
        String cssImages = "css/images/";
        for (Field field : WicketIconsRoot.class.getFields()) {
            try {
                final String name = String.valueOf(field.get(WicketIconsRoot.class));
                PackageResourceReference reference = new VirtualPackageResourceReference(WicketCSSRoot.class, cssImages + name, WicketIconsRoot.class, name);
                this.getSharedResources().add(cssImages + name, reference.getResource());
                this.mountResource(cssImages + name, reference);
                WicketApplication.logger.info("mounting image: " + WicketRoot.class.getCanonicalName() + ": " + cssImages + name);
            } catch (Exception ex) {
                WicketApplication.logger.error("{}", ex);
            }
        }
    }

    protected void mountPages() {
        // mountPage("login", LoginPage.class);
        // mountPage("user", UserPage.class);

        // https://github.com/wicketstuff/core/wiki/Logback
        // also see web.xml and logback.xml
        this.mountPage("logback", ConfiguratorPage.class);

        new AnnotatedMountScanner().scanPackage(this.pagesPackages).mount(this);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        final AnnotationTypeFilter mountedPageFilter = new AnnotationTypeFilter(MountedPage.class);
        final AssignableTypeFilter webPageFilter = new AssignableTypeFilter(WebPage.class);
        TypeFilter TypeFilter = (metadataReader, metadataReaderFactory) -> mountedPageFilter.match(metadataReader, metadataReaderFactory) && webPageFilter.match(metadataReader, metadataReaderFactory);
        scanner.addIncludeFilter(TypeFilter);
        List<String> paths = new ArrayList<>();
        for (BeanDefinition bd : scanner.findCandidateComponents(WicketRoot.class.getPackage().getName())) {
            try {
                String className = bd.getBeanClassName();
                WicketApplication.logger.info("mounting page " + className);
                @SuppressWarnings("unchecked")
                Class<WebPage> pageClass = (Class<WebPage>) Class.forName(className);
                MountedPage mountedPage = pageClass.getAnnotation(MountedPage.class);
                String path = mountedPage.value();
                boolean doMount = true;
                if (path.startsWith("${") && path.endsWith("}")) {
                    if (this.webProperties != null) {
                        path = this.webProperties.getProperty(path.substring(2, path.length() - 1));
                    } else {
                        doMount = false;
                    }
                }
                if (paths.contains(path)) {
                    throw new IllegalArgumentException("mounting multiple pages on the same path " + path);
                }
                paths.add(path);
                WicketApplication.logger.info("on path " + path);
                if (doMount) {
                    this.mountPage(path, pageClass);
                }
            } catch (ClassNotFoundException ex) {
                WicketApplication.logger.error("{}", ex);
            }
        }
    }

    protected void mountResources() {
        this.mountResource(CheckAdsEnabled.IMG_NAME, CheckAdsEnabled.IMG);
    }

    /**
     * @see org.apache.wicket.Application#newConverterLocator()
     */
    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = new ConverterLocator();
        DateTimeConverter dateConverter = new DateTimeConverter();
        locator.set(java.sql.Date.class, dateConverter);
        locator.set(java.util.Date.class, dateConverter);
        locator.set(java.sql.Timestamp.class, dateConverter);
        return locator;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        WicketSession wicketSession = this.createSession(request, response);
        WicketApplication.logger.trace("creating new session {}", wicketSession);
        ((ServletWebRequest) request).getContainerRequest().getSession().setMaxInactiveInterval(60 * 60); // 1h
        return wicketSession;
    }

    public void setWebProperties(Properties webProperties) {
        this.webProperties = webProperties;
    }

    protected static transient WebApplicationContext webApplicationContext;

    protected static String applicationName;

    public static String getApplicationName() {
        if (applicationName == null) {
            applicationName = getWebApplicationContext().getApplicationName();
        }
        return applicationName;
    }

    public static WebApplicationContext getWebApplicationContext() {
        if (webApplicationContext == null) webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(get().getServletContext());
        return webApplicationContext;
    }

    public String getWebSettingsFile() {
        return this.webSettingsFile;
    }

    public void setWebSettingsFile(String webSettingsFile) {
        this.webSettingsFile = webSettingsFile;
    }

    protected WicketAppSettings settings;

    @Override
    public void afterPropertiesSet() throws Exception {
        getSettings();
    }

    public WicketAppSettings getSettings() {
        if (settings == null) {
            if (webSettingsFile != null) {
                FilePath f = new FilePath(webSettingsFile.replace("{user.home}", System.getProperty("user.home")));
                if (f.exists()) {
                    try {
                        settings = WicketAppSettings.JAXB.unmarshall(f.toPath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        if (settings == null) {
            settings = new WicketAppSettings();
            GoogleLogin.init(settings);
            setSettings(settings);
            settings.setCacheDuration(0);
            settings.setShowDebugbars(false);
        }
        return this.settings;
    }

    public void setSettings(WicketAppSettings settings) {
        this.settings = settings;
        if (webSettingsFile != null) {
            FilePath f = new FilePath(webSettingsFile.replace("{user.home}", System.getProperty("user.home")));
            try {
                WicketAppSettings.JAXB.marshall(settings, f.toPath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String restRelativePath = "/rest";

    public static String getRestPath() {
        return WicketApplication.getApplicationName() + WicketApplication.get().getRestRelativePath();
    }

    public String getRestRelativePath() {
        return this.restRelativePath;
    }

    public void setRestRelativePath(String restRelativePath) {
        this.restRelativePath = restRelativePath;
    }

    @Override
    public <T extends Page> MountedMapper mountPage(String path, Class<T> pageClass) {
        MountedMapper page = super.mountPage(path, pageClass);
        logger.info("mounting {} on {}", pageClass.getName(), path);
        return page;
    }

    // @Override
    // protected Class<? extends AbstractAuthenticatedWebSession>
    // getWebSessionClass() {
    // return SecureWebSession.class;
    // }
    //
    // @Override
    // protected Class<? extends WebPage> getSignInPageClass() {
    // return LoginPage.class;
    // }

    protected boolean enableCSP = false;

    public void csp(boolean enableCSP) {
        if (enableCSP) {
            ContentSecurityPolicySettings cspSettings = getCspSettings();
            CSPHeaderConfiguration cfg = cspSettings.blocking().clear();
            csp(cfg);
            cspSettings.enforce(this);
        } else {
            getCspSettings().blocking().disabled();
        }
    }

    public void csp(CSPHeaderConfiguration cfg) {
        cfg//
           // ======================================================================
           // .add(CSPDirective.DEFAULT_SRC, CSPDirectiveSrcValue.SELF)//
           //
           // disabled: .add(CSPDirective.SCRIPT_SRC,
           // CSPDirectiveSrcValue.SELF,
           // CSPDirectiveSrcValue.UNSAFE_INLINE,
           // CSPDirectiveSrcValue.UNSAFE_EVAL)//
           // strict: .add(CSPDirective.SCRIPT_SRC,
           // CSPDirectiveSrcValue.STRICT_DYNAMIC,
           // CSPDirectiveSrcValue.NONCE)
           // .add(CSPDirective.SCRIPT_SRC,
           // CSPDirectiveSrcValue.STRICT_DYNAMIC,
           // CSPDirectiveSrcValue.UNSAFE_INLINE,
           // CSPDirectiveSrcValue.NONCE)
                .add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.SELF, /* CSPDirectiveSrcValue . UNSAFE_INLINE, *///
                        CSPDirectiveSrcValue.UNSAFE_EVAL, //
                        CSPDirectiveSrcValue.NONCE)
                //
                .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF, CSPDirectiveSrcValue.UNSAFE_INLINE)//
                // .add(CSPDirective.IMG_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.CONNECT_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.FONT_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.MANIFEST_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.CHILD_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.CHILD_SRC, CSP.CSPDirectiveSrcValue_BLOB)//
                .add(CSPDirective.BASE_URI, CSPDirectiveSrcValue.SELF)//
                // ======================================================================
                // .add(CSPDirective.IMG_SRC, "data:")//
                // ======================================================================
                // // Google tracking
                // (be.ugent.gismo.researchweb.wicket.TrackerConfig)
                .add(CSPDirective.SCRIPT_SRC, "https://www.googletagmanager.com")//
                .add(CSPDirective.SCRIPT_SRC, "https://www.google-analytics.com")//
                .add(CSPDirective.STYLE_SRC, "https://www.googletagmanager.com")//
                .add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com")//
                // .add(CSPDirective.IMG_SRC, "https://ssl.gstatic.com")//
                // .add(CSPDirective.IMG_SRC, "https://www.gstatic.com")//
                // .add(CSPDirective.IMG_SRC,
                // "https://www.google-analytics.com")//
                .add(CSPDirective.FONT_SRC, "https://fonts.gstatic.com")//
                .add(CSPDirective.FONT_SRC, "data:")//
                .add(CSPDirective.CONNECT_SRC, "https://www.google-analytics.com/")//
                // //
                // ======================================================================
                // // Google Recaptcha (overlaps with Google tracking)
                .add(CSPDirective.SCRIPT_SRC, "https://www.google.com")//
                .add(CSPDirective.SCRIPT_SRC, "https://www.gstatic.com")//
                .add(CSPDirective.FRAME_SRC, "https://www.google.com")//
        ;

        cfg.add(CSPDirective.SCRIPT_SRC, CSP.CSPDirectiveSrcValue_UNSAFE_HASHES);
        try {
            cfg.add(CSPDirective.SCRIPT_SRC, CSP.cspSha256(TextFieldPanel.ONCLICK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            cfg.add(CSPDirective.SCRIPT_SRC, CSP.cspSha256(TextFieldPanel.ONDROP));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            cfg.add(CSPDirective.SCRIPT_SRC, CSP.CSPDirectiveSrcValue_EMPTYJS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            cfg.add(CSPDirective.SCRIPT_SRC, CSP.cspSha256("topFunction()"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SpringBean(name = "objectMapper", required = false)
    protected ObjectMapper objectMapper;

    public synchronized ObjectMapper getObjectMapper() {
        if (objectMapper == null) objectMapper = new CustomObjectMapper();
        return this.objectMapper;
    }
}