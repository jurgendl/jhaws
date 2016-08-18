package org.tools.hqlbuilder.webservice.jquery.ui.flowplayer.plugins.easybits_httpstreaming_flowplayer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.tools.hqlbuilder.webservice.jquery.ui.flowplayer.FlowPlayer;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketUtils;


/**
 * @see http://www.easy-bits.com/products/http-streaming-for-flash/flowplayer
 */
public class EasybitsFlowplayerHttpstreaming {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(EasybitsFlowplayerHttpstreaming.class, "easybits-flash-streaming.js");

	public static JavaScriptResourceReference JS2 = new JavaScriptResourceReference(EasybitsFlowplayerHttpstreaming.class, "easybits-helper.js");

	public static JavaScriptResourceReference JS3 = new JavaScriptResourceReference(EasybitsFlowplayerHttpstreaming.class, "easybits-multistreaming.js");

	static {
		JS//
		.addJavaScriptResourceReferenceDependency(JS2)//
				.addJavaScriptResourceReferenceDependency(JS3)//
				.addJavaScriptResourceReferenceDependency(FlowPlayer.JS);//
	}

	public static String swf = "easybits-httpstreaming-flowplayer-plugin.swf";

	private static String url = null;

	public static String url() {
		if (url == null) {
			url = WicketUtils.mountStream(swf, new StreamResourceReference(EasybitsFlowplayerHttpstreaming.class, swf) {
				private static final long serialVersionUID = -627759054701557680L;

				@Override
				public IResourceStream getResourceStream() {
					return new AbstractResourceStream() {
						private static final long serialVersionUID = -2733832263859249860L;

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
									inputStream = EasybitsFlowplayerHttpstreaming.class.getClassLoader().getResourceAsStream(EasybitsFlowplayerHttpstreaming.class.getPackage().getName().replace('.', '/') + "/" + swf);
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
}
