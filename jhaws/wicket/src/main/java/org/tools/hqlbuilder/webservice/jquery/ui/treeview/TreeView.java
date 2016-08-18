package org.tools.hqlbuilder.webservice.jquery.ui.treeview;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://www.dynamicdrive.com/dynamicindex1/treeview/
 */
public class TreeView {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(TreeView.class, "jquery.treeview.js");

	static {
		try {
			TreeView.JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
		} catch (Exception ex) {
			//
		}
	}

	public static final CssResourceReference CSS = new CssResourceReference(TreeView.class, "jquery.treeview.css");
}
