package org.jhaws.common.io.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

public class PassGen {
	private static final String STRING_O = "~!#$%^_-+[](){}<>";

	private static final String STRING_UC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String STRING_LC = "abcdefghijklmnopqrstuvwxyz";

	private static final String STRING_NR = "0123456789";

	private static final SecureRandom random = new SecureRandom();

	private static final char[] CHARS_NR = STRING_NR.toCharArray();

	private static final char[] CHARS_LC = STRING_LC.toCharArray();

	private static final char[] CHARS_UC = STRING_UC.toCharArray();

	private static final char[] CHARS_O = STRING_O.toCharArray();

	private static final char[] CHARS = (STRING_NR + STRING_LC + STRING_UC + STRING_O).toCharArray();

	public static void main(String[] args) {
		pass();
	}

	public static void pass() {
		pass(1, 30);
	}

	public static void pass(int length) {
		pass(1, length);
	}

	public static String pass(int count, int length) {
		for (int i = 0; i < count - 1; i++) {
			_pass(length);
		}
		return _pass(length);
	}

	private static String _pass(int length) {
		List<String> list = new ArrayList<>();
		list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_NR, random));
		length -= 1;
		list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_LC, random));
		length -= 1;
		list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_UC, random));
		length -= 1;
		list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_O, random));
		length -= 1;
		RandomStringUtils.random(length, 0, 0, false, false, CHARS, random).chars().mapToObj(c -> String.valueOf((char) c)).forEach(list::add);
		Collections.shuffle(list);
		String p = list.stream().collect(Collectors.joining());
		System.out.println(p);
		return p;
	}

	public static String uuid() {
		return uuid(1);
	}

	public static String uuid(int count) {
		for (int i = 0; i < count - 1; i++) {
			System.out.println(java.util.UUID.randomUUID().toString());
		}
		String string = java.util.UUID.randomUUID().toString();
		System.out.println(string);
		return string;
	}
}
