package org.jhaws.common.net.client.latest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.client.utils.URIUtils;
import org.jhaws.common.io.FilePath;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HTTPClientUtils {
    /** mimeTypes */
    protected static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

    /**
     * build relative url
     */
    public static String relativeURL(String original, String newRelative) {
        try {
            return URIUtils.resolve(new URI(original), newRelative).toString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * build relative url
     */
    public static URL relativeURL(URL original, String newRelative) {
        try {
            return URIUtils.resolve(original.toURI(), newRelative).toURL();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * build relative url
     */
    public static URI relativeURL(URI original, String newRelative) {
        return URIUtils.resolve(original, newRelative);
    }

    public static synchronized String getMimeType(FilePath file) {
        return mimeTypes.getContentType(file.toFile());
    }

    public static String pretty(String xml) {
        try {
            // java.lang.System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document original = null;
            try {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                original = dBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(xml.getBytes("UTF-8")))));
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setAttribute("indent-number", 4);
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(original), xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
