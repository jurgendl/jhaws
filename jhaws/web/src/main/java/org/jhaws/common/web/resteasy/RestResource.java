package org.jhaws.common.web.resteasy;

import javax.ws.rs.core.MediaType;

public interface RestResource {
	public static final String D = ".";

	public static final String INTERNET_SHORTCUT_URL = "[InternetShortcut]\nURL=";

	public static final String URL_EXTENSION = "url";

	public static final String FORM_MULTIPART = MediaType.MULTIPART_FORM_DATA;

	public static final String FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED;

	public static final String TEXT = MediaType.TEXT_PLAIN;

	public static final String JSON = MediaType.APPLICATION_JSON;

	public static final String HTML = MediaType.TEXT_HTML;

	public static final String BINARY = MediaType.APPLICATION_OCTET_STREAM;

	public static final String XML = MediaType.TEXT_XML;

	public static final String TEXT_EXTENSION = "txt";

	public static final String JSON_EXTENSION = "json";

	public static final String HTML_EXTENSION = "html";

	public static final String BINARY_EXTENSION = "bin";

	public static final String XML_EXTENSION = "xml";
}
