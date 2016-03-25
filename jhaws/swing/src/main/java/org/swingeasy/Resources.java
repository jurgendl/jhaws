package org.swingeasy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author Jurgen
 */
public class Resources {
	private static ClassLoader defaultClassLoader;

	public static byte[] getDataResource(ClassLoader cl, String key) throws IOException {
		String path = Resources.class.getPackage().getName().replace('.', '/') + "/resources/" + key;//$NON-NLS-1$
		try (InputStream resource = cl.getResourceAsStream(path)) {
			byte[] data = new byte[resource.available()];
			resource.read(data);
			resource.close();
			return data;
		}
	}

	public static byte[] getDataResource(String key) throws IOException {
		return Resources.getDataResource(Resources.getDefaultClassLoader(), key);
	}

	public static ClassLoader getDefaultClassLoader() {
		if (Resources.defaultClassLoader == null) {
			Resources.defaultClassLoader = ClassLoader.getSystemClassLoader();
		}
		return Resources.defaultClassLoader;
	}

	public static ImageIcon getImageResource(ClassLoader cl, String key) {
		String path = Resources.class.getPackage().getName().replace('.', '/') + "/resources/images/" + key;//$NON-NLS-1$
		URL resource = cl.getResource(path);
		if (resource == null) {
			System.err.println("resource " + path + " not found");
			return null;
		}
		return new ImageIcon(resource);
	}

	public static ImageIcon getImageResource(String key) {
		return Resources.getImageResource(Resources.getDefaultClassLoader(), key);
	}

	public static byte[] getResource(ClassLoader cl, String key) throws IOException {
		File file = new File(key);
		if (file.exists()) {
			return Resources.read(file);
		}
		URL url = cl.getResource(key);
		if (url != null) {
			return Resources.read(url);
		}
		throw new IOException("resource not found: " + key);
	}

	public static byte[] getResource(String key) throws IOException {
		return Resources.getResource(Resources.getDefaultClassLoader(), key);
	}

	public static byte[] read(File file) throws IOException {
		try (InputStream in = new FileInputStream(file)) {
			byte[] data = Resources.read(in);
			in.close();
			return data;
		}
	}

	public static byte[] read(InputStream in) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(in.available())) {
			byte[] buffer = new byte[1024 * 8];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.close();
			return out.toByteArray();
		}
	}

	public static byte[] read(URL url) throws IOException {
		try (InputStream in = url.openStream()) {
			byte[] data = Resources.read(in);
			in.close();
			return data;
		}
	}
}
