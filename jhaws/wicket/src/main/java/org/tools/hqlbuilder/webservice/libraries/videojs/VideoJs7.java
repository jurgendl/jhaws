package org.tools.hqlbuilder.webservice.libraries.videojs;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://videojs.com/getting-started/
// http://videojs.com/plugins/
// http://docs.videojs.com/tutorial-options.html#loop
// 7.0.2
public class VideoJs7 {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(VideoJs7.class,
			"video-js-7.0.2/video.js");

	static {
		JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
	}

	public static CssResourceReference CSS = new CssResourceReference(VideoJs7.class, "video-js-7.0.2/video-js.css");
}
