package org.jhaws.common.net.client.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class HTTPClientUtils {
    public static String marshall(Object body, String charSet) {
        try {
            JAXBContext jc = JAXBContext.newInstance(body.getClass());
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(body, baos);
            return baos.toString(charSet);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T unmarshall(Class<T> type, byte[] body) {
        return unmarshall(type, new ByteArrayInputStream(body));
    }

    public static <T> T unmarshall(Class<T> type, String body) {
        return unmarshall(type, body.getBytes());
    }

    public static <T> T unmarshall(Class<T> type, InputStream body) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return type.cast(unmarshaller.unmarshal(body));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
