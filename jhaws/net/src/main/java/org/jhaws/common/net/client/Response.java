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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

public class Response implements Serializable {
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

	private String mime;

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
		return content == null ? null : new String(content);
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public List<Form> getForms() {
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node;
		try {
			node = cleaner.clean(new ByteArrayInputStream(getContent()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		List<? extends TagNode> formlist = node.getElementListByName("form", true);
		List<Form> forms = new ArrayList<Form>();
		for (TagNode formnode : formlist) {
			forms.add(new Form(getLastUri(), formnode));
		}
		return forms;
	}

	public URI getLastUri() {
		return chain.size() == 0 ? uri : chain.get(chain.size() - 1);
	}

	public Form getForm(String id) {
		return getForms().stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public String toString() {
		return "statusCode=" + statusCode + ";content=" + (content != null);
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
		List<? extends TagNode> res = this.getNode().getElementListByName("title", false);
		return res.isEmpty() ? null : res.get(0).getText().toString();
	}

	public TagNode getNode() throws IOException {
		if (this.node == null) {
			this.node = this.cleaner.clean(new ByteArrayInputStream(this.getContent()));
		}
		return this.node;
	}

	public String getMetaRedirect() throws IOException {
		List<? extends TagNode> metas = this.getNode().getElementListByName("meta", true);
		for (TagNode meta : metas) {
			if ("refresh".equals(meta.getAttributeByName("http-equiv"))) {
				String url = meta.getAttributeByName("content").split("url=")[1];

				return url;
			}
		}
		return null;
	}

	public Response cleanup() throws IOException {
		TagNode rootnode = this.getNode();
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

	public String getMime() {
		return this.mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
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
}