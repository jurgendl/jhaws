package org.jhaws.common.web.wicket.videojs;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;

// http://videojs.com/getting-started/
// http://videojs.com/plugins/
// http://docs.videojs.com/tutorial-options.html#loop
// https://videojs.com/plugins/
// 7.5.5
public class VideoJs7 {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(VideoJs7.class,
			"video-js-7.5.5/video.js");

	static {
		JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
	}

	public static JavaScriptResourceReference JS_PREVENT_MULTIPLE = new JavaScriptResourceReference(VideoJs7.class,
			"videojs-prevent-multiple.js");

	static {
		JS_PREVENT_MULTIPLE.addJavaScriptResourceReferenceDependency(JS);
	}

	public static CssResourceReference CSS = new CssResourceReference(VideoJs7.class, "video-js-7.5.5/video-js.css");
}
