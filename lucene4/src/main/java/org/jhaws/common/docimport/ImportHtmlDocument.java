package org.jhaws.common.docimport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.jhaws.common.io.IOFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * HTM(L) document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportHtmlDocument implements ImportDocument {
    /** field */
    private final Tidy tidy = new Tidy();

    /**
     * Creates a new HtmlToLuceneDocument object.
     */
    public ImportHtmlDocument() {
        this.tidy.setErrout(new PrintWriter(new ByteArrayOutputStream()));
    }

    /**
     * na
     * 
     * @param list na
     * @param sb na
     * 
     * @return
     */
    private StringBuilder append(NodeList list, StringBuilder sb) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (Node.TEXT_NODE == node.getNodeType()) {
                sb.append(node.getNodeValue());
            }

            this.append(node.getChildNodes(), sb);
        }

        return sb;
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(final InputStream file) throws IOException {
        Document doc = this.tidy.parseDOM(file, new ByteArrayOutputStream());

        return this.append(doc.getChildNodes(), new StringBuilder()).toString();
    }

    /**
     * na
     * 
     * @param html na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public String getText(final String html) throws IOException {
        Document doc = this.tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), new ByteArrayOutputStream());

        return this.append(doc.getChildNodes(), new StringBuilder()).toString();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "htm;html";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
