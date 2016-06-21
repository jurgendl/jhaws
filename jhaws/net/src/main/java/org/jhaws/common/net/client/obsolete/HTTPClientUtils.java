package org.jhaws.common.net.client.obsolete;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

@SuppressWarnings("deprecation")
@Deprecated
public class HTTPClientUtils extends org.jhaws.common.net.client.HTTPClientUtils {
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
