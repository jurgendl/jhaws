package org.jhaws.common.net.client.latest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * http://www.w3schools.com/xpath/<br>
 * http://www.ibm.com/developerworks/library/x-javaxpathapi/<br>
 * http://blog.davber.com/2006/09/17/xpath-with-namespaces-in-java/<br>
 */
public class XPathHelper {
    public static class Xpr {
        private String expression = "";

        private String namespace = "";

        public Xpr(boolean defaulted) {
            this.namespace = defaulted ? NSHandler.DEFAULT : "";
        }

        public Xpr(String ns) {
            this.namespace = ns;
        }

        public Xpr append(String expr) {
            this.expression += "/" + expr;
            return this;
        }

        public String get() {
            return this.expression;
        }

        public Xpr path(String expr) {
            if (StringUtils.isBlank(this.namespace)) {
                return this.append(expr);
            }
            this.expression += "/" + this.namespace + ":" + expr;
            return this;
        }
    }

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

    public static String xpathfix(String xpath) {
        if ((xpath == null) || (xpath.trim().length() == 0)) {
            return null;
        }
        StringBuilder xpr = new StringBuilder();
        List<String> els = Arrays.asList(xpath.split("/"));
        for (int i = 0; i < els.size(); i++) {
            String el = els.get(i);
            if ((i == 0) && (el.length() == 0)) {
                continue;
            }
            if (el.contains("(") || el.equals("*")) {
                if (i != 0) {
                    xpr.append("/");
                }
                xpr.append(el);
            } else {
                xpr.append("/default:").append(el);
            }
        }
        return xpr.toString();
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, byte[] xmlData)
            throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        return XPathHelper.xpathXml(clazz, expr, XPathHelper.parseXml(xmlData));
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, byte[] xmlData, String basenodename)
            throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        return XPathHelper.xpathXml(clazz, expr, XPathHelper.parseXml(xmlData), basenodename);
    }

    public static <T> T xpathXml(Class<T> clazz, String expr, org.w3c.dom.Document doc) throws XPathExpressionException, IOException {
        String basenodename = expr.substring(1, expr.indexOf('/', 2)).replaceAll("default:", "");
        return XPathHelper.xpathXml(clazz, expr, doc, basenodename);
    }

    @SuppressWarnings("unchecked")
    public static <T> T xpathXml(Class<T> clazz, String expr, org.w3c.dom.Document doc, String basenodename)
            throws XPathExpressionException, IOException {
        NodeList elementsByTagName = doc.getElementsByTagName(basenodename);
        if (elementsByTagName.getLength() == 0) {
            return null;
        }
        Node item = elementsByTagName.item(0);
        NamedNodeMap attributes = item.getAttributes();
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NSHandler(attributes));
        XPathExpression xpr = xpath.compile(expr);
        try {
            Object evaluate = xpr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = NodeList.class.cast(evaluate);
            int length = nodes.getLength();
            List<Node> results = new ArrayList<Node>();
            for (int i = 0; i < length; i++) {
                Node node = nodes.item(i);
                results.add(node);
            }
            return (T) (results.isEmpty() ? null : (results.size() == 1 ? results.get(0) : results));
        } catch (javax.xml.xpath.XPathExpressionException ex) {
            return (T) xpr.evaluate(doc);
        }
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, byte[] xmlData)
            throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        return XPathHelper.xpathXmlList(clazz, expr, XPathHelper.parseXml(xmlData));
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, byte[] xmlData, String basenodename)
            throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        return XPathHelper.xpathXmlList(clazz, expr, XPathHelper.parseXml(xmlData), basenodename);
    }

    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, org.w3c.dom.Document doc) throws XPathExpressionException, IOException {
        int p1 = expr.indexOf("/") + 1;
        int p2 = expr.indexOf("/", p1);
        String basenodename = expr.substring(p1, p2).replaceAll("default:", "");
        return XPathHelper.xpathXmlList(clazz, expr, doc, basenodename);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> xpathXmlList(Class<T> clazz, String expr, org.w3c.dom.Document doc, String basenodename)
            throws XPathExpressionException, IOException {
        Object result = XPathHelper.xpathXml(Object.class, expr, doc, basenodename);
        if (result == null) {
            return Collections.emptyList();
        }
        if (result instanceof List<?>) {
            return (List<T>) result;
        }
        return Collections.singletonList((T) result);
    }

    /** http://apache.org/xml/features/nonvalidating/load-external-dtd */
    protected static final String DTD_NO_VALIDATION = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
}
