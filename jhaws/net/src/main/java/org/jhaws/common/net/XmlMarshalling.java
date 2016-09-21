package org.jhaws.common.net;

import java.io.InputStream;

import org.jhaws.common.io.jaxb.ThreadLocalMarshalling;

public class XmlMarshalling {
	private final ThreadLocalMarshalling threadLocalMarshalling;

	public XmlMarshalling(Class<?>... types) {
		threadLocalMarshalling = new ThreadLocalMarshalling(Class.class.cast(types));
	}

	public String marshall(Object body, String charSet) {
		return threadLocalMarshalling.marshall(body, charSet);
	}

	public <T> T unmarshall(Class<T> type, byte[] body) {
		return threadLocalMarshalling.<T>unmarshall(body);
	}

	public <T> T unmarshall(Class<T> type, String body, String charSet) {
		return threadLocalMarshalling.<T>unmarshall(body, charSet);
	}

	public <T> T unmarshall(Class<T> type, InputStream body) {
		return threadLocalMarshalling.<T>unmarshall(body);
	}
}
