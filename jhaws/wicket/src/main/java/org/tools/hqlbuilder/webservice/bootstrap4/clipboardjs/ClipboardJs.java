package org.tools.hqlbuilder.webservice.bootstrap4.clipboardjs;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// <div class="input-group mb-3">
// <input id="clipboardtarget" type="text" class="form-control">
// <div class="input-group-append">
// <button data-clipboard-target="#clipboardtarget" class="btn btn-outline-secondary clipboard" type="button">
// <i class="fa-fw fas fa-paste"></i>
// </button>
// </div>
// </div>
public class ClipboardJs {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(ClipboardJs.class, "clipboard.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }

    public static final String CLASS = "clipboard";

    public static final String FACTORY = //
            ";var clipboard = new ClipboardJS('." + CLASS + "');"//
                    + "clipboard.on('success', function(e) {\n" + //
                    "    console.info('Action:', e.action);\n" + //
                    "    console.info('Text:', e.text);\n" + //
                    "    console.info('Trigger:', e.trigger);\n" + //
                    "\n" + //
                    "    e.clearSelection();\n" + //
                    "});\n" + //
                    "\n" + //
                    "clipboard.on('error', function(e) {\n" + //
                    "    console.error('Action:', e.action);\n" + //
                    "    console.error('Trigger:', e.trigger);\n" + //
                    "});";
}
