package org.tools.hqlbuilder.webservice.jquery.ui.kendoui;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

/**
 * version 2014.2.716, only includes core
 *
 * @see http://www.telerik.com/download/kendo-ui-core
 * @see http://www.7thweb.net/wicket-jquery-ui/
 * @see http://www.telerik.com/kendo-ui
 */
public class KendoUI {
    public static CssResourceReference KENDO_COMMON_CSS = new CssResourceReference(KendoUI.class, "kendo.common.css");

    public static CssResourceReference KENDO_DEFAULT_CSS = new CssResourceReference(KendoUI.class, "kendo.default.css");
}
