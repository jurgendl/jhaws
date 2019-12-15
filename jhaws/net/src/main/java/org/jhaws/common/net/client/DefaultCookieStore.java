package org.jhaws.common.net.client;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.jhaws.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultCookieStore implements org.apache.http.client.CookieStore, Externalizable {
	/**
	 * deserialize
	 */
	public static DefaultCookieStore deserialize(InputStream in) {
		try {
			try (ObjectInputStream encoder = new ObjectInputStream(in)) {
				Object object = encoder.readObject();
				encoder.close();
				return (DefaultCookieStore) object;
			} catch (ClassNotFoundException ex) {
				throw new IOException(ex);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * xml deserialize
	 */
	public static DefaultCookieStore xmldeserialize(InputStream in) {
		try (XMLDecoder decoder = new XMLDecoder(in)) {
			Integer count = (Integer) decoder.readObject();
			DefaultCookieStore store = new DefaultCookieStore();
			for (int i = 0; i < count; i++) {
				Cookie readObject = (Cookie) decoder.readObject();
				store.addCookie(readObject);
			}
			decoder.close();
			return store;
		}
	}

	public static DefaultCookieStore jsondeserialize(InputStream in) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<SerializableCookie> cc = objectMapper.readValue(in, new TypeReference<List<SerializableCookie>>() {
				/**/});
			DefaultCookieStore store = new DefaultCookieStore();
			cc.forEach(store::addCookie);
			return store;
		} catch (JsonParseException ex) {
			throw new RuntimeException(ex);
		} catch (JsonMappingException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	protected static final Logger logger = LoggerFactory.getLogger(DefaultCookieStore.class);

	/** backing store */
	protected transient CookieStore cookieStore = new BasicCookieStore();

	/** interceptors */
	protected transient List<CookieStoreInterceptor<DefaultCookieStore>> cookieStoreInterceptors = new ArrayList<>();

	public DefaultCookieStore() {
		super();
	}

	public DefaultCookieStore(org.apache.http.client.CookieStore source) {
		setParentCookieStore(source);
	}

	public void setParentCookieStore(org.apache.http.client.CookieStore source) {
		source.getCookies().forEach(this::addCookie);
	}

	/**
	 *
	 * @see org.apache.http.client.CookieStore#addCookie(org.apache.http.cookie.Cookie)
	 */
	@Override
	public synchronized void addCookie(Cookie cookie) {
		for (CookieStoreInterceptor<DefaultCookieStore> interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeAddCookie(this);
		}
		this.cookieStore.addCookie(cookie);
	}

	public void addCookieStoreInterceptor(CookieStoreInterceptor<DefaultCookieStore> interceptor) {
		this.cookieStoreInterceptors.add(interceptor);
	}

	/**
	 *
	 * @see org.apache.http.client.CookieStore#clear()
	 */
	@Override
	public synchronized void clear() {
		for (CookieStoreInterceptor<DefaultCookieStore> interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeClear(this);
		}

		this.cookieStore.clear();
	}

	public void clearCookieStoreInterceptors() {
		this.cookieStoreInterceptors.clear();
	}

	/**
	 *
	 * @see org.apache.http.client.CookieStore#clearExpired(java.util.Date)
	 */
	@Override
	public synchronized boolean clearExpired(Date date) {
		for (CookieStoreInterceptor<DefaultCookieStore> interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeClearExpired(this, date);
		}

		return this.cookieStore.clearExpired(date);
	}

	/**
	 *
	 * @see org.apache.http.client.CookieStore#getCookies()
	 */
	@Override
	public synchronized List<Cookie> getCookies() {
		for (CookieStoreInterceptor<DefaultCookieStore> interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeGetCookies(this);
		}
		return this.cookieStore.getCookies();
	}

	public CookieStore getCookieStore() {
		return this.cookieStore;
	}

	/**
	 *
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) {
		try {
			int readInt = in.readInt();
			for (int i = 0; i < readInt; i++) {
				SerializableCookie sc;
				try {
					sc = (SerializableCookie) in.readObject();
				} catch (ClassNotFoundException ex) {
					throw new IOException(ex);
				}
				this.addCookie(sc);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	protected String readUTF(ObjectInput in) {
		try {
			int size = in.readInt();
			if (size > 0) {
				byte[] buffer = new byte[size];
				in.read(buffer);
				return new String(buffer, StringUtils.UTF8);
			}
			return null;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public void removeCookieStoreInterceptor(CookieStoreInterceptor<DefaultCookieStore> interceptor) {
		this.cookieStoreInterceptors.remove(interceptor);
	}

	public DefaultCookieStore serialize(OutputStream out) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
			encoder.writeObject(this);
			return this;
		}
	}

	public void setCookieStore(org.apache.http.client.CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	/**
	 *
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Cookie[] cookies = this.getCookies().toArray(new Cookie[0]);
		out.writeInt(cookies.length);
		for (Cookie c : cookies) {
			out.writeObject(new SerializableCookie(c));
		}
	}

	protected void writeUTF(ObjectOutput out, String value) throws IOException {
		if ((value != null) && (value.length() > 0)) {
			byte[] bytes = value.getBytes(StringUtils.UTF8);
			out.writeInt(bytes.length);
			out.write(bytes);
		} else {
			out.writeInt(0);
		}
	}

	public DefaultCookieStore xmlserialize(OutputStream out) {
		try (XMLEncoder encoder = new XMLEncoder(out)) {
			Cookie[] cookies = this.getCookies().toArray(new Cookie[0]);
			encoder.writeObject(cookies.length);
			for (Cookie c : cookies) {
				encoder.writeObject(new SerializableCookie(c));
			}
			return this;
		}
	}

	public DefaultCookieStore jsonserialize(OutputStream out) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(out, getCookies());
			return this;
		} catch (JsonGenerationException ex) {
			throw new RuntimeException(ex);
		} catch (JsonMappingException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
