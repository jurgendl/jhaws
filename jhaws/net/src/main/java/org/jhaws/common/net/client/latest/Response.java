package org.jhaws.common.net.client.latest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class Response implements Serializable {
    private static final long serialVersionUID = 1806430557697629499L;

    private int statusCode;

    private byte[] content;

    private URI uri;

    private final Map<String, List<Object>> headers = new HashMap<>();

    private Locale locale;

    private long contentLength;

    private String contentEncoding;

    private String contentType;

    protected void addHeader(String key, Object value) {
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
            forms.add(new Form(uri, formnode));
        }
        return forms;
    }

    public Form getForm(String id) {
        return getForms().stream().filter(f -> f.getId().equals(id)).findFirst().get();
    }

    @Override
    public String toString() {
        return statusCode + "," + (content != null) + "," + headers;
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
}