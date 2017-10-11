package org.tools.hqlbuilder.webservice.jquery.ui.blazy;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// @see http://dinbror.dk/blog/blazy/
// 1.3.1
public class BLazy {
    public static final String BLAZY_CLASS = "b-lazy";

    public static final String BLAZY_SRC = "data-src";

    public static final JavaScriptResourceReference BLAZY_JS = new JavaScriptResourceReference(BLazy.class, "blazy.js");

    public static final CssResourceReference BLAZY_CSS = new CssResourceReference(BLazy.class, "blazy.css");

    public static final OnDomReadyHeaderItem BLAZY_FACTORY_JS = OnDomReadyHeaderItem.forScript(";new Blazy({offset:200});");
}
