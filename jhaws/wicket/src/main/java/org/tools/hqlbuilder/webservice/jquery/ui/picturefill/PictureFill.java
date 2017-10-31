package org.tools.hqlbuilder.webservice.jquery.ui.picturefill;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://scottjehl.github.io/picturefill/
// 3.0.2
public class PictureFill {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(PictureFill.class, "picturefill.js");

    public static final OnDomReadyHeaderItem FACTORY = OnDomReadyHeaderItem.forScript(";document.createElement('picture');");
}
