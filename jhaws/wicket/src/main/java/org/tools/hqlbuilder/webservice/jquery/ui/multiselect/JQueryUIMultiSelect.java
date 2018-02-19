package org.tools.hqlbuilder.webservice.jquery.ui.multiselect;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * TODO locale
 *
 * ;$('.multiselect').multiselect({header:true,selectedList:10}).multiselectfilter();
 *
 * @see https://github.com/ehynds/jquery-ui-multiselect-widget
 * @see http://www.erichynds.com/examples/jquery-ui-multiselect-widget/demos/#basic
 */
public class JQueryUIMultiSelect {
    public static JavaScriptResourceReference JQueryUIMultiSelect_JS = new JavaScriptResourceReference(JQueryUIMultiSelect.class,
            "jquery.multiselect.js");

    static {
        try {
            JQueryUIMultiSelect_JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }

    public static JavaScriptResourceReference JQueryUIMultiSelect_Filter_JS = new JavaScriptResourceReference(JQueryUIMultiSelect.class,
            "jquery.multiselect.filter.js").addJavaScriptResourceReferenceDependency(JQueryUIMultiSelect_JS);

    public static CssResourceReference JQueryUIMultiSelect_CSS = new CssResourceReference(JQueryUIMultiSelect.class, "jquery.multiselect.css");
}
