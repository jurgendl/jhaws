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
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.jhaws.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class CookieStore implements org.apache.http.client.CookieStore, Externalizable {
	public static class SerializableCookie implements SetCookie, Serializable {
		private Date expiryDate;

		private String comment;

		private String domain;

		private String name;

		private String path;

		private String value;

		private boolean isSecure;

		private int version;

		public SerializableCookie() {
			super();
		}

		public SerializableCookie(Cookie cookie) {
			this.setComment(cookie.getComment());
			this.setDomain(cookie.getDomain());
			this.setExpiryDate(cookie.getExpiryDate());
			this.setName(cookie.getName());
			this.setPath(cookie.getPath());
			this.setSecure(cookie.isSecure());
			this.setValue(cookie.getValue());
			this.setVersion(cookie.getVersion());
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getComment()
		 */
		@Override
		public String getComment() {
			return this.comment;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getCommentURL()
		 */
		@Override
		public String getCommentURL() {
			return null;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getDomain()
		 */
		@Override
		public String getDomain() {
			return this.domain;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getExpiryDate()
		 */
		@Override
		public Date getExpiryDate() {
			return this.expiryDate;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getName()
		 */
		@Override
		public String getName() {
			return this.name;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getPath()
		 */
		@Override
		public String getPath() {
			return this.path;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getPorts()
		 */
		@Override
		public int[] getPorts() {
			return null;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getValue()
		 */
		@Override
		public String getValue() {
			return this.value;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#getVersion()
		 */
		@Override
		public int getVersion() {
			return this.version;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#isExpired(java.util.Date)
		 */
		@Override
		public boolean isExpired(Date date) {
			return false;
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#isPersistent()
		 */
		@Override
		public boolean isPersistent() {
			return (null != this.expiryDate);
		}

		/**
		 *
		 * @see org.apache.http.cookie.Cookie#isSecure()
		 */
		@Override
		public boolean isSecure() {
			return this.isSecure;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setComment(java.lang.String)
		 */
		@Override
		public void setComment(String comment) {
			this.comment = comment;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setDomain(java.lang.String)
		 */
		@Override
		public void setDomain(String domain) {
			this.domain = domain;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setExpiryDate(java.util.Date)
		 */
		@Override
		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setPath(java.lang.String)
		 */
		@Override
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setSecure(boolean)
		 */
		@Override
		public void setSecure(boolean isSecure) {
			this.isSecure = isSecure;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setValue(java.lang.String)
		 */
		@Override
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 *
		 * @see org.apache.http.cookie.SetCookie#setVersion(int)
		 */
		@Override
		public void setVersion(int version) {
			this.version = version;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
					.append("expiryDate", this.expiryDate).append("comment", this.comment).append("domain", this.domain)
					.append("name", this.name).append("path", this.path).append("value", this.value)
					.append("isSecure", this.isSecure).append("version", this.version).toString();
		}
	}

	/**
	 * deserialize
	 */
	public static CookieStore deserialize(InputStream in) {
		try {
			try (ObjectInputStream encoder = new ObjectInputStream(in)) {
				Object object = encoder.readObject();
				encoder.close();
				return (CookieStore) object;
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
	public static CookieStore xmldeserialize(InputStream in) {
		try (XMLDecoder decoder = new XMLDecoder(in)) {
			Integer count = (Integer) decoder.readObject();
			CookieStore store = new CookieStore();
			for (int i = 0; i < count; i++) {
				Cookie readObject = (Cookie) decoder.readObject();
				store.addCookie(readObject);
			}
			decoder.close();
			return store;
		}
	}

	protected static final Logger logger = LoggerFactory.getLogger(CookieStore.class);

	/** backing store */
	protected transient org.apache.http.client.CookieStore cookieStore = new BasicCookieStore();

	/** interceptors */
	protected transient List<CookieStoreInterceptor> cookieStoreInterceptors = new ArrayList<>();

	public CookieStore() {
		super();
	}

	public CookieStore(org.apache.http.client.CookieStore source) {
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
	 * @see org.apache.http.client.CookieStore#clear()
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
	 * @see org.apache.http.client.CookieStore#clearExpired(java.util.Date)
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
	 * @see org.apache.http.client.CookieStore#getCookies()
	 */
	@Override
	public synchronized List<Cookie> getCookies() {
		for (CookieStoreInterceptor interceptor : this.cookieStoreInterceptors) {
			interceptor.beforeGetCookies(this);
		}
		return this.cookieStore.getCookies();
	}

	public org.apache.http.client.CookieStore getCookieStore() {
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

	public CookieStore serialize(OutputStream out) throws IOException {
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

	public CookieStore xmlserialize(OutputStream out) {
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
