package org.tools.hqlbuilder.webservice.bootstrap4.tags;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// bootstrap v3 http://bootstrap-tagsinput.github.io/bootstrap-tagsinput/examples/
// bootstrap v4 (modified from bootstrap v3) https://github.com/Nodws/bootstrap4-tagsinput
public class BootstrapTags {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTags.class, "tagsinput.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapTags.class, "tagsinput.css");
}
