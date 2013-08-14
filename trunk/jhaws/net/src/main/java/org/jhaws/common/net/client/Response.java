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
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.net.client.forms.Form;

/**
 * Response
 */
public class Response implements Serializable {
    protected static final long serialVersionUID = -7030369997895433094L;

    public static Response deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream encoder = new ObjectInputStream(in);
        Object object = encoder.readObject();
        encoder.close();

        return (Response) object;
    }

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

    protected String charset;

    public Response() {
        super();
    }

    public Response(byte[] content) {
        this(content, null, null, null, null);
    }

    public Response(byte[] content, String mime, String filename, String charset) {
        this(content, mime, filename, charset, null);
    }

    public Response(byte[] content, String mime, String filename, String charset, List<java.net.URI> chain) {
        this.content = content;
        this.mime = mime;
        this.filename = filename;
        this.chain = chain;
        this.charset = charset;
    }

    protected Response(String redirect) {
        this.redirect = redirect;
    }

    protected Response cleanup() throws IOException {
        TagNode rootnode = this.getNode();
        CleanerProperties properties = this.cleaner.getProperties();
        XmlSerializer xmlSerializer = new PrettyXmlSerializer(properties);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlSerializer.writeToStream(rootnode, out);
        out.close();

        return new Response(out.toByteArray(), this.mime, this.filename, this.charset, this.chain);
    }

    public List<java.net.URI> getChain() {
        return this.chain;
    }

    private String getCharset() {
        return this.charset;
    }

    public byte[] getContent() throws IOException {
        if (StringUtils.isNotBlank(this.redirect)) {
            throw new IOException("redirected to " + this.redirect);
        }

        return this.content;
    }

    public String getContentString() throws IOException {
        return (this.getCharset() == null) ? new String(this.getContent()) : new String(this.getContent(), this.getCharset());
    }

    public String getFilename() {
        return this.filename;
    }

    public Form getForm(String id) throws IOException {
        for (Form form : this.getForms()) {
            if (id.equals(form.getId())) {
                return form;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Form> getForms() throws IOException {
        List<TagNode> formlist = this.getNode().getElementListByName("form", true);

        List<Form> forms = new ArrayList<Form>();

        for (TagNode formnode : formlist) {
            URI uri = this.chain == null ? null : this.chain.get(this.chain.size() - 1);
            forms.add(new Form(uri, formnode));
        }

        return forms;
    }

    @SuppressWarnings("unchecked")
    public String getMetaRedirect() throws IOException {
        List<TagNode> metas = this.getNode().getElementListByName("meta", true);

        for (TagNode meta : metas) {
            if ("refresh".equals(meta.getAttributeByName("http-equiv"))) {
                String url = meta.getAttributeByName("content").split("url=")[1];

                return url;
            }
        }

        return null;
    }

    public String getMime() {
        return this.mime;
    }

    public TagNode getNode() throws IOException {
        if (this.node == null) {
            this.node = this.cleaner.clean(new ByteArrayInputStream(this.getContent()));
        }

        return this.node;
    }

    public String getRedirect() {
        return this.redirect;
    }

    @SuppressWarnings("unchecked")
    public String getTitle() throws IOException {
        List<TagNode> res = this.getNode().getElementListByName("title", false);

        return (res.size() == 0) ? null : res.get(0).getText().toString();
    }

    public Response serialize(OutputStream out) throws IOException {
        ObjectOutputStream encoder = new ObjectOutputStream(out);
        encoder.writeObject(this);
        encoder.close();

        return this;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    protected void setContent(byte[] content) {
        this.content = content;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("filename", this.filename).append("mime", this.mime)
                .append("date", this.date).append("redirect", this.redirect).append("chain", this.chain).append("content", this.content != null)
                .append("content.size", this.content == null ? -1 : this.content.length).toString();
    }

    /**
     * cleanup and make new reponse
     */
    public void write(IOFile file) throws IOException {
        file.writeBytes(this.getContent());
    }
}
