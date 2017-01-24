package org.jhaws.common.net.client;

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
            h.addAuthentication(new HTTPClientAuth(user, passwd, "httpbin.org").setPreemptive(true));
            // h.addAuthentication(new HTTPClientAuth("u1", "p1", "www.google.co.uk"));
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
}
