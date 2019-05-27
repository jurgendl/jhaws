package org.tools.hqlbuilder.webservice.jquery.ui.picturefill;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://scottjehl.github.io/picturefill/
// 3.0.2
public class PictureFill {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(PictureFill.class, "picturefill.js");

    public static final String FACTORY = ";document.createElement('picture');";
}
