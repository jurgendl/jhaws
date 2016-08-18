package org.tools.hqlbuilder.webservice.wicket;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.WicketAjaxDebugJQueryResourceReference;
import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

import com.googlecode.wicket.jquery.core.resource.JQueryGlobalizeResourceReference;
import com.googlecode.wicket.jquery.core.settings.JQueryLibrarySettings;
import com.googlecode.wicket.jquery.ui.plugins.emoticons.resource.EmoticonsJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.emoticons.resource.EmoticonsStyleSheetResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.fixedheadertable.resource.FixedHeaderTableJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.fixedheadertable.resource.FixedHeaderTableStyleSheetResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.BootstrapCombinedNoIconsStyleSheetResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.BootstrapDropDownJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.BootstrapResponsiveStyleSheetResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.BootstrapWysiwygJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.EditorStyleSheetResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.JQueryHotKeysJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.plugins.wysiwyg.resource.PrettifyJavaScriptResourceReference;
import com.googlecode.wicket.jquery.ui.resource.JQueryUIResourceReference;

/**
 * combination of resource references with public getters and setters
 */
public class WicketResourceReferences extends JQueryLibrarySettings {
    /** if possible load resources from CDN when they match any of the patterns in this list */
    protected final List<Pattern> acceptedCDNPatterns = new ArrayList<Pattern>();

    public List<Pattern> getAcceptedCDNPatterns() {
        return this.acceptedCDNPatterns;
    }

    public void addAcceptedCDNPattern(Pattern acceptedCDNPattern) {
        this.acceptedCDNPatterns.add(acceptedCDNPattern);
    }

    public void setAcceptedCDNPatterns(List<String> acceptedCDNPatterns) {
        for (String acceptedCDNPattern : acceptedCDNPatterns) {
            addAcceptedCDNPattern(Pattern.compile(acceptedCDNPattern));
        }
    }

    protected static WicketResourceReferences instance;

    public static synchronized WicketResourceReferences get() {
        if (WicketResourceReferences.instance == null) {
            WicketResourceReferences.instance = new WicketResourceReferences();
        }

        return WicketResourceReferences.instance;
    }

    protected ResourceReference jQueryReference;

    protected ResourceReference wicketEventReference;

    protected ResourceReference wicketAjaxReference;

    protected ResourceReference wicketAjaxDebugReference;

    protected ResourceReference jQueryUIReference;

    protected ResourceReference jQueryGlobalizeReference;

    // protected ResourceReference kendoUICommonStyleSheetReference;

    //    protected ResourceReference kendoUIThemeStyleSheetReference;

//    protected ResourceReference kendoUIJavaScriptReference;

    protected ResourceReference calendarJavaScriptReference;

    protected ResourceReference calendarStyleSheetReference;

    protected ResourceReference gCalJavaScriptReference;

    protected ResourceReference superfishStyleSheetReference;

    protected ResourceReference superfishVerticalStyleSheetReference;

    protected ResourceReference consoleStyleSheetReference;

    protected ResourceReference consoleJavaScriptReference;

    protected ResourceReference fixedHeaderTableStyleSheetReference;

    protected ResourceReference fixedHeaderTableJavaScriptReference;

    protected JavaScriptResourceReference bootstrapDropDownJavaScriptReference;

    protected JavaScriptResourceReference bootstrapWysiwygJavaScriptReference;

    protected JavaScriptResourceReference jQueryHotKeysJavaScriptReference;

    protected JavaScriptResourceReference prettifyJavaScriptReference;

    protected ResourceReference emoticonsStyleSheetReference;

    protected ResourceReference emoticonsJavaScriptReference;

    public WicketResourceReferences() {
        this(true);
    }

    public WicketResourceReferences(boolean init) {
        if (init) {
            setJQueryUIReference(JQueryUIResourceReference.get());// redirect to default
            setJQueryReference(JQueryResourceReference.get());// redirect to default
            setWicketEventReference(WicketEventJQueryResourceReference.get());// redirect to default
            setWicketAjaxReference(WicketAjaxJQueryResourceReference.get());// redirect to default
            setWicketAjaxDebugReference(WicketAjaxDebugJQueryResourceReference.get());// redirect to default
            setJQueryGlobalizeReference(JQueryGlobalizeResourceReference.get()); // this one is not set by default
			// setKendoUICommonStyleSheetReference(KendoUI.KENDO_COMMON_CSS); //
			// this one is not set by default
			// setKendoUIThemeStyleSheetReference(KendoUI.KENDO_DEFAULT_CSS); //
			// this one is not set by default
			// setKendoUIJavaScriptReference(KendoUILibrarySettings.get().getJavaScriptReference());
			// // redirect to default
			// setCalendarJavaScriptReference(CalendarLibrarySettings.get().getJavaScriptReference());
			// // redirect to default
			// setCalendarStyleSheetReference(CalendarLibrarySettings.get().getStyleSheetReference());
			// // redirect to default
			// setGCalJavaScriptReference(CalendarLibrarySettings.get().getGCalJavaScriptReference());
			// // redirect to default
			// setSuperfishStyleSheetReference(SuperfishStyleSheetResourceReference.get());//
			// redirect to default
			// setSuperfishVerticalStyleSheetReference(SuperfishVerticalStyleSheetResourceReference.get());//
			// redirect to default
			// setConsoleStyleSheetReference(ConsoleStyleSheetResourceReference.get());//
			// redirect to default
			// setConsoleJavaScriptReference(ConsoleJavaScriptResourceReference.get());//
			// redirect to default
            setFixedHeaderTableStyleSheetReference(FixedHeaderTableStyleSheetResourceReference.get());// redirect to default
            setFixedHeaderTableJavaScriptReference(FixedHeaderTableJavaScriptResourceReference.get());// redirect to default
            setBootstrapDropDownJavaScriptReference(BootstrapDropDownJavaScriptResourceReference.get());// redirect to default
            setBootstrapWysiwygJavaScriptReference(BootstrapWysiwygJavaScriptResourceReference.get());// redirect to default
            setJQueryHotKeysJavaScriptReference(JQueryHotKeysJavaScriptResourceReference.get());// redirect to default
            setPrettifyJavaScriptReference(PrettifyJavaScriptResourceReference.get());// redirect to default
            setEmoticonsStyleSheetReference(EmoticonsStyleSheetResourceReference.get());// redirect to default
            setEmoticonsJavaScriptReference(EmoticonsJavaScriptResourceReference.get());// redirect to default
        }
    }

    /**
     * @see com.googlecode.wicket.kendo.ui.settings.IKendoUILibrarySettings#getKendoUICommonStyleSheetReference()
     */

	// public ResourceReference getKendoUICommonStyleSheetReference() {
	// return kendoUICommonStyleSheetReference;
	// }

    /**
     * @see com.googlecode.wicket.kendo.ui.settings.IKendoUILibrarySettings#getKendoUIThemeStyleSheetReference()
     */

	// public ResourceReference getKendoUIThemeStyleSheetReference() {
	// return kendoUIThemeStyleSheetReference;
	// }

    /**
     * @see com.googlecode.wicket.kendo.ui.settings.IKendoUILibrarySettings#getKendoUIJavaScriptReference()
     */

	// public ResourceReference getKendoUIJavaScriptReference() {
	// return kendoUIJavaScriptReference;
	// }

    /**
     * @see com.googlecode.wicket.jquery.ui.calendar.settings.ICalendarLibrarySettings#getCalendarJavaScriptReference()
     */

    public ResourceReference getCalendarJavaScriptReference() {
        return calendarJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.calendar.settings.ICalendarLibrarySettings#getCalendarStyleSheetReference()
     */

    public ResourceReference getCalendarStyleSheetReference() {
        return calendarStyleSheetReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.calendar.settings.ICalendarLibrarySettings#getGCalJavaScriptReference()
     */

    public ResourceReference getGCalJavaScriptReference() {
        return gCalJavaScriptReference;
    }

	// public void setKendoUICommonStyleSheetReference(ResourceReference
	// kendoUICommonStyleSheetReference) {
	// this.kendoUICommonStyleSheetReference = kendoUICommonStyleSheetReference;
	// }

	// public void setKendoUIThemeStyleSheetReference(ResourceReference
	// kendoUIThemeStyleSheetReference) {
	// this.kendoUIThemeStyleSheetReference = kendoUIThemeStyleSheetReference;
	// }

	// public void setKendoUIJavaScriptReference(ResourceReference
	// kendoUIJavaScriptReference) {
	// this.kendoUIJavaScriptReference = kendoUIJavaScriptReference;
	// }

    public void setCalendarJavaScriptReference(ResourceReference calendarJavaScriptReference) {
        this.calendarJavaScriptReference = calendarJavaScriptReference;
    }

    public void setCalendarStyleSheetReference(ResourceReference calendarStyleSheetReference) {
        this.calendarStyleSheetReference = calendarStyleSheetReference;
    }

    public void setGCalJavaScriptReference(ResourceReference gCalJavaScriptReference) {
        this.gCalJavaScriptReference = gCalJavaScriptReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#getJQueryReference()
     */
    @Override
    public ResourceReference getJQueryReference() {
        return this.jQueryReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#getWicketEventReference()
     */
    @Override
    public ResourceReference getWicketEventReference() {
        return this.wicketEventReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#getWicketAjaxReference()
     */
    @Override
    public ResourceReference getWicketAjaxReference() {
        return this.wicketAjaxReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#getWicketAjaxDebugReference()
     */
    @Override
    public ResourceReference getWicketAjaxDebugReference() {
        return this.wicketAjaxDebugReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings#getJQueryUIReference()
     */

    public ResourceReference getJQueryUIReference() {
        return this.jQueryUIReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings#getJQueryGlobalizeReference()
     */

    @Override
    public ResourceReference getJQueryGlobalizeReference() {
        return this.jQueryGlobalizeReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#setJQueryReference(org.apache.wicket.request.resource.ResourceReference)
     */
    @Override
    public void setJQueryReference(ResourceReference jQueryReference) {
        this.jQueryReference = jQueryReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#setWicketEventReference(org.apache.wicket.request.resource.ResourceReference)
     */
    @Override
    public void setWicketEventReference(ResourceReference wicketEventReference) {
        this.wicketEventReference = wicketEventReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#setWicketAjaxReference(org.apache.wicket.request.resource.ResourceReference)
     */
    @Override
    public void setWicketAjaxReference(ResourceReference wicketAjaxReference) {
        this.wicketAjaxReference = wicketAjaxReference;
    }

    /**
     * @see org.apache.wicket.settings.IJavaScriptLibrarySettings#setWicketAjaxDebugReference(org.apache.wicket.request.resource.ResourceReference)
     */
    @Override
    public void setWicketAjaxDebugReference(ResourceReference wicketAjaxDebugReference) {
        this.wicketAjaxDebugReference = wicketAjaxDebugReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings#setJQueryUIReference(org.apache.wicket.request.resource.ResourceReference)
     */
    public void setJQueryUIReference(ResourceReference jQueryUIReference) {
        this.jQueryUIReference = jQueryUIReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings#setJQueryGlobalizeReference(org.apache.wicket.request.resource.ResourceReference)
     */

    @Override
    public void setJQueryGlobalizeReference(ResourceReference jQueryGlobalizeReference) {
        this.jQueryGlobalizeReference = jQueryGlobalizeReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.sfmenu.settings.ISuperfishLibrarySettings#getSuperfishStyleSheetReference()
     */
    public ResourceReference getSuperfishStyleSheetReference() {
        return superfishStyleSheetReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.sfmenu.settings.ISuperfishLibrarySettings#getSuperfishVerticalStyleSheetReference()
     */
    public ResourceReference getSuperfishVerticalStyleSheetReference() {
        return superfishVerticalStyleSheetReference;
    }

    public void setSuperfishStyleSheetReference(ResourceReference superfishStyleSheetReference) {
        this.superfishStyleSheetReference = superfishStyleSheetReference;
    }

    public void setSuperfishVerticalStyleSheetReference(ResourceReference superfishVerticalStyleSheetReference) {
        this.superfishVerticalStyleSheetReference = superfishVerticalStyleSheetReference;
    }

    /**
     * @see com.googlecode.wicket.kendo.ui.settings.IConsoleLibrarySettings#getConsoleStyleSheetReference()
     */

    public ResourceReference getConsoleStyleSheetReference() {
        return consoleStyleSheetReference;
    }

    /**
     * @see com.googlecode.wicket.kendo.ui.settings.IConsoleLibrarySettings#getConsoleJavaScriptReference()
     */

    public ResourceReference getConsoleJavaScriptReference() {
        return consoleJavaScriptReference;
    }

    public void setConsoleStyleSheetReference(ResourceReference consoleStyleSheetReference) {
        this.consoleStyleSheetReference = consoleStyleSheetReference;
    }

    public void setConsoleJavaScriptReference(ResourceReference consoleJavaScriptReference) {
        this.consoleJavaScriptReference = consoleJavaScriptReference;
    }

    /**
     *
     * @see com.googlecode.wicket.jquery.ui.plugins.fixedheadertable.settings.IFixedHeaderTableLibrarySettings#getFixedHeaderTableStyleSheetReference()
     */

    public ResourceReference getFixedHeaderTableStyleSheetReference() {
        return fixedHeaderTableStyleSheetReference;
    }

    /**
     *
     * @see com.googlecode.wicket.jquery.ui.plugins.fixedheadertable.settings.IFixedHeaderTableLibrarySettings#getFixedHeaderTableJavaScriptReference()
     */

    public ResourceReference getFixedHeaderTableJavaScriptReference() {
        return fixedHeaderTableJavaScriptReference;
    }

    public void setFixedHeaderTableStyleSheetReference(ResourceReference fixedHeaderTableStyleSheetReference) {
        this.fixedHeaderTableStyleSheetReference = fixedHeaderTableStyleSheetReference;
    }

    public void setFixedHeaderTableJavaScriptReference(ResourceReference fixedHeaderTableJavaScriptReference) {
        this.fixedHeaderTableJavaScriptReference = fixedHeaderTableJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getBootstrapCombinedNoIconsStyleSheetReference()
     */

    public CssResourceReference getBootstrapCombinedNoIconsStyleSheetReference() {
        return BootstrapCombinedNoIconsStyleSheetResourceReference.get();
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getBootstrapResponsiveStyleSheetReference()
     */

    public CssResourceReference getBootstrapResponsiveStyleSheetReference() {
        return BootstrapResponsiveStyleSheetResourceReference.get();
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getEditorStyleSheetReference()
     */

    public CssResourceReference getEditorStyleSheetReference() {
        return EditorStyleSheetResourceReference.get();
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getBootstrapDropDownJavaScriptReference()
     */

    public JavaScriptResourceReference getBootstrapDropDownJavaScriptReference() {
        return bootstrapDropDownJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getBootstrapWysiwygJavaScriptReference()
     */

    public JavaScriptResourceReference getBootstrapWysiwygJavaScriptReference() {
        return bootstrapWysiwygJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getJQueryHotKeysJavaScriptReference()
     */

    public JavaScriptResourceReference getJQueryHotKeysJavaScriptReference() {
        return jQueryHotKeysJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.wysiwyg.settings.IWysiwygLibrarySettings#getPrettifyJavaScriptReference()
     */

    public JavaScriptResourceReference getPrettifyJavaScriptReference() {
        return prettifyJavaScriptReference;
    }

    public void setPrettifyJavaScriptReference(JavaScriptResourceReference prettifyJavaScriptReference) {
        this.prettifyJavaScriptReference = prettifyJavaScriptReference;
    }

    public void setJQueryHotKeysJavaScriptReference(JavaScriptResourceReference jQueryHotKeysJavaScriptReference) {
        this.jQueryHotKeysJavaScriptReference = jQueryHotKeysJavaScriptReference;
    }

    public void setBootstrapWysiwygJavaScriptReference(JavaScriptResourceReference bootstrapWysiwygJavaScriptReference) {
        this.bootstrapWysiwygJavaScriptReference = bootstrapWysiwygJavaScriptReference;
    }

    public void setBootstrapDropDownJavaScriptReference(JavaScriptResourceReference bootstrapDropDownJavaScriptReference) {
        this.bootstrapDropDownJavaScriptReference = bootstrapDropDownJavaScriptReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.emoticons.settings.IEmoticonsLibrarySettings#getEmoticonsStyleSheetReference()
     */

    public ResourceReference getEmoticonsStyleSheetReference() {
        return emoticonsStyleSheetReference;
    }

    /**
     * @see com.googlecode.wicket.jquery.ui.plugins.emoticons.settings.IEmoticonsLibrarySettings#getEmoticonsJavaScriptReference()
     */

    public ResourceReference getEmoticonsJavaScriptReference() {
        return emoticonsJavaScriptReference;
    }

    public void setEmoticonsStyleSheetReference(ResourceReference emoticonsStyleSheetReference) {
        this.emoticonsStyleSheetReference = emoticonsStyleSheetReference;
    }

    public void setEmoticonsJavaScriptReference(ResourceReference emoticonsJavaScriptReference) {
        this.emoticonsJavaScriptReference = emoticonsJavaScriptReference;
    }
}