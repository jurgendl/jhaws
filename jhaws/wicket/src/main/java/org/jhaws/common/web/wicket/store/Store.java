package org.jhaws.common.web.wicket.store;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

/**
 * @see https://github.com/marcuswestin/store.js
 */
public class Store {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Store.class, "store.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
