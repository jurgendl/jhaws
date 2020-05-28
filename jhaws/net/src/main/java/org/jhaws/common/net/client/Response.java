package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.jhaws.common.lang.CollectionUtils8;
import org.jhaws.common.lang.EnhancedArrayList;
import org.jhaws.common.lang.EnhancedLinkedHashMap;
import org.jhaws.common.lang.EnhancedList;
import org.jhaws.common.lang.EnhancedMap;
import org.jhaws.common.lang.IntegerValue;
import org.jhaws.common.lang.StringUtils;
import org.jsoup.Jsoup;

@SuppressWarnings("serial")
public class Response extends InputStream implements Serializable {
	public static Response deserialize(InputStream in) throws IOException {
		try (ObjectInputStream encoder = new ObjectInputStream(in)) {
			Object object;
			try {
				object = encoder.readObject();
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException(ex);
			}
			encoder.close();
			return (Response) object;
		}
	}

	public Response serialize(OutputStream out) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
			encoder.writeObject(this);
			encoder.close();
			return this;
		}
	}

	protected int statusCode;

	protected String statusText;

	protected byte[] content;

	protected URI uri;

	protected EnhancedList<URI> chain;

	protected EnhancedMap<String, EnhancedList<Object>> headers = new EnhancedLinkedHashMap<>();

	protected Locale locale;

	protected long contentLength;

	protected String contentEncoding;

	protected String contentType;

	protected Date date;

	protected String filename;

	protected String charset;

	public Response() {
		super();
	}

	public void addHeader(String key, Object value) {
		EnhancedList<Object> list = headers.get(key);
		if (list == null) {
			list = new EnhancedArrayList<>();
			headers.put(key, list);
		}
		list.add(value);
	}

	public EnhancedMap<String, EnhancedList<Object>> getHeaders() {
		return headers;
	}

	public boolean isOk() {
		return 200 <= getStatusCode() && getStatusCode() <= 299;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public byte[] getContent() {
		return content;
	}

	public String getContentString() {
		try {
			return content == null ? null : new String(content, StringUtils.UTF8);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setContent(byte[] content) {
		this.content = content;
		this.pos = 0;
		this.count = content.length;
	}

	public URI getLastUri() {
		return chain.size() == 0 ? uri : chain.get(chain.size() - 1);
	}

	public Form getForm(String id) {
		return getOptionalForm(id).orElse(null);
	}

	public Optional<Form> getOptionalForm(String id) {
		return getForms().stream().filter(f -> f.getId().equals(id)).findFirst();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response[").append("\n");
		builder.append("statusCode=").append(this.statusCode).append("\n");
		if (this.statusText != null)
			builder.append("statusText=").append(this.statusText).append("\n");
		if (this.uri != null)
			builder.append("uri=").append(this.uri).append("\n");
		if (this.chain != null) {
			IntegerValue i = new IntegerValue(-1);
			chain.stream().forEach(c -> builder.append("chain:").append(i.add()).append("=").append(c).append("\n"));
		}
		if (this.headers != null)
			headers.entrySet().stream().forEach(
					e -> builder.append("header:").append(e.getKey()).append("=").append(e.getValue()).append("\n"));
		if (this.locale != null)
			builder.append("locale=").append(this.locale).append("\n");
		builder.append("contentLength=").append(this.contentLength).append("\n");
		if (this.contentEncoding != null)
			builder.append("contentEncoding=").append(this.contentEncoding).append("\n");
		if (this.contentType != null)
			builder.append("contentType=").append(this.contentType).append("\n");
		if (this.date != null)
			builder.append("date=").append(this.date).append("\n");
		if (this.filename != null)
			builder.append("filename=").append(this.filename).append("\n");
		if (this.charset != null)
			builder.append("charset=").append(this.charset).append("\n");
		builder.append("]");
		return builder.toString();
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public long getContentLength() {
		return contentLength;
	}

	public long getHeaderContentLength() {
		try {
			return Long.parseLong(getHeaders().get("Content-Length").get(0).toString());
		} catch (java.lang.NullPointerException ex) {
			return -1l;
		}
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public EnhancedList<URI> getChain() {
		return this.chain;
	}

	public void setChain(EnhancedList<URI> chain) {
		this.chain = chain;
	}

	public String getFilename() {
		return filename;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setHeaders(EnhancedMap<String, EnhancedList<Object>> headers) {
		this.headers = headers;
	}

	public String getStatusText() {
		return this.statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	protected int pos;

	protected int count;

	protected int mark = 0;

	@Override
	public int read() throws IOException {
		return (pos < count) ? (content[pos++] & 0xff) : -1;
	}

	@Override
	public void close() throws IOException {
		//
	}

	@Override
	public synchronized int available() {
		return count - pos;
	}

	@Override
	public void mark(int readAheadLimit) {
		mark = pos;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized int read(byte b[], int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (pos >= count) {
			return -1;
		}
		int avail = count - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(content, pos, b, off, len);
		pos += len;
		return len;
	}

	@Override
	public synchronized void reset() {
		pos = mark;
	}

	@Override
	public synchronized long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		pos += k;
		return k;
	}

	protected transient org.jsoup.nodes.Document parsed;

	protected org.jsoup.nodes.Document parse() {
		if (parsed == null) {
			try {
				parsed = Jsoup.parse(new ByteArrayInputStream(this.getContent()), charset, uri.toASCIIString());
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
		return parsed;
	}

	public String getTitle() {
		return parse().select("title").html();
	}

	public String getMetaRedirect() {
		return parse().select("meta[refresh=http-equiv]").attr("content").split("url=")[1];
	}

	public EnhancedList<org.jhaws.common.net.client.Form> getForms() {
		List<org.jhaws.common.net.client.Form> collect = //
				parse()//
						.select("form")//
						.stream()//
						.map(org.jsoup.nodes.FormElement.class::cast)//
						.map((org.jsoup.nodes.FormElement node) -> {
							return new org.jhaws.common.net.client.Form(getLastUri(), node);
						}).collect(CollectionUtils8.collectList());
		return (EnhancedList<Form>) collect;
	}
}
