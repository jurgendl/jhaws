package org.jhaws.common.io.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

public class PassGen {
	private static final String STRING_NR = "0123456789";

	private static final String STRING_LC = "abcdefghjkmnpqrstuvwxyz";

	private static final String STRING_UC = "ABCDEFGHJLMNPQRSTUVWXYZ";

	private static final String STRING_SP = "~$^_-";

	private static final SecureRandom random = new SecureRandom();

	private static final char[] CHARS_NR = STRING_NR.toCharArray();

	private static final char[] CHARS_LC = STRING_LC.toCharArray();

	private static final char[] CHARS_UC = STRING_UC.toCharArray();

	private static final char[] CHARS_SP = STRING_SP.toCharArray();

	public static enum PassType {
		numbers, lowercase, uppercase, special;
	}

	public static void main(String[] args) {
		System.out.println("\n\nspecial characters:");
		for (int i = 0; i < 5; i++)
			System.out.println(specialpass());
		System.out.println("\n\nalphanumeric:");
		for (int i = 0; i < 5; i++)
			System.out.println(pass());
		System.out.println("\n\nOracle:");
		for (int i = 0; i < 5; i++)
			System.out.println(pass(30, "#_$(^", PassType.lowercase, PassType.uppercase, PassType.numbers));
	}

	public static String specialpass(int length) {
		return pass(length, PassType.lowercase, PassType.uppercase, PassType.numbers, PassType.special);
	}

	public static String specialpass() {
		return specialpass(16);
	}

	public static String pass(int length) {
		return pass(length, PassType.lowercase, PassType.numbers);
	}

	public static String pass() {
		return pass(10);
	}

	public static String pass(int length, PassType type, PassType... types) {
		return pass(length, (String) null, type, types);
	}

	public static String pass(int length, String chars, PassType type, PassType... types) {
		EnumSet<PassType> t = EnumSet.of(type, types);
		List<String> list = new ArrayList<>();
		String S = "";
		if (chars != null && chars.length() > 0) {
			list.add(RandomStringUtils.random(1, 0, 0, false, false, chars.toCharArray(), random));
			length -= 1;
			S += chars;
		}
		if (t.contains(PassType.numbers)) {
			list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_NR, random));
			length -= 1;
			S += STRING_NR;
		}
		if (t.contains(PassType.lowercase)) {
			list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_LC, random));
			length -= 1;
			S += STRING_LC;
		}
		if (t.contains(PassType.uppercase)) {
			list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_UC, random));
			length -= 1;
			S += STRING_UC;
		}
		if (t.contains(PassType.special)) {
			list.add(RandomStringUtils.random(1, 0, 0, false, false, CHARS_SP, random));
			length -= 1;
			S += STRING_SP;
		}
		RandomStringUtils.random(length, 0, 0, false, false, S.toCharArray(), random).chars()
				.mapToObj(c -> String.valueOf((char) c)).forEach(list::add);
		Collections.shuffle(list);
		String p = list.stream().collect(Collectors.joining());
		return p;
	}

	public static String uuid() {
		return java.util.UUID.randomUUID().toString();
	}
}
