package org.tools.hqlbuilder.webservice.jquery.ui.ckeditor;

import java.util.Locale;

import org.apache.wicket.request.Url;
import org.tools.hqlbuilder.webservice.wicket.UrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * <code>$( '#ckeditor' ).ckeditor();</code>
 *
 * @see http://docs.ckeditor.com/
 */
public class CKEditor {
    public static enum CKEType {
        full, standard, basic;
    }

    public static final String VERSION = "4.4.4";

    public static final String URL = "http://cdn.ckeditor.com/" + VERSION + "/";

    public static final String CKEDITOR_JS = "ckeditor.js";

    public static final String ADAPTERS_JQUERY_JS = "adapters/jquery.js";

    public static UrlResourceReference CDN_CKEDITOR_FULL_JS = new UrlResourceReference(Url.parse(URL + CKEType.full.toString() + "/" + CKEDITOR_JS));

    public static UrlResourceReference CDN_CKEDITOR_STANDARD_JS = new UrlResourceReference(Url.parse(URL + CKEType.standard.toString() + "/"
            + CKEDITOR_JS));

    public static UrlResourceReference CDN_CKEDITOR_BASIC_JS = new UrlResourceReference(Url.parse(URL + CKEType.basic.toString() + "/" + CKEDITOR_JS));

    public static UrlResourceReference CDN_CKEDITOR_FULL_JQUERY_JS = new UrlResourceReference(Url.parse(URL + CKEType.full.toString() + "/"
            + ADAPTERS_JQUERY_JS)).addJavaScriptResourceReferenceDependency(CDN_CKEDITOR_FULL_JS);

    public static UrlResourceReference CDN_CKEDITOR_STANDARD_JQUERY_JS = new UrlResourceReference(Url.parse(URL + CKEType.standard.toString() + "/"
            + ADAPTERS_JQUERY_JS)).addJavaScriptResourceReferenceDependency(CDN_CKEDITOR_STANDARD_JS);

    public static UrlResourceReference CDN_CKEDITOR_BASIC_JQUERY_JS = new UrlResourceReference(Url.parse(URL + CKEType.basic.toString() + "/"
            + ADAPTERS_JQUERY_JS)).addJavaScriptResourceReferenceDependency(CDN_CKEDITOR_BASIC_JS);

    static {
        try {
            CDN_CKEDITOR_FULL_JQUERY_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings()
                    .getJQueryReference());
            CDN_CKEDITOR_STANDARD_JQUERY_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings()
                    .getJQueryReference());
            CDN_CKEDITOR_BASIC_JQUERY_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings()
                    .getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static UrlResourceReference forType(CKEType type) {
        UrlResourceReference ref;
        switch (type) {
            case basic:
                ref = CDN_CKEDITOR_BASIC_JQUERY_JS;
                break;
            default:
            case standard:
                ref = CDN_CKEDITOR_STANDARD_JQUERY_JS;
                break;
            case full:
                ref = CDN_CKEDITOR_FULL_JQUERY_JS;
                break;
        }
        return ref;
    }

    public static UrlResourceReference forLanguage(CKEType type, Locale locale) {
        return new UrlResourceReference(Url.parse(URL + type.toString() + "/lang/" + locale.getLanguage() + ".js"))
                .addJavaScriptResourceReferenceDependency(forType(type));
    }
}
