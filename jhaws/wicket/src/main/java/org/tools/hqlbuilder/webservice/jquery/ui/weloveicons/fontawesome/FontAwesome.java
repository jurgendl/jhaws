package org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://fontawesome.com/get-started
// https://fontawesome.com/v4.7.0/
// https://fontawesome.com/how-to-use/upgrading-from-4
// https://fontawesome.com/get-started/svg-with-js
// https://fontawesome.com/icons?d=gallery
// https://fontawesome.com/how-to-use/upgrading-from-4#icon-name-changes-complete-list
// https://fontawesome.com/how-to-use/svg-with-js
// 4.7.0
// 5.0.11
public class FontAwesome {
    public static CssResourceReference CSS4 = new CssResourceReference(FontAwesome.class, "font-awesome-4.7.0/css/font-awesome.css");

    public static CssResourceReference CSS5 = new CssResourceReference(FontAwesome.class,
            "fontawesome-free-5.0.11/web-fonts-with-css/css/fontawesome-all.css");

    public static CssResourceReference CSS5SVG = new CssResourceReference(FontAwesome.class,
            "fontawesome-free-5.0.11/svg-with-js/css/fa-svg-with-js.css");

    public static JavaScriptResourceReference JS5SVG = new JavaScriptResourceReference(FontAwesome.class,
            "fontawesome-free-5.0.11/svg-with-js/js/fontawesome-all.js");

    public static JavaScriptResourceReference JS5SVG_4SHIM = new JavaScriptResourceReference(FontAwesome.class,
            "fontawesome-free-5.0.11/svg-with-js/js/fa-v4-shims.js").addJavaScriptResourceReferenceDependency(JS5SVG);
}
