package org.tools.hqlbuilder.webservice.wicket;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Session;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.bean.validation.IPropertyResolver;
import org.apache.wicket.devutils.diskstore.DebugDiskDataStore;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.apache.wicket.util.time.Duration;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Filters.AudioFilter;
import org.jhaws.common.io.FilePath.Filters.ImageFilter;
import org.jhaws.common.io.FilePath.Filters.VideoFilter;
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
import org.tools.hqlbuilder.common.icons.WicketIconsRoot;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.js.GoogleLogin;
import org.tools.hqlbuilder.webservice.wicket.settings.WicketAppSettings;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.htmlcompressor.HtmlCompressingMarkupFactory;
import org.wicketstuff.logback.ConfiguratorPage;

// getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
// getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class);
// getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
// getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
public class WicketApplication extends WebApplication implements InitializingBean {
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
    protected String pagesPackages = "org.tools.hqlbuilder.webservice.wicket.pages";

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
        return org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage.class;
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
        boolean inDevelopment = !deployed;

        // spring injector
        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        Injector.get().inject(this);

        // gather browser info
        if (s.isGatherBrowserInfo()) {
            this.getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
            // =>
            // ((WebClientInfo)WebRequestCycle.get().getClientInfo()).getProperties().isJavaEnabled()
        }

        // markup settings
        this.getMarkupSettings().setStripComments(deployed);

        this.getMarkupSettings().setCompressWhitespace(deployed);

        // breaks layout if not on
        this.getMarkupSettings().setStripWicketTags(true);

        if (deployed) {
            this.getMarkupSettings().setMarkupFactory(new HtmlCompressingMarkupFactory());
        }

        // request logger settings
        this.getRequestLoggerSettings().setRecordSessionSize(inDevelopment);
        this.getRequestLoggerSettings().setRequestLoggerEnabled(inDevelopment);

        // debug settings
        this.getDebugSettings().setAjaxDebugModeEnabled(s.isShowDebugbars() && inDevelopment);
        this.getDebugSettings().setComponentUseCheck(inDevelopment);
        this.getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
        this.getDebugSettings().setOutputMarkupContainerClassName(inDevelopment);
        this.getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
        // getDebugSettings().setOutputComponentPath(inDevelopment);

        // resource settings
        this.getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
        this.getResourceSettings().setUseMinifiedResources(deployed);
        this.getResourceSettings().setEncodeJSessionId(deployed);
        this.getResourceSettings()
                .setDefaultCacheDuration(StringUtils.isNotBlank(s.getCacheDuration())
                        ? ("none".equals(s.getCacheDuration()) ? Duration.NONE : Duration.valueOf(s.getCacheDuration()))
                        : (inDevelopment ? Duration.NONE : WebResponse.MAX_CACHE_DURATION));

        if (deployed) {
            // // minify your resources on deploy
            // getResourceSettings()
            // .setJavaScriptCompressor(new
            // GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
            // getResourceSettings().setCssCompressor(new
            // de.agilecoders.wicket.extensions.javascript.YuiCssCompressor());
        }

        // library resources
        this.setJavaScriptLibrarySettings(new WicketResourceReferences());

        // to put javascript down on the page (DefaultWebPage.html must contain
        // wicket:id='footer-bucket'
        this.setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("footer-bucket"));

        // store
        this.initStore();

        // stateless checker
        if (inDevelopment) {
            this.getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
        }

        addBundles();

        // jsr bean validation: special models can implement IPropertyResolver
        // to return the propertyname
        BeanValidationConfiguration beanValidationConfiguration = new BeanValidationConfiguration();
        beanValidationConfiguration.configure(this);
        beanValidationConfiguration.add(component -> {
            IModel<?> model = component.getModel();
            if (model instanceof IPropertyResolver) {
                return ((IPropertyResolver) model).resolveProperty(component);
            }
            return null;
        });

        // mount resources
        this.mountImages();
        this.mountResources();
        this.mountPages();

        // defaults
        this.getMarkupSettings().setDefaultBeforeDisabledLink("");
        this.getMarkupSettings().setDefaultAfterDisabledLink("");

        // exceptions
        this.getExceptionSettings()
                .setUnexpectedExceptionDisplay(inDevelopment ? IExceptionSettings.SHOW_EXCEPTION_PAGE : IExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
        /*
         * getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class) ;
         * getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class ); getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.
         * class);
         */

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
        }

        getSessionListeners().add(session -> logger.trace("new session created {}", session));
    }

    protected void addBundles() {
        // this.getResourceBundles()
        // .addJavaScriptBundle(WicketJSRoot.class, "tinymcebundle.js",
        // new JavaScriptResourceReference[] {
        // BootstrapTinyMCE.JS,
        // BootstrapTinyMCE.JS_JQUERY,
        // BootstrapTinyMCE.JS_PLUGIN_ADVLIST,
        // BootstrapTinyMCE.JS_PLUGIN_ANCHOR,
        // BootstrapTinyMCE.JS_PLUGIN_AUTOLINK,
        // BootstrapTinyMCE.JS_PLUGIN_CHARMAP,
        // BootstrapTinyMCE.JS_PLUGIN_CODE,
        // BootstrapTinyMCE.JS_PLUGIN_COLORPICKER,
        // BootstrapTinyMCE.JS_PLUGIN_HELP,
        // BootstrapTinyMCE.JS_PLUGIN_HR,
        // BootstrapTinyMCE.JS_PLUGIN_IMAGE,
        // BootstrapTinyMCE.JS_PLUGIN_INSERTDATETIME,
        // BootstrapTinyMCE.JS_PLUGIN_LINK,
        // BootstrapTinyMCE.JS_PLUGIN_LISTS,
        // BootstrapTinyMCE.JS_PLUGIN_MEDIA,
        // BootstrapTinyMCE.JS_PLUGIN_PASTE,
        // BootstrapTinyMCE.JS_PLUGIN_PREVIEW,
        // BootstrapTinyMCE.JS_PLUGIN_PRINT,
        // BootstrapTinyMCE.JS_PLUGIN_SEARCHREPLACE,
        // BootstrapTinyMCE.JS_PLUGIN_TABLE,
        // BootstrapTinyMCE.JS_PLUGIN_TEXTCOLOR,
        // BootstrapTinyMCE.JS_PLUGIN_VISUALCHARS,
        // BootstrapTinyMCE.JS_PLUGIN_WORDCOUNT });
    }

    protected void initStore() {
        WicketAppSettings s = getSettings();
        if (this.usesDevelopmentConfig()) {
            if (!s.isDiskStore()) {
                this.setPageManagerProvider(new DefaultPageManagerProvider(this) {
                    @Override
                    protected IDataStore newDataStore() {
                        return new HttpSessionDataStore(WicketApplication.this.getPageManagerContext(), new PageNumberEvictionStrategy(20));
                    }
                });
            } else {
                DebugDiskDataStore.register(this);
            }
        } else {
            if (!s.isDiskStore()) {
                this.setPageManagerProvider(new DefaultPageManagerProvider(this) {
                    @Override
                    protected IDataStore newDataStore() {
                        return new HttpSessionDataStore(WicketApplication.this.getPageManagerContext(), new PageNumberEvictionStrategy(20));
                    }
                });
            } // no else
        }
    }

    protected void mountImages() {
        String cssImages = "css/images/";
        for (Field field : WicketIconsRoot.class.getFields()) {
            try {
                final String name = String.valueOf(field.get(WicketIconsRoot.class));
                PackageResourceReference reference = new VirtualPackageResourceReference(WicketCSSRoot.class, cssImages + name, WicketIconsRoot.class,
                        name);
                this.getSharedResources().add(cssImages + name, reference.getResource());
                this.mountResource(cssImages + name, reference);
                WicketApplication.logger.info("mounting image: " + WicketRoot.class.getCanonicalName() + ": " + cssImages + name);
            } catch (Exception ex) {
                WicketApplication.logger.error("{}", ex);
            }
        }
    }

    protected void mountPages() {
        // https://github.com/wicketstuff/core/wiki/Logback
        // also see web.xml and logback.xml
        this.mountPage("logback", ConfiguratorPage.class);

        new AnnotatedMountScanner().scanPackage(this.pagesPackages).mount(this);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        final AnnotationTypeFilter mountedPageFilter = new AnnotationTypeFilter(MountedPage.class);
        final AssignableTypeFilter webPageFilter = new AssignableTypeFilter(WebPage.class);
        TypeFilter TypeFilter = (metadataReader, metadataReaderFactory) -> mountedPageFilter.match(metadataReader, metadataReaderFactory)
                && webPageFilter.match(metadataReader, metadataReaderFactory);
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
        if (webApplicationContext == null)
            webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(get().getServletContext());
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
            settings.setCacheDuration("none");
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
}