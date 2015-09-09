package org.jhaws.common.net.client.tmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlMarshalling {
    private final Unmarshaller unmarshaller;
    
    private final Marshaller marshaller;
    
    public XmlMarshalling(Class<?> ... types) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(types);
            unmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    
    public  String marshall(Object body, String charSet) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(body, baos);
            return baos.toString(charSet);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public  <T> T unmarshall(Class<T> type, byte[] body) {
        return unmarshall(type, new ByteArrayInputStream(body));
    }

    public  <T> T unmarshall(Class<T> type, String body, String charSet) {
        try {
            return unmarshall(type, body.getBytes(charSet));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T unmarshall(Class<T> type, InputStream body) {
        try {
            return type.cast(unmarshaller.unmarshal(body));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
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
