package org.jhaws.common.lang;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DecimalSystem {
	/** https://en.wikipedia.org/wiki/Unit_prefix */
	private static final Map<Locale, List<String>> shortdata;
	private static final Map<Locale, List<String>> longdata;
	private static final List<Integer> powers;
	private static DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS;
	static {
		{
			powers = Arrays.asList(-18, -15, -12, -9, -6, -3, -2, -1, 0, 1, 2, 3, 6, 9, 12, 15, 18);
		}
		{
			Map<Locale, List<String>> tmp = new HashMap<>();
			tmp.put(null, Arrays.asList("a", "f", "p", "n", "μ", "m", "c", "d", "", "da", "h", "k", "M", "G", "T", "P", "E", "Z", "Y", "X", "W"));
			shortdata = Collections.unmodifiableMap(tmp);
		}
		{
			Map<Locale, List<String>> tmp = new HashMap<>();
			tmp.put(null, Arrays.asList("atto", "femto", "pico", "nano", "micro", "milli", "centi", "deci", "", "deca", "hecto", "kilo", "mega", "giga", "tera", "peta", "exa",
					"zetta", "yotta", "xona", "weka"));
			longdata = Collections.unmodifiableMap(tmp);
		}
	}

	public static List<String> shortvalues() {
		return shortvalues(Locale.getDefault());
	}

	public static List<String> longvalues() {
		return longvalues(Locale.getDefault());
	}

	public static List<String> shortvalues(Locale locale) {
		return values(shortdata, locale);
	}

	public static List<String> longvalues(Locale locale) {
		return values(longdata, locale);
	}

	public static List<Integer> power() {
		return powers;
	}

	public static String shortvalue(int power) {
		return shortvalue(Locale.getDefault(), power);
	}

	public static String longvalue(int power) {
		return longvalue(Locale.getDefault(), power);
	}

	public static String shortvalue(Locale locale, int power) {
		return values(shortdata, locale).get(powers.indexOf(power));
	}

	public static String longvalue(Locale locale, int power) {
		return values(longdata, locale).get(powers.indexOf(power));
	}

	private static List<String> values(Map<Locale, List<String>> data, Locale locale) {
		List<String> tmp = data.get(locale);
		if (tmp == null && locale.getVariant() != null) {
			locale = new Locale(locale.getLanguage(), locale.getCountry());
			tmp = data.get(data);
		}
		if (tmp == null && locale.getCountry() != null) {
			locale = new Locale(locale.getLanguage());
			tmp = data.get(data);
		}
		if (tmp == null) {
			tmp = data.get(null);
		}
		return tmp;
	}

	public static DecimalFormatSymbols getDecimalFormatSymbols() {
		if (DECIMAL_FORMAT_SYMBOLS == null) {
			DECIMAL_FORMAT_SYMBOLS = DecimalFormatSymbols.getInstance();
		}
		return DECIMAL_FORMAT_SYMBOLS;
	}

	public static void setDecimalFormatSymbols(Locale locale) {
		DECIMAL_FORMAT_SYMBOLS = DecimalFormatSymbols.getInstance(locale);
	}
}
