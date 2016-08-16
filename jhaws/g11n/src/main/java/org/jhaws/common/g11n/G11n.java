package org.jhaws.common.g11n;

import java.util.Locale;

public class G11n {
	static {
		Locale defaultLocale;
		try {
			// (1) Java 1.7 compilable in Java 1.6 but gives Exception at runtimee so we can fall back to (2)
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
	 * can also be set via command line parameter: "-Duser.country=UK -Duser.language=en"; use this instead of {@link Locale#setDefault(Locale)} to change Locale of all
	 * {@link EComponentI}s dynamically
	 */
	public static void setCurrentLocale(Locale currentLocale) {
		Locale.setDefault(currentLocale);
	}
}
