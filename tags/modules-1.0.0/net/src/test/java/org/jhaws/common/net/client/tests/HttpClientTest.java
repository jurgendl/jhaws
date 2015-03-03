package org.jhaws.common.net.client.tests;

import java.util.Arrays;

import org.jhaws.common.net.client.SecureNet;
import org.jhaws.common.net.client.forms.Password;
import org.junit.Assert;
import org.junit.Test;

public class HttpClientTest {
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
            SecureNet sn = new SecureNet();
            String data = sn.getClass().getName();
            Assert.assertEquals(
                    "[111, 114, 103, 46, 99, 111, 109, 109, 111, 110, 46, 110, 101, 116, 46, 99, 108, 105, 101, 110, 116, 46, 83, 101, 99, 117, 114, 101, 78, 101, 116]",
                    Arrays.toString(data.getBytes()));
            byte[] encrypt = sn.encrypt(data);
            Assert.assertEquals(data, sn.decrypt(encrypt));
        } catch (Exception e) {
            Assert.fail(String.valueOf(e));
        }
    }
}