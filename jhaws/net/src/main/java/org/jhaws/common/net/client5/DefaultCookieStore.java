package org.jhaws.common.net.client5;

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

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jhaws.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCookieStore implements org.apache.hc.client5.http.cookie.CookieStore, Externalizable {
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

	protected static final Logger logger = LoggerFactory.getLogger(DefaultCookieStore.class);

	/** backing store */
	protected transient CookieStore cookieStore = new BasicCookieStore();

	/** interceptors */
	protected transient List<CookieStoreInterceptor> cookieStoreInterceptors = new ArrayList<>();

	public DefaultCookieStore() {
		super();
	}

	public DefaultCookieStore(org.apache.hc.client5.http.cookie.CookieStore source) {
		setParentCookieStore(source);
	}

	public void setParentCookieStore(org.apache.hc.client5.http.cookie.CookieStore source) {
		source.getCookies().forEach(this::addCookie);
	}

	/**
	 *
	 * @see org.apache.hc.client5.http.cookie.CookieStore#addCookie(org.apache.http.cookie.Cookie)
	 */
	@Override
	public synchronized void addCookie(Cookie cookie) {
		for (CookieStoreInterceptor interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeAddCookie(this);
		}
		this.cookieStore.addCookie(cookie);
	}

	public void addCookieStoreInterceptor(CookieStoreInterceptor interceptor) {
		this.cookieStoreInterceptors.add(interceptor);
	}

	/**
	 *
	 * @see org.apache.hc.client5.http.cookie.CookieStore#clear()
	 */
	@Override
	public synchronized void clear() {
		for (CookieStoreInterceptor interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeClear(this);
		}

		this.cookieStore.clear();
	}

	public void clearCookieStoreInterceptors() {
		this.cookieStoreInterceptors.clear();
	}

	/**
	 *
	 * @see org.apache.hc.client5.http.cookie.CookieStore#clearExpired(java.util.Date)
	 */
	@Override
	public synchronized boolean clearExpired(Date date) {
		for (CookieStoreInterceptor interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeClearExpired(this, date);
		}

		return this.cookieStore.clearExpired(date);
	}

	/**
	 *
	 * @see org.apache.hc.client5.http.cookie.CookieStore#getCookies()
	 */
	@Override
	public synchronized List<Cookie> getCookies() {
		for (CookieStoreInterceptor interceptor : this.cookieStoreInterceptors) {
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

	public void removeCookieStoreInterceptor(CookieStoreInterceptor interceptor) {
		this.cookieStoreInterceptors.remove(interceptor);
	}

	public DefaultCookieStore serialize(OutputStream out) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
			encoder.writeObject(this);
			return this;
		}
	}

	public void setCookieStore(org.apache.hc.client5.http.cookie.CookieStore cookieStore) {
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
}
