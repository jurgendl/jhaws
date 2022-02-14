package org.jhaws.common.web.wicket;

import org.apache.wicket.settings.JavaScriptLibrarySettings;

/**
 * combination of resource references with public getters and setters
 */
public class WicketResourceReferences extends JavaScriptLibrarySettings {
    // /**
    // * if possible load resources from CDN when they match any of the patterns
    // * in this list
    // */
    // protected final List<Pattern> acceptedCDNPatterns = new ArrayList<>();
    //
    // public List<Pattern> getAcceptedCDNPatterns() {
    // return this.acceptedCDNPatterns;
    // }
    //
    // public void addAcceptedCDNPattern(Pattern acceptedCDNPattern) {
    // this.acceptedCDNPatterns.add(acceptedCDNPattern);
    // }
    //
    // public void setAcceptedCDNPatterns(List<String> acceptedCDNPatterns) {
    // for (String acceptedCDNPattern : acceptedCDNPatterns) {
    // addAcceptedCDNPattern(Pattern.compile(acceptedCDNPattern));
    // }
    // }
    //
    // protected static WicketResourceReferences instance;
    //
    // public static synchronized WicketResourceReferences get() {
    // if (WicketResourceReferences.instance == null) {
    // WicketResourceReferences.instance = new WicketResourceReferences();
    // }
    //
    // return WicketResourceReferences.instance;
    // }
    //
    // //protected ResourceReference jQueryReference;
    //
    // //protected ResourceReference wicketEventReference;
    //
    // //protected ResourceReference wicketAjaxReference;
    //
    // //protected ResourceReference wicketAjaxDebugReference;
    //
    public WicketResourceReferences() {
        this(true);
    }

    //
    public WicketResourceReferences(boolean init) {
        if (init) {
            // setJQueryReference(JQuery.JS/* JQueryResourceReference.get() */);
            // setJQueryReference(JQuery.JS/* JQueryResourceReference.get() */);
            // setWicketEventReference(WicketEventJQueryResourceReference.get());
            // setWicketAjaxReference(WicketAjaxJQueryResourceReference.get());
            // setWicketAjaxDebugReference(WicketAjaxDebugJQueryResourceReference.get());
        }
    }
    //
    // @Override
    // public ResourceReference getJQueryReference() {
    // return this.jQueryReference;
    // }
    //
    // @Override
    // public ResourceReference getWicketEventReference() {
    // return this.wicketEventReference;
    // }
    //
    // @Override
    // public ResourceReference getWicketAjaxReference() {
    // return this.wicketAjaxReference;
    // }
    //
    // @Override
    // public ResourceReference getWicketAjaxDebugReference() {
    // return this.wicketAjaxDebugReference;
    // }
    //
    // @Override
    // public JavaScriptLibrarySettings setJQueryReference(ResourceReference jQueryReference) {
    // this.jQueryReference = jQueryReference;
    // return this;
    // }
    //
    // @Override
    // public JavaScriptLibrarySettings setWicketEventReference(ResourceReference wicketEventReference) {
    // this.wicketEventReference = wicketEventReference;
    // return this;
    // }
    //
    // @Override
    // public JavaScriptLibrarySettings setWicketAjaxReference(ResourceReference wicketAjaxReference) {
    // this.wicketAjaxReference = wicketAjaxReference;
    // return this;
    // }
    //
    // @Override
    // public JavaScriptLibrarySettings setWicketAjaxDebugReference(ResourceReference wicketAjaxDebugReference) {
    // this.wicketAjaxDebugReference = wicketAjaxDebugReference;
    // return this;
    // }
}
