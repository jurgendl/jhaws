package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.common.io.IOFile;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;
import org.jhaws.common.net.client.forms.Form;


/**
 * Response
 */
public class Response implements Serializable {

    protected static final long serialVersionUID = -7030369997895433094L;

    /** date */
    protected Date date;

    /** cleaner */
    protected transient HtmlCleaner cleaner = new HtmlCleaner();

    /** chain */
    protected List<java.net.URI> chain = new ArrayList<java.net.URI>();

    /** filename */
    protected String filename;

    /** mime */
    protected String mime;

    /** redirect */
    protected String redirect;

    /** rootnode */
    protected transient TagNode node = null;

    /** content */
    protected byte[] content;

    public Response() {
        super();
    }

    protected Response(byte[] content, String mime, String filename, String charset) {
        this(content, mime, filename, charset, null);
    }

    protected Response(byte[] content, String mime, String filename, String charset, List<java.net.URI> chain) {
        this.content = content;
        this.mime = mime;
        this.filename = filename;
        this.chain = chain;
        this.charset = charset;
    }

    protected Response(String redirect) {
        this.redirect = redirect;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("filename", filename).append("mime", mime).append("date", date)
                .append("redirect", redirect).append("chain", chain).append("content", content != null)
                .append("content.size", content == null ? -1 : content.length).toString();
    }

    public String getMime() {
        return this.mime;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getContentString() throws IOException {
        return (getCharset() == null) ? new String(getContent()) : new String(getContent(), getCharset());
    }

    private String charset;

    private String getCharset() {
        return charset;
    }

    public byte[] getContent() throws IOException {
        if (StringUtils.isNotBlank(redirect)) {
            throw new IOException("redirected to " + redirect);
        }

        return this.content;
    }

    protected void setContent(byte[] content) {
        this.content = content;
    }

    @SuppressWarnings("unchecked")
    public List<Form> getForms() throws IOException {
        List<TagNode> formlist = getNode().getElementListByName("form", true);

        List<Form> forms = new ArrayList<Form>();

        for (TagNode formnode : formlist) {
            URI uri = chain.get(chain.size() - 1);
            forms.add(new Form(uri, formnode));
        }

        return forms;
    }

    public String getRedirect() {
        return this.redirect;
    }

    @SuppressWarnings("unchecked")
    public String getMetaRedirect() throws IOException {
        List<TagNode> metas = getNode().getElementListByName("meta", true);

        for (TagNode meta : metas) {
            if ("refresh".equals(meta.getAttributeByName("http-equiv"))) {
                String url = meta.getAttributeByName("content").split("url=")[1];

                return url;
            }
        }

        return null;
    }

    public Response serialize(OutputStream out) throws IOException {
        ObjectOutputStream encoder = new ObjectOutputStream(out);
        encoder.writeObject(this);
        encoder.close();

        return this;
    }

    public static Response deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream encoder = new ObjectInputStream(in);
        Object object = encoder.readObject();
        encoder.close();

        return (Response) object;
    }

    /**
     * cleanup and make new reponse
     */
    public void write(IOFile file) throws IOException {
        file.writeBytes(getContent());
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    public Form getForm(String id) throws IOException {
        for (Form form : getForms()) {
            if (id.equals(form.getId())) {
                return form;
            }
        }

        return null;
    }

    public List<java.net.URI> getChain() {
        return chain;
    }

    @SuppressWarnings("unchecked")
    public String getTitle() throws IOException {
        List<TagNode> res = getNode().getElementListByName("title", false);

        return (res.size() == 0) ? null : res.get(0).getText().toString();
    }

    public TagNode getNode() throws IOException {
        if (node == null) {
            node = cleaner.clean(new ByteArrayInputStream(getContent()));
        }

        return this.node;
    }

    protected Response cleanup() throws IOException {
        TagNode rootnode = getNode();
        CleanerProperties properties = cleaner.getProperties();
        XmlSerializer xmlSerializer = new PrettyXmlSerializer(properties);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlSerializer.writeXmlToStream(rootnode, out);
        out.close();

        return new Response(out.toByteArray(), mime, filename, charset, chain);
    }
}
