package org.jhaws.common.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

public class TikaHelper {

    public static String parse(InputStream stream) throws IOException, SAXException, TikaException {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        parser.parse(stream, handler, metadata);
        return handler.toString();
    }

    public static void parse(InputStream stream, OutputStream out) throws IOException, SAXException, TikaException {
        AutoDetectParser parser = new AutoDetectParser();
        WriteOutContentHandler handler = new WriteOutContentHandler(out);
        Metadata metadata = new Metadata();
        parser.parse(stream, handler, metadata);
    }

    public static String parseToString(InputStream stream) throws IOException, SAXException, TikaException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        parse(stream, out);
        return new String(out.toByteArray());
    }

    public static String extractHtml(File f) throws IOException {
        byte[] bytes = Files.toByteArray(f);
        AutoDetectParser tikaParser = new AutoDetectParser();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        TransformerHandler handler;
        try {
            handler = factory.newTransformerHandler();
        } catch (TransformerConfigurationException ex) {
            throw new IOException(ex);
        }
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.toString());
        handler.setResult(new StreamResult(out));
        ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);
        try {
            tikaParser.parse(new ByteArrayInputStream(bytes), handler1, new Metadata());
        } catch (SAXException | TikaException ex) {
            throw new IOException(ex);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8.toString());
    }

}
