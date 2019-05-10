package org.tools.hqlbuilder.webservice.prismjs;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// <pre><code class="language-xxx">...</code></pre>
public class PrismJs {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(PrismJs.class, "prism.js");

    public static final CssResourceReference CSS = new CssResourceReference(PrismJs.class, "prism.css");
}
