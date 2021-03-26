package org.jhaws.common.web.wicket.settings;

import java.io.Serializable;
import java.util.function.Supplier;

import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.web.wicket.spin.Spin.SpinType;

@SuppressWarnings("serial")
@XmlRootElement(name = "web-app-settings")
public class WicketAppSettings implements Serializable {
	public static final JAXBMarshalling JAXB = new JAXBMarshalling(WicketAppSettings.class, WebSettings.class);

	protected Boolean diskStore = false;

	protected Boolean showDebugbars = true;

	protected Boolean checkCookiesEnabled = true;

	protected Boolean checkJavaScriptEnabled = true;

	protected Boolean checkAdsEnabled = false;

	protected String shortcutIcon;

	protected Boolean javascriptAtBottom = true;

	protected Integer cacheDuration;

	protected Boolean gatherBrowserInfo = true;

	protected String googleSigninClientId = "";

	protected Boolean demo = true;

	@EnumValidator(enumClazz = SpinType.class)
	protected String spinner = SpinType._default.id();

	@EnumValidator(enumClazz = Theme.class)
	protected String theme = Theme.defaulting.id();

	public static enum Theme implements Supplier<String> {
		defaulting("default"), //
		creative("creative"), //
		freelancer("freelancer"), //
		sbadmin2("sb-admin-2"), //
		cerulean(null), //
		cosmo(null), //
		cyborg(null), //
		darkly(null), //
		flatly(null), //
		journal(null), //
		litera(null), //
		lumen(null), //
		lux(null), //
		materia(null), //
		minty(null), //
		pulse(null), //
		sandstone(null), //
		simplex(null), //
		sketchy(null), //
		slate(null), //
		solar(null), //
		spacelab(null), //
		superhero(null), //
		united(null), //
		yeti(null), //
		resume(null), //
		grayscale(null), //
		;

		private final String id;

		private Theme(String id) {
			this.id = id;
			if (id == null)
				id = name();
		}

		public String id() {
			return id == null ? name() : id;
		}

		@Override
		public String get() {
			return id();
		}
	}

	public WicketAppSettings() {
		super();
	}

	public Boolean getDiskStore() {
		return this.diskStore;
	}

	public void setDiskStore(Boolean diskStore) {
		this.diskStore = diskStore;
	}

	public Boolean getShowDebugbars() {
		return this.showDebugbars;
	}

	public void setShowDebugbars(Boolean showDebugbars) {
		this.showDebugbars = showDebugbars;
	}

	public Boolean getCheckCookiesEnabled() {
		return this.checkCookiesEnabled;
	}

	public void setCheckCookiesEnabled(Boolean checkCookiesEnabled) {
		this.checkCookiesEnabled = checkCookiesEnabled;
	}

	public Boolean getCheckJavaScriptEnabled() {
		return this.checkJavaScriptEnabled;
	}

	public void setCheckJavaScriptEnabled(Boolean checkJavaScriptEnabled) {
		this.checkJavaScriptEnabled = checkJavaScriptEnabled;
	}

	public Boolean getCheckAdsEnabled() {
		return this.checkAdsEnabled;
	}

	public void setCheckAdsEnabled(Boolean checkAdsEnabled) {
		this.checkAdsEnabled = checkAdsEnabled;
	}

	public String getShortcutIcon() {
		return this.shortcutIcon;
	}

	public void setShortcutIcon(String shortcutIcon) {
		this.shortcutIcon = shortcutIcon;
	}

	public Boolean getJavascriptAtBottom() {
		return true;// FIXME this.javascriptAtBottom;
	}

	public void setJavascriptAtBottom(Boolean javascriptAtBottom) {
		// FIXME this.javascriptAtBottom = javascriptAtBottom;
	}

	public Integer getCacheDuration() {
		return this.cacheDuration;
	}

	public void setCacheDuration(Integer cacheDuration) {
		this.cacheDuration = cacheDuration;
	}

	public Boolean getGatherBrowserInfo() {
		return this.gatherBrowserInfo;
	}

	public void setGatherBrowserInfo(Boolean gatherBrowserInfo) {
		this.gatherBrowserInfo = gatherBrowserInfo;
	}

	public String getGoogleSigninClientId() {
		return this.googleSigninClientId;
	}

	public void setGoogleSigninClientId(String googleSigninClientId) {
		this.googleSigninClientId = googleSigninClientId;
	}

	public String getSpinner() {
		return this.spinner;
	}

	public void setSpinner(String spinner) {
		this.spinner = spinner;
	}

	public boolean isCheckAdsEnabled() {
		return Boolean.TRUE.equals(getCheckAdsEnabled());
	}

	public boolean isCheckCookiesEnabled() {
		return Boolean.TRUE.equals(getCheckCookiesEnabled());
	}

	public boolean isCheckJavaScriptEnabled() {
		return Boolean.TRUE.equals(getCheckJavaScriptEnabled());
	}

	public boolean isJavascriptAtBottom() {
		return Boolean.TRUE.equals(getJavascriptAtBottom());
	}

	public boolean isShowDebugbars() {
		return Boolean.TRUE.equals(getShowDebugbars());
	}

	public boolean isGatherBrowserInfo() {
		return Boolean.TRUE.equals(getGatherBrowserInfo());
	}

	public boolean isDiskStore() {
		return Boolean.TRUE.equals(getDiskStore());
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Boolean getDemo() {
		return this.demo;
	}

	public boolean isDemo() {
		return Boolean.TRUE.equals(getDemo());
	}

	public void setDemo(Boolean demo) {
		this.demo = demo;
	}
}
