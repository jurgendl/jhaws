package org.jhaws.common.web.wicket.videojs;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// http://videojs.com/getting-started/
// http://videojs.com/plugins/
// http://docs.videojs.com/tutorial-options.html#loop
// https://videojs.com/plugins/
// 7.9.1
public class VideoJs7 {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(VideoJs7.class, "video-js-7.9.1/video.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
    }

    public static JavaScriptResourceReference JS_PREVENT_MULTIPLE = new JavaScriptResourceReference(VideoJs7.class, "videojs-prevent-multiple.js");

    // https://www.npmjs.com/package/videojs-sprite-thumbnails/v/0.6.0
    // https://codepen.io/giriaakula/pen/dyNjRmW
    // https://unpkg.com/browse/videojs-sprite-thumbnails@0.6.0/dist/
    // https://github.com/phloxic/videojs-sprite-thumbnails#installation
    public static JavaScriptResourceReference JS_SPRITE = new JavaScriptResourceReference(VideoJs7.class, "sprite-thumbnails/videojs-sprite-thumbnails.js");

    static {
        JS_PREVENT_MULTIPLE.addJavaScriptResourceReferenceDependency(JS);
        JS_SPRITE.addJavaScriptResourceReferenceDependency(JS);
    }

    public static CssResourceReference CSS = new CssResourceReference(VideoJs7.class, "video-js-7.9.1/video-js.css");
}
