package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.List;

import org.apache.wicket.request.resource.ResourceReference;

import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.Plugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Align;
import wicket.contrib.tinymce.settings.TinyMCESettings.EntityEncoding;
import wicket.contrib.tinymce.settings.TinyMCESettings.Language;
import wicket.contrib.tinymce.settings.TinyMCESettings.Location;
import wicket.contrib.tinymce.settings.TinyMCESettings.Position;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;
import wicket.contrib.tinymce.settings.TinyMCESettings.Toolbar;

public class TinyMCETextAreaSettings extends AbstractTextAreaSettings<TinyMCETextAreaSettings> {
    private static final long serialVersionUID = 2558455789913160626L;

    protected TinyMCESettings tinyMCESettings;

    public TinyMCETextAreaSettings() {
        super();
    }

    public TinyMCETextAreaSettings(Theme theme) {
        this.tinyMCESettings = new TinyMCESettings(theme);
    }

    public TinyMCETextAreaSettings add(Button button, Toolbar toolbar, Position position) {
        this.getTinyMCESettings().add(button, toolbar, position);
        return this;
    }

    public TinyMCETextAreaSettings addCustomSetting(String customSetting) {
        this.getTinyMCESettings().addCustomSetting(customSetting);
        return this;
    }

    public TinyMCETextAreaSettings disableButton(Button button) {
        this.getTinyMCESettings().disableButton(button);
        return this;
    }

    public String getAdditionalPluginJavaScript() {
        return this.getTinyMCESettings().getAdditionalPluginJavaScript();
    }

    public String getBlockFormats() {
        return this.getTinyMCESettings().getBlockFormats();
    }

    public ResourceReference getContentCss() {
        return this.getTinyMCESettings().getContentCss();
    }

    public Boolean getConvertUrls() {
        return this.getTinyMCESettings().getConvertUrls();
    }

    public String[] getCustomSettings() {
        return this.getTinyMCESettings().getCustomSettings();
    }

    public String getDocumentBaseUrl() {
        return this.getTinyMCESettings().getDocumentBaseUrl();
    }

    public EntityEncoding getEntityEncoding() {
        return this.getTinyMCESettings().getEntityEncoding();
    }

    public boolean getHorizontalResizing() {
        return this.getTinyMCESettings().getHorizontalResizing();
    }

    public Language getLanguage() {
        return this.getTinyMCESettings().getLanguage();
    }

    public String getLoadPluginJavaScript() {
        return this.getTinyMCESettings().getLoadPluginJavaScript();
    }

    public Boolean getRelativeUrls() {
        return this.getTinyMCESettings().getRelativeUrls();
    }

    public Boolean getRemoveScriptHost() {
        return this.getTinyMCESettings().getRemoveScriptHost();
    }

    public boolean getResizing() {
        return this.getTinyMCESettings().getResizing();
    }

    public boolean getResizingUseCookie() {
        return this.getTinyMCESettings().getResizingUseCookie();
    }

    public Location getStatusbarLocation() {
        return this.getTinyMCESettings().getStatusbarLocation();
    }

    public Theme getTheme() {
        return this.getTinyMCESettings().getTheme();
    }

    public TinyMCESettings getTinyMCESettings() {
        if (this.tinyMCESettings == null) {
            this.tinyMCESettings = new TinyMCESettings();
        }
        return this.tinyMCESettings;
    }

    public Align getToolbarAlign() {
        return this.getTinyMCESettings().getToolbarAlign();
    }

    public List<Button> getToolbarButtons(Toolbar toolbar) {
        return this.getTinyMCESettings().getToolbarButtons(toolbar);
    }

    public Location getToolbarLocation() {
        return this.getTinyMCESettings().getToolbarLocation();
    }

    @Override
    public boolean isReadOnly() {
        return this.getTinyMCESettings().isReadOnly();
    }

    public TinyMCETextAreaSettings register(Plugin plugin) {
        this.getTinyMCESettings().register(plugin);
        return this;
    }

    public TinyMCETextAreaSettings setBlockFormats(String blockFormats) {
        this.getTinyMCESettings().setBlockFormats(blockFormats);
        return this;
    }

    public TinyMCETextAreaSettings setContentCss(ResourceReference contentCss) {
        this.getTinyMCESettings().setContentCss(contentCss);
        return this;
    }

    public TinyMCETextAreaSettings setConvertUrls(boolean convertUrls) {
        this.getTinyMCESettings().setConvertUrls(convertUrls);
        return this;
    }

    public TinyMCETextAreaSettings setDocumentBaseUrl(String documentBaseUrl) {
        this.getTinyMCESettings().setDocumentBaseUrl(documentBaseUrl);
        return this;
    }

    public TinyMCETextAreaSettings setEntityEncoding(EntityEncoding entityEncoding) {
        this.getTinyMCESettings().setEntityEncoding(entityEncoding);
        return this;
    }

    public TinyMCETextAreaSettings setHorizontalResizing(boolean horizontalResizing) {
        this.getTinyMCESettings().setHorizontalResizing(horizontalResizing);
        return this;
    }

    @Override
    public TinyMCETextAreaSettings setReadOnly(boolean readOnly) {
        this.getTinyMCESettings().setReadOnly(readOnly);
        return this;
    }

    public TinyMCETextAreaSettings setRelativeUrls(Boolean relativeUrls) {
        this.getTinyMCESettings().setRelativeUrls(relativeUrls);
        return this;
    }

    public TinyMCETextAreaSettings setRemoveScriptHost(Boolean removeScriptHost) {
        this.getTinyMCESettings().setRemoveScriptHost(removeScriptHost);
        return this;
    }

    public TinyMCETextAreaSettings setResizing(boolean resizing) {
        this.getTinyMCESettings().setResizing(resizing);
        return this;
    }

    public TinyMCETextAreaSettings setResizingUseCookie(boolean resizingUseCookie) {
        this.getTinyMCESettings().setResizingUseCookie(resizingUseCookie);
        return this;
    }

    public TinyMCETextAreaSettings setStatusbarLocation(Location statusbarLocation) {
        this.getTinyMCESettings().setStatusbarLocation(statusbarLocation);
        return this;
    }

    public TinyMCETextAreaSettings setTinyMCESettings(TinyMCESettings tinyMCESettings) {
        this.tinyMCESettings = tinyMCESettings;
        return this;
    }

    public TinyMCETextAreaSettings setToolbarAlign(Align toolbarAlign) {
        this.getTinyMCESettings().setToolbarAlign(toolbarAlign);
        return this;
    }

    public TinyMCETextAreaSettings setToolbarButtons(Toolbar toolbar, List<Button> buttons) {
        this.getTinyMCESettings().setToolbarButtons(toolbar, buttons);
        return this;
    }

    public TinyMCETextAreaSettings setToolbarLocation(Location toolbarLocation) {
        this.getTinyMCESettings().setToolbarLocation(toolbarLocation);
        return this;
    }
}
