package org.jhaws.common.encoding;

import java.io.UnsupportedEncodingException;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.jhaws.common.lang.StringUtils;

/**
 * @see http://java-performance.info/base64-encoding-and-decoding-performance/
 */
public class Base64 {
	private static final Decoder BASE64_DECODER = java.util.Base64.getDecoder();

	private static final Encoder BASE64_ENCODER = java.util.Base64.getEncoder();

	public static byte[] base64Encode(byte[] data) {
		return BASE64_ENCODER.encode(data);
	}

	public static byte[] base64Encode(String data) {
		try {
			return BASE64_ENCODER.encode(data.getBytes(StringUtils.UTF8));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static byte[] base64Decode(byte[] data) {
		return BASE64_DECODER.decode(data);
	}

	public static byte[] base64Decode(String data) {
		try {
			return BASE64_DECODER.decode(data.getBytes(StringUtils.UTF8));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String base64EncodeToString(byte[] data) {
		try {
			return new String(base64Encode(data), StringUtils.UTF8);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String base64EncodeToString(String data) {
		try {
			return new String(base64Encode(data), StringUtils.UTF8);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String base64DecodeToString(byte[] data) {
		try {
			return new String(base64Decode(data), StringUtils.UTF8);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String base64DecodeToString(String data) {
		try {
			return new String(base64Decode(data), StringUtils.UTF8);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
}
