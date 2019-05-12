package org.tools.hqlbuilder.webservice.prismjs;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// v1.16.0
// <pre><code class="language-xxx">...</code></pre>
public class PrismJs {
    public static final JavaScriptResourceReference JS_ALL_LANGUAGES_ALL_PLUGINS = new JavaScriptResourceReference(PrismJs.class,
            "dist/prism-all.js");

    public static final JavaScriptResourceReference JS_ALL_LANGUAGES_NO_PLUGINS = new JavaScriptResourceReference(PrismJs.class,
            "dist/prism-limited.js");

    public static final CssResourceReference CSS = new CssResourceReference(PrismJs.class, "dist/prism.css");
}