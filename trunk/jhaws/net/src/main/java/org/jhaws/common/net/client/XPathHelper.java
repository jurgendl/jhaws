package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XPathHelper
 */
public class XPathHelper {
    /** //title */
    protected static final String TITLE = "//title";

    /** http://apache.org/xml/features/nonvalidating/load-external-dtd */
    protected static final String DTD_NO_VALIDATION = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    public static org.w3c.dom.Document parseXml(byte[] xmlData) throws ParserConfigurationException, SAXException, IOException {
        return XPathHelper.parseXml(new ByteArrayInputStream(xmlData));
    }

    public static org.w3c.dom.Document parseXml(InputStream xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(xmlFile);
        return doc;
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, byte[] xmlData) throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException {
        return XPathHelper.xpathXml(clazz, expr, XPathHelper.parseXml(xmlData));
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, byte[] xmlData, String basenodename) throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException {
        return XPathHelper.xpathXml(clazz, expr, XPathHelper.parseXml(xmlData), basenodename);
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, org.w3c.dom.Document doc) throws XPathExpressionException, IOException {
        String basenodename = expr.substring(1, expr.indexOf('/', 2));
        return XPathHelper.xpathXml(clazz, expr, doc, basenodename);
    }

    @SuppressWarnings("unchecked")
    public static <T> T xpathXml(@SuppressWarnings("unused") Class<T> clazz, String expr, org.w3c.dom.Document doc, String basenodename)
            throws XPathExpressionException, IOException {
        // http://www.w3schools.com/xpath/
        // http://www.ibm.com/developerworks/library/x-javaxpathapi/
        // http://blog.davber.com/2006/09/17/xpath-with-namespaces-in-java/
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NSHandler(NodeList.class.cast(doc.getElementsByTagName(basenodename)).item(0).getAttributes()));
        List<Node> results = new ArrayList<Node>();
        NodeList nodes = NodeList.class.cast(xpath.compile(expr).evaluate(doc, XPathConstants.NODESET));
        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            results.add(nodes.item(i));
        }
        return (T) (results.size() == 0 ? null : (results.size() == 1 ? results.get(0) : results));
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, byte[] xmlData) throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException {
        return XPathHelper.xpathXmlList(clazz, expr, XPathHelper.parseXml(xmlData));
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, byte[] xmlData, String basenodename) throws XPathExpressionException,
            IOException, ParserConfigurationException, SAXException {
        return XPathHelper.xpathXmlList(clazz, expr, XPathHelper.parseXml(xmlData), basenodename);
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, org.w3c.dom.Document doc) throws XPathExpressionException, IOException {
        String basenodename = expr.substring(1, expr.indexOf('/', 2));
        return XPathHelper.xpathXmlList(clazz, expr, doc, basenodename);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> xpathXmlList(@SuppressWarnings("unused") Class<T> clazz, String expr, org.w3c.dom.Document doc, String basenodename)
            throws XPathExpressionException, IOException {
        List<T> o = XPathHelper.xpathXml(List.class, expr, doc, basenodename);
        if (o == null) {
            return new ArrayList<T>();
        }
        return o;
    }

    /** DocumentBuilder */
    protected transient DocumentBuilder builder = null;

    /** DocumentBuilderFactory */
    protected transient DocumentBuilderFactory documentBuilderFactory = null;

    /** XPathFactory */
    protected transient XPathFactory xPathFactory = null;

    protected String findTitle(ByteArrayOutputStream bout) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException,
            IOException, javax.xml.xpath.XPathException {
        Document document = this.getBuilder().parse(new ByteArrayInputStream(bout.toByteArray()));
        XPath xpath = this.getXPathFactory().newXPath();
        String title = xpath.evaluate(XPathHelper.TITLE, document);
        title = this.strip(title);

        return title;
    }

    protected DocumentBuilder getBuilder() throws javax.xml.parsers.ParserConfigurationException {
        if (this.builder == null) {
            this.builder = this.getDocumentBuilderFactory().newDocumentBuilder();
        }

        return this.builder;
    }

    protected DocumentBuilderFactory getDocumentBuilderFactory() {
        if (this.documentBuilderFactory == null) {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilderFactory.setAttribute(XPathHelper.DTD_NO_VALIDATION, false);
        }

        return this.documentBuilderFactory;
    }

    protected XPathFactory getXPathFactory() {
        if (this.xPathFactory == null) {
            this.xPathFactory = XPathFactory.newInstance();
        }

        return this.xPathFactory;
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
