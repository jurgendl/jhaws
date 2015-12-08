package org.jhaws.common.lang;

import java.text.Normalizer;

public interface StringUtils {
	public static final char[] REGULARS = "\\<([{^-=$!|]})?*+.>".toCharArray();

	public static final char[] whitespace_chars = ( //
	"\u0009" // CHARACTER TABULATION
			+ "\n" // LINE FEED (LF)
			+ "\u000B" // LINE TABULATION
			+ "\u000C" // FORM FEED (FF)
			+ "\r" // CARRIAGE RETURN (CR)
			+ "\u0020" // SPACE
			+ "\u0085" // NEXT LINE (NEL)
			+ "\u00A0" // NO-BREAK SPACE
			+ "\u1680" // OGHAM SPACE MARK
			+ "\u180E" // MONGOLIAN VOWEL SEPARATOR
			+ "\u2000" // EN QUAD
			+ "\u2001" // EM QUAD
			+ "\u2002" // EN SPACE
			+ "\u2003" // EM SPACE
			+ "\u2004" // THREE-PER-EM SPACE
			+ "\u2005" // FOUR-PER-EM SPACE
			+ "\u2006" // SIX-PER-EM SPACE
			+ "\u2007" // FIGURE SPACE
			+ "\u2008" // PUNCTUATION SPACE
			+ "\u2009" // THIN SPACE
			+ "\u200A" // HAIR SPACE
			+ "\u2028" // LINE SEPARATOR
			+ "\u2029" // PARAGRAPH SEPARATOR
			+ "\u202F" // NARROW NO-BREAK SPACE
			+ "\u205F" // MEDIUM MATHEMATICAL SPACE
			+ "\u3000" // IDEOGRAPHIC SPACE
	).toCharArray();

	public static boolean isWhiteSpace(char c) {
		for (char ws : StringUtils.whitespace_chars) {
			if (ws == c) {
				return true;
			}
		}
		return false;
	}

	public static String removeUnnecessaryWhiteSpaces(String s) {
		StringBuilder sb = new StringBuilder();
		boolean wasWhiteSpace = false; // was previous character(s) whitespaces
		boolean start = true; // to remove whitespaces at front
		for (char c : s.toCharArray()) {
			if (StringUtils.isWhiteSpace(c)) {
				if (start || wasWhiteSpace) {
					continue;
				}
				wasWhiteSpace = true;
			} else {
				if (wasWhiteSpace) {
					sb.append(" "); // replace all previous whitespaces by a single space
				}
				sb.append(c); // append non-whitespace character
				wasWhiteSpace = false;
				start = false;
			}
		}
		return sb.toString();
	}

	public static String abc(String string) {
		if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : Normalizer.normalize(string, Normalizer.Form.NFKD).toCharArray()) {
			if (('A' <= c) && (c <= 'Z')) {
				sb.append(c);
			} else {
				if (('a' <= c) && (c <= 'z')) {
					sb.append(c);
				} else {
					sb.append(' ');
				}
			}
		}
		return sb.toString();
	}

	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	public static String sortable(String string) {
		if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : Normalizer.normalize(string, Normalizer.Form.NFKD).toUpperCase().toCharArray()) {
			if (('A' <= c) && (c <= 'Z')) {
				sb.append(c);
			} else {
				if (('0' <= c) && (c <= '9')) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String regularize(String s) {
		for (int i = 0; i < REGULARS.length; i++) {
			s = s.replaceAll("\\" + REGULARS[i], "\\\\" + REGULARS[i]);
		}
		return s;
	}

	public static String escape(String s) {
		s = s.replaceAll("\\\\", "\\\\\\\\");
		s = s.replaceAll("\"", "\\\\\"");
		return s;
	}

	/**
	 * @see http://stackoverflow.com/questions/4775898/java-regex-until-certain-word-text-characters
	 */
	public static String regularUntil(String s) {
		return "(.*?)\\b" + s + ".*";
	}
}
