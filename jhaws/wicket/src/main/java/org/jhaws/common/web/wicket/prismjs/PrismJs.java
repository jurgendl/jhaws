package org.jhaws.common.web.wicket.prismjs;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// v1.16.0
// <pre><code class="language-xxx">...</code></pre>
public class PrismJs {
    public static final JavaScriptResourceReference JS_ALL_LANGUAGES_ALL_PLUGINS = new JavaScriptResourceReference(PrismJs.class, "dist/prism-all.js");

    public static final JavaScriptResourceReference JS_ALL_LANGUAGES_NO_PLUGINS = new JavaScriptResourceReference(PrismJs.class, "dist/prism-limited.js");

    public static final CssResourceReference CSS = new CssResourceReference(PrismJs.class, "dist/prism.css");
}
