package org.jhaws.common.web.resteasy;

import javax.ws.rs.core.MediaType;

public interface RestResource {
	public static final String FORM_MULTIPART_DATA = MediaType.MULTIPART_FORM_DATA;

	public static final String FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED;

	public static final String TEXT = MediaType.TEXT_PLAIN;

	public static final String JSON = MediaType.APPLICATION_JSON;

	public static final String HTML = MediaType.TEXT_HTML;

	public static final String BINARY = MediaType.APPLICATION_OCTET_STREAM;

	public static final String XML = MediaType.TEXT_XML;

	public static final String EXTENSION_TEXT = "txt";

	public static final String EXTENSION_JSON = "json";

	public static final String EXTENSION_HTML = "html";

	public static final String EXTENSION_BINARY = "bin";

	public static final String EXTENSION_XML = "xml";
}
