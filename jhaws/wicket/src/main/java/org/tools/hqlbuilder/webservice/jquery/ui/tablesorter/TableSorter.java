package org.tools.hqlbuilder.webservice.jquery.ui.tablesorter;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://mottie.github.io/tablesorter/docs/index.html
 */
public class TableSorter {
    public static JavaScriptResourceReference TABLE_SORTER_JS = new JavaScriptResourceReference(TableSorter.class, "js/jquery.tablesorter.js")
    .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static JavaScriptResourceReference TABLE_SORTER_WIDGETS_JS = new JavaScriptResourceReference(TableSorter.class,
            "js/jquery.tablesorter.widgets.js").addJavaScriptResourceReferenceDependency(TABLE_SORTER_JS);
}
