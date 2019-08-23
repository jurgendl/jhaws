package org.jhaws.common.lang;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// [^abc], which is anything but abc,
// or negative lookahead: a(?!b), which is a not followed by b
// or negative lookbehind: (?<!a)b, which is b not preceeded by a
public interface StringUtils {
	public static final String UTF8 = "UTF-8";

	public static final String REGULAR_STRING = "\\<([{^-=$!|]})?*+.>";

	public static final char[] REGULAR_ARRAY = REGULAR_STRING.toCharArray();

	public static final List<Character> REGULAR_LIST = Collections
			.unmodifiableList(CollectionUtils8.stream(REGULAR_STRING).collect(Collectors.toList()));

	/**
	 * because /s is not capturing all unicode whitespaces <br>
	 * or use \s\u0085\p{Z} instead <br>
	 * (since java 1.7) or set Pattern.UNICODE_CHARACTER_CLASS to true when
	 * creating Pattern <br>
	 * (since java 1.7) or use (?U)...\s instead
	 *
	 * @see http://stackoverflow.com/questions/4731055/whitespace-matching-regex-java
	 */
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
		for (char ws : whitespace_chars) {
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
			if (isWhiteSpace(c)) {
				if (start || wasWhiteSpace) {
					continue;
				}
				wasWhiteSpace = true;
			} else {
				if (wasWhiteSpace) {
					sb.append(" "); // replace all previous whitespaces by a
									// single space
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

	public static String abcSpace(String string) {
		if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : Normalizer.normalize(string, Normalizer.Form.NFKD).toCharArray()) {
			if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == ' ') {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String splitCamelCase(String s) {
		return s == null ? null
				: s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
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
		return s == null ? null
				: s.chars().mapToObj(i -> (char) i).map(StringUtils::regularize).collect(Collectors.joining());
	}

	public static String regularize(char c) {
		return REGULAR_LIST.contains(c) ? "\\" + c : String.valueOf(c);
	}

	public static String escape(String s) {
		s = s.replaceAll("\\\\", "\\\\\\\\");
		s = s.replaceAll("\"", "\\\\\"");
		return s;
	}

	/**
	 * @see http://stackoverflow.com/questions/4775898/java-regex-until-certain-
	 * word-text-characters
	 */
	public static String regularUntil(String s) {
		return "(.*?)\\b" + s + ".*";
	}

	public static boolean isNotBlank(String s) {
		return org.apache.commons.lang3.StringUtils.isNotBlank(s);
	}

	public static boolean isBlank(String s) {
		return org.apache.commons.lang3.StringUtils.isBlank(s);
	}

	public static boolean isNotEmpty(String s) {
		return org.apache.commons.lang3.StringUtils.isNotEmpty(s);
	}

	/** a not followed by b */
	public static String regexNotLookAhead(String a, String notB) {
		return groupRegex(a) + "(" + "?!" + notB + ")";
	}

	/** b not preceeded by a */
	public static String regexNotLookBehind(String notA, String b) {
		return "(" + "?<!" + notA + ")" + groupRegex(b);
	}

	public static String regexNot(String notA) {
		return "!" + notA;
	}

	public static String regexMultipleOr(String... x) {
		String r = x[0];
		for (int i = 1; i < x.length; i++) {
			r = regexOr(r, x[i]);
		}
		return r;
	}

	static String regexOr(String x, String y) {
		return "(" + groupRegex(x) + "|" + groupRegex(y) + ")";
	}

	public static String regexMultipleAnd(String... x) {
		String r = x[0];
		for (int i = 1; i < x.length; i++) {
			r = regexAnd(r, x[i]);
		}
		return r;
	}

	static String regexAnd(String x, String y) {
		return "((" + "?=" + x + ")" + groupRegex(y) + ")";
	}

	static String groupRegex(String x) {
		return x.startsWith("(") && x.endsWith(")") ? x : "(" + x + ")";
	}

	/**
	 * remove leading and trailing spaces and replace multiple spaces by 1 space
	 *
	 * @see http://stackoverflow.com/questions/2932392/java-how-to-replace-2-or-more-spaces-with-single-space-in-string-and-delete-lead
	 */
	public static String cleanSpaces(String s) {
		return s.replaceAll("^ +| +$|( )+", "$1");
	}

	public static String repeat(char c, int len) {
		char[] cc = new char[len];
		for (int i = 0; i < cc.length; i++) {
			cc[i] = c;
		}
		return new String(cc);
	}

	public static String replaceLeading(String string, char replaced, char replacedBy) {
		Pattern p = Pattern.compile(replaced + "++");
		Matcher matcher = p.matcher(string);
		if (matcher.find()) {
			string = matcher.replaceFirst(matcher.group(0).replace(replaced, replacedBy));
		}
		return string;
	}

	public static String replaceLeading(String string, String replaced, String replacedBy) {
		Pattern p = Pattern.compile("(" + replaced + ")++");
		Matcher matcher = p.matcher(string);
		if (matcher.find()) {
			string = matcher.replaceFirst(matcher.group(0).replace(replaced, replacedBy));
		}
		return string;
	}

	public static int count(String string, String findStr) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = string.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

	public static String ucLeading(String string) {
		if (string == null || string.length() == 0)
			return string;
		if (string.length() == 1)
			return String.valueOf(Character.toUpperCase(string.charAt(0)));
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static List<String> splitKeepDelimiter(String delimiter, String input) {
		return Arrays.asList(input.split(//
				"(?i)" // case insensitive
						+ "((?<="//
						+ delimiter//
						+ ")|(?="//
						+ delimiter//
						+ "))"//
		));
	}

	public static List<String> splitHtmlTagKeepDelimiter(String tag, String input) {
		return splitHtmlTagKeepDelimiter(tag, 100, input);
	}

	public static List<String> splitHtmlTagKeepDelimiter(String tag, int max, String input) {
		// max: could not use "+?" because of "Look-behind group does not have
		// an
		// obvious maximum length near index NR"
		String tagRegex = "(<" + tag + ">(.{0," + max + "})</" + tag + ">)";
		return splitKeepDelimiter(tagRegex, input);
	}

	public static String replaceLast(String text, String regex, String replacement) {
		// https://stackoverflow.com/questions/2282728/java-replacelast
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}
}
