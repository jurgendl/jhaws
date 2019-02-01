package org.tools.hqlbuilder.webservice.bootstrap4.customfileinput;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://github.com/Johann-S/bs-custom-file-input
// v1.3.1
// init: bsCustomFileInput.init();
public class CustomFileInput {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(CustomFileInput.class, "bs-custom-file-input.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
