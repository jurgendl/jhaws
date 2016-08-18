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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.js.GoogleLogin;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.htmlcompressor.HtmlCompressingMarkupFactory;
import org.wicketstuff.logback.ConfiguratorPage;

import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;

public class WicketApplication extends WebApplication {
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

	@SpringBean(name = "diskStore", required = false)
	protected boolean diskStore = false;

	@SpringBean(name = "showDebugbars", required = false)
	protected boolean showDebugbars = true;

	@SpringBean(name = "checkCookiesEnabled", required = false)
	protected boolean checkCookiesEnabled = true;

	@SpringBean(name = "checkJavaScriptEnabled", required = false)
	protected boolean checkJavaScriptEnabled = true;

	@SpringBean(name = "checkAdsEnabled", required = false)
	protected boolean checkAdsEnabled = false;

	@SpringBean(name = "shortcutIcon", required = false)
	protected String shortcutIcon;

	@SpringBean(name = "javascriptAtBottom", required = false)
	protected boolean javascriptAtBottom = true;

	@SpringBean(name = "javascriptAtBottom", required = false)
	protected String cacheDuration;

	@SpringBean(name = "gatherBrowserInfo", required = false)
	protected boolean gatherBrowserInfo = true;

    @SpringBean(name = "googleSigninClientId", required = false)
    protected String googleSigninClientId = "";

	public WicketApplication() {
        GoogleLogin.init(this);
	}

	public WicketSession createSession(Request request, Response response) {
		return new WicketSession(request);
	}

	public String getCacheDuration() {
		return this.cacheDuration;
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends DefaultWebPage> getHomePage() {
		return DefaultWebPage.class;
	}

	public String getShortcutIcon() {
		return this.shortcutIcon;
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

		boolean deployed = this.usesDeploymentConfig();
		boolean inDevelopment = !deployed;

		// spring injector
		this.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		Injector.get().inject(this);

		// gather browser info
		if (this.isGatherBrowserInfo()) {
			this.getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
			// => ((WebClientInfo)WebRequestCycle.get().getClientInfo()).getProperties().isJavaEnabled()
		}

		// markup settings
		this.getMarkupSettings().setStripComments(deployed);
		this.getMarkupSettings().setCompressWhitespace(deployed);
		this.getMarkupSettings().setStripWicketTags(true);
		if (deployed) {
			this.getMarkupSettings().setMarkupFactory(new HtmlCompressingMarkupFactory());
		}

		// request logger settings
		this.getRequestLoggerSettings().setRecordSessionSize(inDevelopment);
		this.getRequestLoggerSettings().setRequestLoggerEnabled(inDevelopment);

		// debug settings
		this.getDebugSettings().setAjaxDebugModeEnabled(this.showDebugbars && inDevelopment);
		this.getDebugSettings().setComponentUseCheck(inDevelopment);
		this.getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
		this.getDebugSettings().setOutputMarkupContainerClassName(inDevelopment);
		this.getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
		// getDebugSettings().setOutputComponentPath(inDevelopment);

		// resource settings
		this.getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
		this.getResourceSettings().setUseMinifiedResources(deployed);
		this.getResourceSettings().setEncodeJSessionId(deployed);
		this.getResourceSettings().setDefaultCacheDuration(
				StringUtils.isNotBlank(this.cacheDuration) ? ("none".equals(this.cacheDuration) ? Duration.NONE : Duration.valueOf(this.cacheDuration))
						: (inDevelopment ? Duration.NONE : WebResponse.MAX_CACHE_DURATION));

		if (deployed) {
			// minify your resources on deploy
			// getResourceSettings().setJavaScriptCompressor(new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
			// getResourceSettings().setCssCompressor(new YuiCssCompressor());
		}

		// library resources
		this.setJavaScriptLibrarySettings(new WicketResourceReferences());

		// to put javascript down on the page (DefaultWebPage.html must contain wicket:id='footer-bucket'
		this.setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("footer-bucket"));

		// store
		this.initStore();

		// stateless checker
		if (inDevelopment) {
			this.getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
		}

		// styling bundle
		this.getResourceBundles().addCssBundle(WicketCSSRoot.class, "basebundle.css", //
				WicketCSSRoot.CLEARFIX, //
				WicketCSSRoot.NORMALIZE, //
				WicketCSSRoot.GENERAL);//

		// jquery & prime bundle
		this.getResourceBundles().addJavaScriptBundle(WicketJSRoot.class, "jquery+ui.js", //
				JQuery.getJQueryReference(), //
				JQueryUI.getJQueryUIReference(), //
				JQueryUI.JQUERY_UI_FACTORY_JS, //
				PrimeUI.PRIME_UI_JS, //
				PrimeUI.PRIME_UI_FACTORY_JS);//

		// jsr bean validation: special models can implement IPropertyResolver to return the propertyname
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
		this.getExceptionSettings().setUnexpectedExceptionDisplay(inDevelopment ? IExceptionSettings.SHOW_EXCEPTION_PAGE : IExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
		/*
		 * getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class); getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
		 * getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
		 */

		// http://wicketguide.comsysto.com/guide/chapter19.html#chapter19_4
		IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard) {
			SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
			guard.addPattern("+*.mp4");
			guard.addPattern("+*.flv");
			guard.addPattern("+*.ogg");
			guard.addPattern("+*.webm");
		}
	}

	protected void initStore() {
		if (this.usesDevelopmentConfig()) {
			if (!this.diskStore) {
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
			if (!this.diskStore) {
				this.setPageManagerProvider(new DefaultPageManagerProvider(this) {
					@Override
					protected IDataStore newDataStore() {
						return new HttpSessionDataStore(WicketApplication.this.getPageManagerContext(), new PageNumberEvictionStrategy(20));
					}
				});
			} // no else
		}
	}

	public boolean isCheckAdsEnabled() {
		return this.checkAdsEnabled;
	}

	public boolean isCheckCookiesEnabled() {
		return this.checkCookiesEnabled;
	}

	public boolean isCheckJavaScriptEnabled() {
		return this.checkJavaScriptEnabled;
	}

	public boolean isGatherBrowserInfo() {
		return this.gatherBrowserInfo;
	}

	public boolean isJavascriptAtBottom() {
		return this.javascriptAtBottom;
	}

	public boolean isShowDebugbars() {
		return this.showDebugbars;
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
		PrimeUI.mountImages(this);
	}

	protected void mountPages() {
		// https://github.com/wicketstuff/core/wiki/Logback
		// also see web.xml and logback.xml
		this.mountPage("logback", ConfiguratorPage.class);

		new AnnotatedMountScanner().scanPackage(this.pagesPackages).mount(this);

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		final AnnotationTypeFilter mountedPageFilter = new AnnotationTypeFilter(MountedPage.class);
		final AssignableTypeFilter webPageFilter = new AssignableTypeFilter(WebPage.class);
		TypeFilter TypeFilter = (metadataReader, metadataReaderFactory) -> mountedPageFilter.match(metadataReader, metadataReaderFactory) && webPageFilter.match(metadataReader, metadataReaderFactory);
		scanner.addIncludeFilter(TypeFilter);
		List<String> paths = new ArrayList<String>();
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
	final public Session newSession(Request request, Response response) {
		WicketSession wicketSession = this.createSession(request, response);
		WicketApplication.logger.trace("creating new session {}", wicketSession);
		return wicketSession;
	}

	public void setCacheDuration(String cacheDuration) {
		this.cacheDuration = cacheDuration;
	}

	public void setCheckAdsEnabled(boolean checkAdsEnabled) {
		this.checkAdsEnabled = checkAdsEnabled;
	}

	public void setCheckCookiesEnabled(boolean checkCookiesEnabled) {
		this.checkCookiesEnabled = checkCookiesEnabled;
	}

	public void setCheckJavaScriptEnabled(boolean checkJavaScriptEnabled) {
		this.checkJavaScriptEnabled = checkJavaScriptEnabled;
	}

	public void setGatherBrowserInfo(boolean gatherBrowserInfo) {
		this.gatherBrowserInfo = gatherBrowserInfo;
	}

	public void setJavascriptAtBottom(boolean javascriptAtBottom) {
		this.javascriptAtBottom = javascriptAtBottom;
	}

	public void setShortcutIcon(String shortcutIcon) {
		this.shortcutIcon = shortcutIcon;
	}

	public void setShowDebugbars(boolean showDebugbars) {
		this.showDebugbars = showDebugbars;
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

    public String getGoogleSigninClientId() {
        return googleSigninClientId;
    }

    public void setGoogleSigninClientId(String googleSigninClientId) {
        this.googleSigninClientId = googleSigninClientId;
    }
}