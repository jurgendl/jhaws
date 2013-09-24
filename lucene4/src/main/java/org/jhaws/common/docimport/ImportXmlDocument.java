package org.jhaws.common.docimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jhaws.common.io.IOFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportXmlDocument implements ImportDocument {
    /** field */
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    /**
     * Creates a new XmlToLuceneDocument object.
     */
    public ImportXmlDocument() {
        super();
    }

    /**
     * na
     * 
     * @param parent na
     * @param sb na
     */
    private void digestChildren(final Node parent, final StringBuilder sb) {
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);

            if ((Node.TEXT_NODE == node.getNodeType()) || (Node.CDATA_SECTION_NODE == node.getNodeType())) {
                String text = node.getNodeValue().trim();

                if (text.length() > 0) {
                    sb.append(text);
                    sb.append("; ");
                }
            }

            this.digestChildren(node, sb);
        }
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        try {
            DocumentBuilder builder = ImportXmlDocument.factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(file);
            StringBuilder sb = new StringBuilder(100);
            this.digestChildren(document, sb);

            return sb.toString();
        } catch (final ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (final SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "xml";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
