package org.jhaws.common.net.client.old;

import java.util.Arrays;

import org.jhaws.common.io.security.SecureMe;
import org.jhaws.common.net.client.HTTPClient;
import org.jhaws.common.net.client.obsolete.Password;
import org.junit.Assert;
import org.junit.Test;

public class HttpClientTest {
	@Test
	public void test() {
		try (HTTPClient hc = new HTTPClient()) {
			System.out.println(hc.get("http://www.google.com").getContentString().length());
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
					"[111, 114, 103, 46, 106, 104, 97, 119, 115, 46, 99, 111, 109, 109, 111, 110, 46, 105, 111, 46, 115, 101, 99, 117, 114, 105, 116, 121, 46, 83, 101, 99, 117, 114, 101, 77, 101]",
					Arrays.toString(data.getBytes()));
			byte[] encrypt = sn.encrypt(data);
			Assert.assertEquals(data, sn.decrypt(encrypt));
		} catch (Exception e) {
			Assert.fail(String.valueOf(e));
		}
	}
}
