package org.jhaws.common.web.wicket.rating;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

public class Rating {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Rating.class, "js/star-rating.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static CssResourceReference CSS = new CssResourceReference(Rating.class, "css/star-rating.css");
}
