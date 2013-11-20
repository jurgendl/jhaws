package org.jhaws.common.net.client.tests;

import java.util.Arrays;

import org.jhaws.common.io.security.SecureMe;
import org.jhaws.common.net.client.HTTPClient;
import org.jhaws.common.net.client.forms.Password;
import org.junit.Assert;
import org.junit.Test;

public class HttpClientTest {
    @Test
    public void test() {
        try {
            HTTPClient hc = new HTTPClient();
            hc.get("http://www.google.com");
        } catch (Exception e) {
            Assert.fail(String.valueOf(e));
        }
    }

    @Test
    public void testPassword() {
        Password test = new Password("2", "3");
        String value = "sqpodfjofds65645964fdjikdf545esdsddd";
        test.setValue(value);
        Assert.assertEquals(value, test.getValue());
    }

    @Test
    public void testSecureNet() {
        try {
            SecureMe sn = new SecureMe();
            String data = sn.getClass().getName();
            Assert.assertEquals(
                    "[111, 114, 103, 46, 106, 104, 97, 119, 115, 46, 99, 111, 109, 109, 111, 110, 46, 110, 101, 116, 46, 99, 108, 105, 101, 110, 116, 46, 83, 101, 99, 117, 114, 101, 78, 101, 116]",
                    Arrays.toString(data.getBytes()));
            byte[] encrypt = sn.encrypt(data);
            Assert.assertEquals(data, sn.decrypt(encrypt));
        } catch (Exception e) {
            Assert.fail(String.valueOf(e));
        }
    }
}
