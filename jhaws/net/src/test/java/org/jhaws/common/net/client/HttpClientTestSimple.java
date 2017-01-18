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
}
