package org.jhaws.common.net.client.tests;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;

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
}
