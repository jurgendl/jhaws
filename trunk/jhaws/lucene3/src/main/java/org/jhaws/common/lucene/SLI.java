package org.jhaws.common.lucene;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SLI {

    public static void main(String[] args) {
        ServiceLoader<ToLuceneDocument> sl = ServiceLoader.load(ToLuceneDocument.class);
        Iterator<ToLuceneDocument> iterator = sl.iterator();
        System.out.println("start");
        while (iterator.hasNext()) {
            ToLuceneDocument i = iterator.next();
            System.out.println(i.toString());
        }
        System.out.println("end");
    }
}
