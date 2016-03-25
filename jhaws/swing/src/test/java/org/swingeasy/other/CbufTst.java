package org.swingeasy.other;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.Ostermiller.util.CircularByteBuffer;

public class CbufTst {
	public static void main(String[] args) {
		try {
			String text = "Implements the Circular Buffer producer/consumer model for bytes.\nMore information about this class is available from <a target=\"_top\" href=\"http://ostermiller.org/utils/CircularByteBuffer.html\">ostermiller.org</a>";
			final CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try (InputStream in = cbb.getInputStream()) {
						while (in.available() > 0) {
							System.out.print((char) in.read());
						}
						in.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}

				}
			}).start();
			OutputStream out = cbb.getOutputStream();
			for (char c : text.toCharArray()) {
				out.write(c);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
