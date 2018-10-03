package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;
import org.jhaws.common.lang.IntegerValue;
import org.jhaws.common.lang.StringUtils;

public class Response extends InputStream implements Serializable {
	private static final long serialVersionUID = 1806430557697629499L;

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

	private int statusCode;

	private String statusText;

	private byte[] content;

	private URI uri;

	private List<URI> chain;

	private Map<String, List<Object>> headers = new HashMap<>();

	private Locale locale;

	private long contentLength;

	private String contentEncoding;

	private String contentType;

	private transient TagNode node = null;

	private transient HtmlCleaner cleaner = new HtmlCleaner();

	private Date date;

	private String filename;

	private String charset;

	public Response() {
		super();
	}

	public void addHeader(String key, Object value) {
		List<Object> list = headers.get(key);
		if (list == null) {
			list = new ArrayList<>();
			headers.put(key, list);
		}
		list.add(value);
	}

	public Map<String, List<Object>> getHeaders() {
		return Collections.unmodifiableMap(headers);
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

	public List<Form> getForms() {
		TagNode n;
		try {
			n = cleaner.clean(new ByteArrayInputStream(getContent()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		List<? extends TagNode> formlist = n.getElementListByName("form", true);
		List<Form> forms = new ArrayList<>();
		for (TagNode formnode : formlist) {
			forms.add(new Form(getLastUri(), formnode));
		}
		return forms;
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
		return Long.parseLong(getHeaders().get("Content-Length").get(0).toString());
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

	public List<URI> getChain() {
		return this.chain;
	}

	public void setChain(List<URI> chain) {
		this.chain = chain;
	}

	public String getTitle() throws IOException {
		List<? extends TagNode> res = getNode().getElementListByName("title", false);
		return res.isEmpty() ? null : res.get(0).getText().toString();
	}

	public TagNode getNode() throws IOException {
		if (this.node == null) {
			this.node = this.cleaner.clean(new ByteArrayInputStream(this.getContent()));
		}
		return this.node;
	}

	public String getMetaRedirect() throws IOException {
		List<? extends TagNode> metas = getNode().getElementListByName("meta", true);
		for (TagNode meta : metas) {
			if ("refresh".equals(meta.getAttributeByName("http-equiv"))) {
				String url = meta.getAttributeByName("content").split("url=")[1];
				return url;
			}
		}
		return null;
	}

	public Response cleanup() throws IOException {
		TagNode rootnode = getNode();
		CleanerProperties properties = this.cleaner.getProperties();
		XmlSerializer xmlSerializer = new PrettyXmlSerializer(properties);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			xmlSerializer.writeToStream(rootnode, out);
			out.close();
			Response r;
			try (ByteArrayOutputStream o = new ByteArrayOutputStream()) {
				serialize(o);
				try (ByteArrayInputStream i = new ByteArrayInputStream(o.toByteArray())) {
					r = deserialize(i);
				}
			}
			r.setContent(out.toByteArray());
			return r;
		}
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

	public void setHeaders(Map<String, List<Object>> headers) {
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

	public synchronized int available() {
		return count - pos;
	}

	public void mark(int readAheadLimit) {
		mark = pos;
	}

	public boolean markSupported() {
		return true;
	}

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

	public synchronized void reset() {
		pos = mark;
	}

	public synchronized long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		pos += k;
		return k;
	}

}
