package org.jhaws.common.documents;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @see http://stackoverflow.com/questions/5456680/xml-document-to-string
 */
public class XmlTextExtracter implements FileTextExtracter {
    public static String SPLIT = "\n";

    @Override
    public void extract(InputStream stream, FilePath target) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex);
        }
        Document doc;
        try {
            doc = dBuilder.parse(stream);
        } catch (SAXException ex) {
            throw new IOException(ex);
        }
        doc.getDocumentElement().normalize();
        Element el = doc.getDocumentElement();
        BufferedWriter out = target.newBufferedWriter();
        iterate(el, out);
        out.close();
    }

    private void iterate(Node el, BufferedWriter bufferedWriter) throws IOException {
        NodeList childNodes = el.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode instanceof org.w3c.dom.Text) {
                org.w3c.dom.Text text = org.w3c.dom.Text.class.cast(childNode);
                if (StringUtils.isBlank(text.getWholeText())) continue;
                bufferedWriter.write(text.getWholeText());
                bufferedWriter.write(SPLIT);
            }
            iterate(childNode, bufferedWriter);
        }
    }

    public String getSPLIT() {
        return SPLIT;
    }

    public void setSPLIT(String sPLIT) {
        SPLIT = sPLIT;
    }
}
