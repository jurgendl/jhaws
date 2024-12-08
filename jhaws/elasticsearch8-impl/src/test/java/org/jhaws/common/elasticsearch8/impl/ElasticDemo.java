package org.jhaws.common.elasticsearch8.impl;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jhaws.common.elasticsearch.common.ElasticDocument;
import org.jhaws.common.elasticsearch.common.Field;
import org.jhaws.common.elasticsearch.common.Index;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;

public class ElasticDemo {
    @Index("testum")
    public static class T1 extends ElasticDocument {
        @Field
        Long size;

        @Field
        String string;

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public String getString() {
            return this.string;
        }

        public void setString(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("T1 [size=");
            builder.append(this.size);
            builder.append(", string=");
            builder.append(this.string);
            builder.append(", toString()=");
            builder.append(super.toString());
            builder.append("]");
            return builder.toString();
        }
    }

    public static void main(String[] args) {
        ElasticSuperClient es = null;
        try {
            Properties p = new Properties();
            p.load(new FileInputStream(System.getProperty("user.home") + "/tests/docker-es/.env"));
            es = new ElasticSuperClient();
            es.setPassword(p.getProperty("ELASTIC_PASSWORD"));
            es.setPort(Integer.parseInt(p.getProperty("ES_PORT")));
            es.setProtocol("https");
            es.setUrl("localhost");
            if (false) {
                System.out.println("a- " + es.deleteIndex(T1.class));
                System.out.println("b- " + es.createIndex(T1.class));
            }
            if (false) {
                System.out.println("c- " + es.createIndex(T1.class));
                System.out.println("d- " + es.deleteIndex(T1.class));
                System.out.println("e- " + es.deleteIndex(T1.class));
                System.out.println("f- " + es.createIndex(T1.class));
            }
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
            if (false) {
                T1 t1 = new T1();
                t1.setSize(6_546_540l);
                t1.setId("n" + System.currentTimeMillis());
                t1.setString("mille");
                T1 t2 = new T1();
                t2.setSize(789_000l);
                t2.setId("o" + System.currentTimeMillis());
                t2.setString("term");
                System.out.println("n: " + es.multiIndexDocument(t1, t2));
                System.out.println("o: " + es.multiGetDocument(T1.class, null, Arrays.asList(t1.getId(), t2.getId())));
                System.out.println("p: " + es.multiDeleteDocument(T1.class, Arrays.asList(t1.getId(), t2.getId())));
                System.out.println("q: " + es.multiGetDocument(T1.class, null, Arrays.asList(t1.getId(), t2.getId())));
                System.out.println("p: " + es.multiDeleteDocument(T1.class, Arrays.asList(t1.getId(), t2.getId())));
            }
            if (true) {
                if (false) {
                    T1 t1 = new T1();
                    t1.setSize(6_540l);
                    t1.setId("n" + System.currentTimeMillis());
                    t1.setString("a");
                    T1 t2 = new T1();
                    t2.setSize(789_000l);
                    t2.setId("o" + System.currentTimeMillis());
                    t2.setString("b");
                    es.indexDocument(t1);
                    es.indexDocument(t2);
                }
                System.out.println("#" + es.count(T1.class));

                // System.out.println(es.getIndexMapping(T1.class));
                Query q = new Query.Builder().queryString(new QueryStringQuery.Builder().fields("string.keyword").query("a").build()).build();
                Scrolling pag = new Scrolling(1);
                do {
                    List<QueryResult<T1>> r = es.query(T1.class, q, pag, null, null, null);
                    r.forEach(rr -> System.out.println(rr));
                    System.out.println(pag);
                    System.out.println("----");
                } while (pag.canContinue());
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                es.getClient().indices().flush();
                es.shutdown();
            } catch (Exception ex2) {
                //
            }
        }
    }
}
