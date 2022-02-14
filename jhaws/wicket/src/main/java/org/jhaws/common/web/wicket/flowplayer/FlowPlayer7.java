package org.jhaws.common.web.wicket.flowplayer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.StreamResourceReference;
import org.jhaws.common.web.wicket.WicketUtils;
import org.jhaws.common.web.wicket.jquery.JQuery;

// https://flowplayer.com/
// https://flowplayer.com/docs/player/skinning
// https://flowplayer.com/demos
// https://flowplayer.com/docs
// https://flowplayer.com/player
// https://flowplayer.com/demos/showcase/complete
// 7.2.6
@SuppressWarnings("serial")
public class FlowPlayer7 {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(FlowPlayer7.class, "flowplayer-7.2.6/flowplayer.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
    }

    public static JavaScriptResourceReference JS_HLS = new JavaScriptResourceReference(FlowPlayer7.class, "flowplayer-7.2.6/flowplayer.hlsjs.js");

    static {
        JS_HLS.addJavaScriptResourceReferenceDependency(JS);
    }

    public static CssResourceReference SKIN_CSS = new CssResourceReference(FlowPlayer7.class, "flowplayer-7.2.6/skin/skin.css");

    public static String swf = "flowplayer.swf";

    private static String url = null;

    public static String url() {
        if (url == null) {
            url = WicketUtils.mountStream(FlowPlayer7.swf, new StreamResourceReference(FlowPlayer7.class, FlowPlayer7.swf) {
                @Override
                public IResourceStream getResourceStream() {
                    return new AbstractResourceStream() {
                        protected transient InputStream inputStream;

                        @Override
                        public void close() throws IOException {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }

                        @Override
                        public InputStream getInputStream() throws ResourceStreamNotFoundException {
                            if (inputStream == null) {
                                try {
                                    inputStream = FlowPlayer7.class.getClassLoader().getResourceAsStream(FlowPlayer7.class.getPackage().getName().replace('.', '/') + "/" + swf);
                                } catch (Exception e) {
                                    throw new ResourceStreamNotFoundException(e);
                                }
                            }
                            return inputStream;
                        }
                    };
                }
            });
        }
        return url;
    }

    public static String swfhls = "flowplayerhls.swf";

    private static String urlhls = null;

    public static String urlhls() {
        if (urlhls == null) {
            urlhls = WicketUtils.mountStream(FlowPlayer7.swfhls, new StreamResourceReference(FlowPlayer7.class, FlowPlayer7.swfhls) {
                @Override
                public IResourceStream getResourceStream() {
                    return new AbstractResourceStream() {
                        protected transient InputStream inputStream;

                        @Override
                        public void close() throws IOException {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }

                        @Override
                        public InputStream getInputStream() throws ResourceStreamNotFoundException {
                            if (inputStream == null) {
                                try {
                                    inputStream = FlowPlayer7.class.getClassLoader().getResourceAsStream(FlowPlayer7.class.getPackage().getName().replace('.', '/') + "/" + swfhls);
                                } catch (Exception e) {
                                    throw new ResourceStreamNotFoundException(e);
                                }
                            }
                            return inputStream;
                        }
                    };
                }
            });
        }
        return urlhls;
    }

    /**
     * response.render(CssHeaderItem.forReference(FlowPlayer7.SKIN_CSS));<br>
     * response.render(JavaScriptHeaderItem.forReference(FlowPlayer7.JS));<br>
     * response.render(OnDomReadyHeaderItem.forScript(FlowPlayer7.javaScript("customflowplayer", true, true)));<br>
     */
    public static String javaScript(String tag, boolean loop, boolean splash) {
        return ";$('." + tag + "').flowplayer({"//
                + "});";
    }
}
