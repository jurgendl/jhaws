package org.jhaws.common.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebUtilities {
	public static final String IMAGE_PLACEHOLDER = "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==";

	public static URI convert(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
