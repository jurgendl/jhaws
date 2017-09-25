package org.jhaws.common.io.jaxb;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.jaxb.pom.Pom;
import org.junit.Test;

public class JaxBTest {
    @Test
    public void testStream() {
        JAXBMarshalling j = new JAXBMarshalling(Pom.class);
        Pom pom;

        pom = j.unmarshall(new FilePath("pom.xml").readAllBytes());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml").readAll());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml"));
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml").toFile());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml").newInputStream());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml").newBufferedInputStream());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml").newBufferedReader());
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());
    }

    @Test
    public void testType() {
        JAXBMarshalling j = new JAXBMarshalling(Pom.class);
        Pom pom;

        pom = j.unmarshall(Pom.class, new FilePath("pom.xml"));
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(null, new FilePath("pom.xml"));
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = (Pom) j.unmarshall(Object.class, new FilePath("pom.xml"));
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());

        pom = j.unmarshall(new FilePath("pom.xml"));
        System.out.println(pom.getVersion() == null ? null : pom.getVersion().getVersion());
        System.out.println(pom.getParent() == null ? null : pom.getParent().getVersion().getVersion());
    }
}
