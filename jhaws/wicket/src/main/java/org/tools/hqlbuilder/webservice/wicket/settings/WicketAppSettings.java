package org.tools.hqlbuilder.webservice.wicket.settings;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.tools.hqlbuilder.webservice.jquery.ui.spin.Spin.SpinType;

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

	protected String cacheDuration;

	protected Boolean gatherBrowserInfo = true;

	protected String googleSigninClientId = "";

	@EnumValidator(enumClazz = SpinType.class)
	protected String spinner = "default";

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
		return this.javascriptAtBottom;
	}

	public void setJavascriptAtBottom(Boolean javascriptAtBottom) {
		this.javascriptAtBottom = javascriptAtBottom;
	}

	public String getCacheDuration() {
		return this.cacheDuration;
	}

	public void setCacheDuration(String cacheDuration) {
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
}
