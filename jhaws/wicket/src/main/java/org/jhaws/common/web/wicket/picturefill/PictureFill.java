package org.jhaws.common.web.wicket.picturefill;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://scottjehl.github.io/picturefill/
// 3.0.2
public class PictureFill {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(PictureFill.class, "picturefill.js");

    public static final String FACTORY = ";document.createElement('picture');";
}
