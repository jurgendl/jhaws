package org.jhaws.common.g11n;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @see https://docs.oracle.com/javase/tutorial/i18n/format/numberFormat.html
 * @see https://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
 */
public class G11n {
	static {
		Locale defaultLocale;
		try {
			// (1) Java 1.7 compilable in Java 1.6 but gives Exception at runtime so we can fall back to (2)
			@SuppressWarnings("rawtypes")
			Class type = Class.forName("java.util.Locale$Category");
			@SuppressWarnings("unchecked")
			Object enumvalue = Enum.valueOf(type, "FORMAT");
			defaultLocale = Locale.class.cast(Locale.class.getMethod("getDefault", type).invoke(null, enumvalue));
		} catch (Exception ex) {
			// (2) Java 1.6 (gives wrong info in Java 1.7)
			defaultLocale = Locale.getDefault();
		}
		Locale.setDefault(defaultLocale);
	}

	/**
	 * {@link Locale#getDefault()}
	 */
	public static Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	/**
	 * {@link Locale#getAvailableLocales()}
	 */
	public static List<Locale> getAvailableLocales() {
		return Arrays.asList(Locale.getAvailableLocales());
	}

	/**
	 * can also be set via command line parameter: "-Duser.country=UK -Duser.language=en"; use this instead of {@link Locale#setDefault(Locale)} to change Locale of all
	 * {@link EComponentI}s dynamically
	 */
	public static void setCurrentLocale(Locale currentLocale) {
		Locale.setDefault(currentLocale);
		decimalFormatSymbols = null;
		dateFormatSymbols = null;
		shortDateFormat = null;
		mediumDateFormat = null;
		longDateFormat = null;
		shortTimeFormat = null;
		mediumTimeFormat = null;
		longTimeFormat = null;
		shortDateTimeFormat = null;
		mediumDateTimeFormat = null;
		longDateTimeFormat = null;
		currencySymbols = null;
		currencyFormat = null;
		decimalNumberFormat = null;
		integerNumberFormat = null;
		percentNumberFormat = null;
	}

	private static DecimalFormatSymbols decimalFormatSymbols;
	private static NumberFormat decimalNumberFormat;
	private static NumberFormat integerNumberFormat;
	private static NumberFormat percentNumberFormat;

	public static DecimalFormatSymbols getDecimalFormatSymbols() {
		if (G11n.decimalFormatSymbols == null) {
			G11n.decimalFormatSymbols = DecimalFormatSymbols.getInstance(getCurrentLocale());
		}
		return G11n.decimalFormatSymbols;
	}

	public static void setDecimalFormatSymbols(DecimalFormatSymbols symbols) {
		G11n.decimalFormatSymbols = symbols;
	}

	public static NumberFormat getDecimalNumberFormat() {
		if (decimalNumberFormat == null)
			decimalNumberFormat = NumberFormat.getNumberInstance(getCurrentLocale());
		return decimalNumberFormat;
	}

	public static void setDecimalNumberFormat(NumberFormat decimalNumberFormat) {
		G11n.decimalNumberFormat = decimalNumberFormat;
	}

	public static NumberFormat getIntegerNumberFormat() {
		if (integerNumberFormat == null)
			integerNumberFormat = NumberFormat.getIntegerInstance(getCurrentLocale());
		return integerNumberFormat;
	}

	public static void setIntegerNumberFormat(NumberFormat integerNumberFormat) {
		G11n.integerNumberFormat = integerNumberFormat;
	}

	public static NumberFormat getPercentNumberFormat() {
		if (percentNumberFormat == null)
			percentNumberFormat = NumberFormat.getPercentInstance(getCurrentLocale());
		return percentNumberFormat;
	}

	public static void setPercentNumberFormat(NumberFormat percentNumberFormat) {
		G11n.percentNumberFormat = percentNumberFormat;
	}

	private static DateFormatSymbols dateFormatSymbols;
	private static DateFormat shortDateFormat;
	private static DateFormat mediumDateFormat;
	private static DateFormat longDateFormat;
	private static DateFormat shortTimeFormat;
	private static DateFormat mediumTimeFormat;
	private static DateFormat longTimeFormat;
	private static DateFormat shortDateTimeFormat;
	private static DateFormat mediumDateTimeFormat;
	private static DateFormat longDateTimeFormat;

	public static DateFormatSymbols getDateFormatSymbols() {
		if (G11n.dateFormatSymbols == null) {
			G11n.dateFormatSymbols = DateFormatSymbols.getInstance(getCurrentLocale());
		}
		return G11n.dateFormatSymbols;
	}

	public static void setDateFormatSymbols(DateFormatSymbols symbols) {
		G11n.dateFormatSymbols = symbols;
	}

	public static DateFormat getShortDateFormat() {
		if (G11n.shortDateFormat == null) {
			G11n.shortDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, getCurrentLocale());
		}
		return shortDateFormat;
	}

	public static void setShortDateFormat(DateFormat format) {
		G11n.shortDateFormat = format;
	}

	public static DateFormat getMediumDateFormat() {
		if (G11n.mediumDateFormat == null) {
			G11n.mediumDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, getCurrentLocale());
		}
		return mediumDateFormat;
	}

	public static void setMediumDateFormat(DateFormat format) {
		G11n.mediumDateFormat = format;
	}

	public static DateFormat getLongDateFormat() {
		if (G11n.longDateFormat == null) {
			G11n.longDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, getCurrentLocale());
		}
		return longDateFormat;
	}

	public static void setLongDateFormat(DateFormat format) {
		G11n.longDateFormat = format;
	}

	public static DateFormat getShortTimeFormat() {
		if (G11n.shortTimeFormat == null) {
			G11n.shortTimeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, getCurrentLocale());
		}
		return shortTimeFormat;
	}

	public static void setShortTimeFormat(DateFormat format) {
		G11n.shortTimeFormat = format;
	}

	public static DateFormat getMediumTimeFormat() {
		if (G11n.mediumTimeFormat == null) {
			G11n.mediumTimeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, getCurrentLocale());
		}
		return mediumTimeFormat;
	}

	public static void setMediumTimeFormat(DateFormat format) {
		G11n.mediumTimeFormat = format;
	}

	public static DateFormat getLongTimeFormat() {
		if (G11n.longTimeFormat == null) {
			G11n.longTimeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.LONG, getCurrentLocale());
		}
		return longTimeFormat;
	}

	public static void setLongTimeFormat(DateFormat format) {
		G11n.longTimeFormat = format;
	}

	public static DateFormat getShortDateTimeFormat() {
		if (G11n.shortDateTimeFormat == null) {
			G11n.shortDateTimeFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, getCurrentLocale());
		}
		return shortDateTimeFormat;
	}

	public static void setShortDateTimeFormat(DateFormat format) {
		G11n.shortDateTimeFormat = format;
	}

	public static DateFormat getMediumDateTimeFormat() {
		if (G11n.mediumDateTimeFormat == null) {
			G11n.mediumDateTimeFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM, getCurrentLocale());
		}
		return mediumDateTimeFormat;
	}

	public static void setMediumDateTimeFormat(DateFormat format) {
		G11n.mediumDateTimeFormat = format;
	}

	public static DateFormat getLongDateTimeFormat() {
		if (G11n.longDateTimeFormat == null) {
			G11n.longDateTimeFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG, getCurrentLocale());
		}
		return longDateTimeFormat;
	}

	public static void setLongDateTimeFormat(DateFormat format) {
		G11n.longDateTimeFormat = format;
	}

	private static Currency currencySymbols;
	private static NumberFormat currencyFormat;

	public static Currency getCurrencySymbols() {
		if (currencySymbols == null) {
			currencySymbols = Currency.getInstance(getCurrentLocale());
		}
		return currencySymbols;
	}

	public static void setCurrencySymbols(Currency currencySymbols) {
		G11n.currencySymbols = currencySymbols;
	}

	public static NumberFormat getCurrencyFormat() {
		if (currencyFormat == null) {
			currencyFormat = NumberFormat.getCurrencyInstance(getCurrentLocale());
		}
		return currencyFormat;
	}

	public static void setCurrencyFormat(NumberFormat currencyFormat) {
		G11n.currencyFormat = currencyFormat;
	}

	public static void main(String[] args) {
		System.out.println(G11n.getCurrentLocale());
		System.out.println(G11n.getCurrencyFormat().format(12345.09876));
		System.out.println(G11n.getLongDateFormat().format(new Date()));
		System.out.println(G11n.getLongTimeFormat().format(new Date()));
		System.out.println(G11n.getLongDateTimeFormat().format(new Date()));
		System.out.println(G11n.getDecimalNumberFormat().format(12345.09876));
		System.out.println(G11n.getIntegerNumberFormat().format(12345.09876));
		System.out.println(G11n.getPercentNumberFormat().format(12345.09876));
	}

}
