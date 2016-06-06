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

	private static final String STRING_LC = "abcdefghijklmnopqrstuvwxyz";

	private static final String STRING_UC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String STRING_SP = "~!#$%^_-+[](){}<>";

	private static final SecureRandom random = new SecureRandom();

	private static final char[] CHARS_NR = STRING_NR.toCharArray();

	private static final char[] CHARS_LC = STRING_LC.toCharArray();

	private static final char[] CHARS_UC = STRING_UC.toCharArray();

	private static final char[] CHARS_SP = STRING_SP.toCharArray();

	public static enum PassType {
		numbers, lowercase, uppercase, special;
	}

	public static void main(String[] args) {
		System.out.println(pass());
	}

	public static String pass() {
		return pass(30, PassType.lowercase, PassType.uppercase, PassType.numbers, PassType.special);
	}

	public static String pass(int length, PassType type, PassType... types) {
		EnumSet<PassType> t = EnumSet.of(type, types);
		List<String> list = new ArrayList<>();
		String S = "";
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
		RandomStringUtils.random(length, 0, 0, false, false, S.toCharArray(), random).chars().mapToObj(c -> String.valueOf((char) c)).forEach(list::add);
		Collections.shuffle(list);
		String p = list.stream().collect(Collectors.joining());
		return p;
	}

	public static String uuid() {
		return java.util.UUID.randomUUID().toString();
	}
}
