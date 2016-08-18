package org.tools.hqlbuilder.webservice.jquery.ui.jquery_file_upload;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see https://github.com/blueimp/jQuery-File-Upload/wiki/Setup
 */
public class JQueryFileUpload {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload.js");

	public static final JavaScriptResourceReference JS_TRANSPORT = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.iframe-transport.js");

	public static final JavaScriptResourceReference JS_PROCESS = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-process.js");

	public static final JavaScriptResourceReference JS_IMAGE = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-image.js");

	public static final JavaScriptResourceReference JS_AUDIO = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-audio.js");

	public static final JavaScriptResourceReference JS_VIDEO = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-video.js");

	public static final JavaScriptResourceReference JS_VALIDATE = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-validate.js");

	public static final JavaScriptResourceReference JS_UI = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-ui.js");

	public static final JavaScriptResourceReference JS_JQUI = new JavaScriptResourceReference(JQueryFileUpload.class, "js/jquery.fileupload-jquery-ui.js");

	public static final CssResourceReference CSS = new CssResourceReference(JQueryFileUpload.class, "css/jquery.fileupload.css");

	public static final CssResourceReference CSS_JQUI = new CssResourceReference(JQueryFileUpload.class, "css/jquery.fileupload-ui.css");

	static {
		try {
			JS_JQUI.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
		} catch (Exception e) {
			//
		}
	}

	public static final JavaScriptResourceReference MAIN = new JavaScriptResourceReference(JQueryFileUpload.class, "js/main.js");
}
