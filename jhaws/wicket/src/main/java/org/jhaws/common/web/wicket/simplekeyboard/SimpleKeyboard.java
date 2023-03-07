package org.jhaws.common.web.wicket.simplekeyboard;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://hodgef.com/simple-keyboard/getting-started/
// v3.5.26
public class SimpleKeyboard {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(SimpleKeyboard.class, "index.js");

	public static CssResourceReference CSS = new CssResourceReference(SimpleKeyboard.class, "index.css");

}
