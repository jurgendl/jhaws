package org.jhaws.common.web.wicket.tablesorter;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

/**
 * @see http://mottie.github.io/tablesorter/docs/index.html
 */
public class TableSorter {
    public static JavaScriptResourceReference TABLE_SORTER_JS = new JavaScriptResourceReference(TableSorter.class, "js/jquery.tablesorter.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static JavaScriptResourceReference TABLE_SORTER_WIDGETS_JS = new JavaScriptResourceReference(TableSorter.class, "js/jquery.tablesorter.widgets.js").addJavaScriptResourceReferenceDependency(TABLE_SORTER_JS);
}
