package org.jhaws.common.net.client;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

/**
 * XPathHelper
 */
public class XPathHelper {
    /** //title */
    protected static final String TITLE = "//title";

    /** http://apache.org/xml/features/nonvalidating/load-external-dtd */
    protected static final String DTD_NO_VALIDATION = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    /** DocumentBuilder */
    protected transient DocumentBuilder builder = null;

    /** DocumentBuilderFactory */
    protected transient DocumentBuilderFactory documentBuilderFactory = null;

    /** XPathFactory */
    protected transient XPathFactory xPathFactory = null;

    protected XPathFactory getXPathFactory() {
        if (xPathFactory == null) {
            xPathFactory = XPathFactory.newInstance();
        }

        return this.xPathFactory;
    }

    protected DocumentBuilder getBuilder() throws javax.xml.parsers.ParserConfigurationException {
        if (builder == null) {
            builder = getDocumentBuilderFactory().newDocumentBuilder();
        }

        return this.builder;
    }

    protected DocumentBuilderFactory getDocumentBuilderFactory() {
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(DTD_NO_VALIDATION, false);
        }

        return this.documentBuilderFactory;
    }

    protected String findTitle(ByteArrayOutputStream bout) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException,
            IOException, javax.xml.xpath.XPathException {
        Document document = getBuilder().parse(new ByteArrayInputStream(bout.toByteArray()));
        XPath xpath = getXPathFactory().newXPath();
        String title = xpath.evaluate(TITLE, document);
        title = strip(title);

        return title;
    }

    /**
     * strip characters
     */
    protected String strip(String string) {
        StringBuilder sb = new StringBuilder();

        for (char c : string.toCharArray()) {
            if ((c == '\t') || (c == '\n') || (c == '\f') || (c == '\r')) {
                continue;
            }

            if ((c == '\\') || (c == '/') || (c == '?') || (c == '%') || (c == '*') || (c == ':') || (c == '|') || (c == '<') || (c == '>')
                    || (c == '.') || (c == '"')) {
                continue;
            }

            sb.append(c);
        }

        return sb.toString();
    }
}
