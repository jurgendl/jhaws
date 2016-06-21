package org.jhaws.common.net.client.obsolete;

import java.net.URI;
import java.util.HashMap;

import org.apache.http.client.methods.HttpRequestBase;
import org.jhaws.common.io.IOFile;

/**
 * HTTPClientListener
 */
@SuppressWarnings("deprecation")
@Deprecated
public interface HTTPClientListener {
	public void afterExecute(HTTPClient client, URI url, HttpRequestBase httpRequest, Response response);

	public void beforeExecute(HTTPClient client, URI url, HttpRequestBase httpRequest);

	public void beforeMethod(HTTPClient client, String type, String url, HashMap<String, String> formValues, HashMap<String, IOFile> attachments);
}
