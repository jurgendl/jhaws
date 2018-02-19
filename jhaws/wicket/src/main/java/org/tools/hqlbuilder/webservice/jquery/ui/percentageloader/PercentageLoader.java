package org.tools.hqlbuilder.webservice.jquery.ui.percentageloader;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// <div id="topLoader"></div><button id="animateButton">Animate Loader</button>
//
//
// var $topLoader = $("#topLoader").percentageLoader({
// width : 200,
// height : 200,
// controllable : false,
// progress : 0.0,
// onProgressUp : function(val) {
// $topLoader.setValue(Math.round(val * 100.0));
// }
// });
//
// var topLoaderRunning = false;
// $("#animateButton").click(function() {
// if (topLoaderRunning) {
// return;
// }
// topLoaderRunning = true;
//
// var kb = 0;
// var totalKb = 999;
//
// $topLoader.setProgress(kb / totalKb);
// $topLoader.setValue(kb.toString()+' / 999 kb');
//
// var animateFunc = function() {
// kb += 17;
// $topLoader.setProgress(kb / totalKb);
// $topLoader.setValue(kb.toString() + ' / 999 kb');
//
// if (kb < totalKb) {
// setTimeout(animateFunc, 25);
// } else {
// topLoaderRunning = false;
// }
// }
//
// setTimeout(animateFunc, 25);
// });

/**
 * @see https://bitbucket.org/Better2Web/jquery.percentageloader/downloads
 * @see http://widgets.better2web.com/loader/
 * @see http://www.jqueryscript.net/loading/Percentage-Loader-jQuery-Progress-Bar-Plugin.html
 */
public class PercentageLoader {
    public static final JavaScriptResourceReference PercentageLoader_JS = new JavaScriptResourceReference(PercentageLoader.class,
            "jquery.percentageloader-0.1.js");

    static {
        try {
            PercentageLoader_JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static final CssResourceReference PercentageLoader_CSS = new CssResourceReference(PercentageLoader.class,
            "jquery.percentageloader-0.1.css");
}
