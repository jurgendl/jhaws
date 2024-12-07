package org.jhaws.common.elasticsearch8.impl;

import java.io.FileInputStream;
import java.util.Properties;

import org.jhaws.common.elasticsearch.common.ElasticDocument;
import org.jhaws.common.elasticsearch.common.Field;
import org.jhaws.common.elasticsearch.common.Index;

public class ElasticDemo {
    @Index("testum")
    public static class T1 extends ElasticDocument {
        @Field
        Long size;

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }
    }

    public static void main(String[] args) {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream(System.getProperty("user.home") + "/tests/docker-es/.env"));
            ElasticSuperClient es = new ElasticSuperClient();
            es.setPassword(p.getProperty("ELASTIC_PASSWORD"));
            es.setPort(Integer.parseInt(p.getProperty("ES_PORT")));
            es.setProtocol("https");
            es.setUrl("localhost");
            if (false) {
                T1 t1 = new T1();
                t1.setSize(System.currentTimeMillis());
                t1.setId("" + t1.getSize());
                System.out.println("a: " + es.indexDocument(t1));
                t1.setSize(1234 + System.currentTimeMillis());
                System.out.println("b: " + es.indexDocument(t1));
                System.out.println("c: " + es.documentExists(t1));
                String id = t1.getId();
                t1.setId("x");
                System.out.println("d: " + es.documentExists(t1));
                System.out.println("e: " + es.getIndexMapping(T1.class));
                t1.setId(id);
                t1 = es.getDocument(t1);
                System.out.println("f: " + t1);
                System.out.println("g: " + ElasticHelper.debug(t1, es.getObjectMapper()));
                System.out.println("h: " + es.deleteDocument(t1));
                System.out.println("i: " + es.documentExists(t1));
            }
            if (false) {
                T1 t1 = new T1();
                t1.setSize(System.currentTimeMillis());
                t1.setId("f" + System.currentTimeMillis());
                System.out.println("j: " + es.updateDocument(t1));
                System.out.println("k: " + es.documentExists(t1));
                System.out.println("l: " + es.createDocument(t1));
                System.out.println("m: " + es.documentExists(t1));
            }
            es.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}
