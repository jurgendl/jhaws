package org.jhaws.common.net.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.client.utils.URIUtils;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.net.client.cookies.CookieStore;

@SuppressWarnings("deprecation")
public class HTTPClientUtils {
	/** mimeTypes */
	protected static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

	/**
	 * build relative url
	 */
	public static String relativeURL(String original, String newRelative) {
		try {
			return URIUtils.resolve(new URI(original), newRelative).toString();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * build relative url
	 */
	public static URL relativeURL(URL original, String newRelative) {
		try {
			return URIUtils.resolve(original.toURI(), newRelative).toURL();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * build relative url
	 */
	public static URI relativeURL(URI original, String newRelative) {
		return URIUtils.resolve(original, newRelative);
	}

	public static synchronized String getMimeType(IOFile file) {
		return mimeTypes.getContentType(file);
	}

	public static HTTPClient deserialize(InputStream in) throws IOException, ClassNotFoundException {
		try (ObjectInputStream encoder = new ObjectInputStream(in)) {
			Object object = encoder.readObject();
			CookieStore cs = (CookieStore) encoder.readObject();
			encoder.close();
			HTTPClient hc = (HTTPClient) object;
			hc.setCookieStore(cs);

			return hc;
		}
	}

	public static void serialize(HTTPClient client, OutputStream out) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
			encoder.writeObject(client);
			encoder.writeObject(client.getCookieStore());
			encoder.close();
		}
	}
}
