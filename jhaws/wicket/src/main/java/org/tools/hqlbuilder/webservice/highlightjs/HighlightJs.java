package org.tools.hqlbuilder.webservice.highlightjs;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://highlightjs.org/download/
// https://highlightjs.readthedocs.io/en/latest/css-classes-reference.html
// https://highlightjs.org/static/demo/
// npm install highlight.js
//
// https://alistapart.com/article/alternate/
public class HighlightJs {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(HighlightJs.class, "highlight.pack.js");

    public static final CssResourceReference CSS = new CssResourceReference(HighlightJs.class, "styles/default.css");
}
