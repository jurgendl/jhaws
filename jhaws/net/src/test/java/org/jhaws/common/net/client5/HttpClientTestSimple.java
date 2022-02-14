package org.jhaws.common.net.client5;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.jhaws.common.net.client.HTTPClientAuth;
import org.jhaws.common.net.client.Response;
import org.junit.Assert;
import org.junit.Test;

public class HttpClientTestSimple {
    @Test
    public void test() {
        try (HTTPClient h = new HTTPClient()) {
            Response r = h.get("https://www.google.co.uk/?gws_rd=ssl");
            System.out.println(r.getContentEncoding());
            System.out.println(r.getContentString());
        }
    }

    @Test
    public void testBasicAuth() {
        String user = "user";
        String passwd = "passwd";
        try (HTTPClient h = new HTTPClient()) {
            h.addAuthentication(new HTTPClientAuth(user, passwd, "httpbin.org").setPort(80).setPreemptive(true));
            // h.addAuthentication(new HTTPClientAuth("u1", "p1",
            // "www.google.co.uk"));
            {
                Response r = h.get("http://httpbin.org/basic-auth/" + user + "/" + passwd);
                System.out.println(r.getContentString());
            }
            // {
            // Response r = h.get("https://www.google.co.uk/?gws_rd=ssl");
            // System.out.println(r.getContentString().length());
            // }
        }
    }

    @Test
    public void testMultiThreaded() {
        try (HTTPClient h = new HTTPClient()) {
            Thread[] threads = new Thread[10];
            HttpClientContext[] context = new HttpClientContext[threads.length];
            for (int i = 0; i < threads.length; i++) {
                final int j = i;
                threads[i] = new Thread(() -> {
                    System.out.println(h.get("http://www.google.com").getContentString());
                    context[j] = h.getContext(null);
                });
            }
            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    //
                }
            }
            Assert.assertEquals(threads.length, new HashSet<>(Arrays.asList(context)).size());
        }
    }
}
