package org.tools.hqlbuilder.webservice.jquery.ui.blazy;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// @see http://dinbror.dk/blog/blazy/
// 1.3.1
public class BLazy {
    public static final String BLAZY_CLASS = "b-lazy";

    public static final String BLAZY_SRC = "data-src";

    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BLazy.class, "blazy.js");

    public static final CssResourceReference CSS = new CssResourceReference(BLazy.class, "blazy.css");

    public static final String FACTORY = ";new Blazy({offset:200});";
}
