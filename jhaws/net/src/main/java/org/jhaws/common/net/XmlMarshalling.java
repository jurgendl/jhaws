package org.jhaws.common.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlMarshalling {
    private final Unmarshaller unmarshaller;

    private final Marshaller marshaller;

    public XmlMarshalling(Class<?>... types) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(types);
            unmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public String marshall(Object body, String charSet) {
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

    public <T> T unmarshall(Class<T> type, byte[] body) {
        return unmarshall(type, new ByteArrayInputStream(body));
    }

    public <T> T unmarshall(Class<T> type, String body, String charSet) {
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
}
