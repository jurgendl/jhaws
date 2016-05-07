package org.jhaws.common.lang;

import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see http://www.mscharhag.com/2014/02/java-8-datetime-api.html
 * @see http://blog.progs.be/542/date-to-java-time
 */
public class DateTime8 {
	public static final LocalTime START_OF_DAY = LocalTime.of(0, 0, 0);

	public static char decimalSeperator;

	static {
		// http://stackoverflow.com/questions/4713166/decimal-separator-in-numberformat
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		decimalSeperator = symbols.getDecimalSeparator();
	}

	public static char getDecimalSeperator() {
		return decimalSeperator;
	}

	public static void setDecimalSeperator(char decimalSeperator) {
		DateTime8.decimalSeperator = decimalSeperator;
	}

	public static String printshort(Duration duration) {
		long days = duration.toDays();
		duration = duration.minusDays(days);
		long hours = duration.toHours();
		duration = duration.minusHours(hours);
		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);
		long seconds = duration.toMillis() / 1000;
		duration = duration.minusSeconds(seconds);
		long millis = duration.toMillis();
		StringBuilder formatted = new StringBuilder();
		if ((days != 0) || (formatted.length() > 0)) {
			throw new UnsupportedOperationException();
		}
		if ((hours != 0) || (formatted.length() > 0)) {
			formatted.append(hours).append(":");
		}
		if ((minutes != 0) || (formatted.length() > 0)) {
			formatted.append(minutes).append(":");
		}
		if ((seconds != 0) || (formatted.length() > 0)) {
			if (millis > 0) {
				formatted.append(seconds).append(getDecimalSeperator()).append(millis).append("s");
			} else {
				formatted.append(seconds);
			}
		}
		return formatted.toString();
	}

	public static String print(Duration duration) {
		long days = duration.toDays();
		duration = duration.minusDays(days);
		long hours = duration.toHours();
		duration = duration.minusHours(hours);
		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);
		long seconds = duration.toMillis() / 1000;
		duration = duration.minusSeconds(seconds);
		long millis = duration.toMillis();
		StringBuilder formatted = new StringBuilder();
		if ((days != 0) || (formatted.length() > 0)) {
			formatted.append(days).append("d");
		}
		if ((hours != 0) || (formatted.length() > 0)) {
			if (hours < 10 && formatted.length() > 0) {
				formatted.append("0");
			}
			formatted.append(hours).append("h");
		}
		if ((minutes != 0) || (formatted.length() > 0)) {
			if (minutes < 10 && formatted.length() > 0) {
				formatted.append("0");
			}
			formatted.append(minutes).append("m");
		}
		{
			if (seconds < 10 && formatted.length() > 0) {
				formatted.append("0");
			}
			formatted.append(seconds);
			if (millis > 0) {
				formatted.append(getDecimalSeperator());
				if (millis < 100) {
					formatted.append("0");
				}
				if (millis < 10) {
					formatted.append("0");
				}
				formatted.append(millis);
			}
			formatted.append("s");
		}
		return formatted.toString();
	}

	public static final DateTimeFormatter TIME_PARSER_MILLIS = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.HOUR_OF_DAY).appendLiteral(":").appendValue(ChronoField.MINUTE_OF_HOUR, 2)
			.appendLiteral(":").appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendLiteral(".")
			.appendValue(ChronoField.MILLI_OF_SECOND).toFormatter();

	public static final DateTimeFormatter TIME_PARSER_SEC = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.HOUR_OF_DAY).appendLiteral(":").appendValue(ChronoField.MINUTE_OF_HOUR, 2)
			.appendLiteral(":").appendValue(ChronoField.SECOND_OF_MINUTE, 2).toFormatter();

	static Pattern TIME_PATTERN = Pattern.compile("^(\\d{1,2}:){0,1}(\\d{1,2}:){0,1}(\\d{1,2}){1}(\\.\\d{1,3}){0,1}$");

	public static LocalTime parseTime(String text) {
		Matcher matcher = TIME_PATTERN.matcher(text);
		matcher.matches();
		String p1 = matcher.group(1);
		String p2 = matcher.group(2);
		String p3 = matcher.group(3);
		String milliseconds = matcher.group(4);
		int h = 0;
		int m = 0;
		int s = 0;
		int ms = 0;
		if (p1 != null && p2 != null) {
			h = Integer.parseInt(p1.replaceAll(":", ""));
			m = Integer.parseInt(p2.replaceAll(":", ""));
		} else if (p1 != null && p2 == null) {
			m = Integer.parseInt(p1.replaceAll(":", ""));
		} else if (p1 == null && p2 != null) {
			m = Integer.parseInt(p2.replaceAll(":", ""));
		}
		if (p3 != null) {
			s = Integer.parseInt(p3);
		}
		if (milliseconds != null) {
			ms = Integer.parseInt(milliseconds.replaceAll("\\.", ""));
			if (ms < 10) {
				ms *= 100000000;
			} else if (ms < 100) {
				ms *= 10000000;
			} else {
				ms *= 1000000;
			}
		}
		return LocalTime.of(h, m, s, ms);
	}

	public static Duration parseDuration(String from, String to) {
		return Duration.between(parseTime(from), parseTime(to));
	}

	public static Duration toDuration(LocalTime time) {
		return Duration.between(START_OF_DAY, time);
	}

	public static LocalTime toLocalTime(Duration duration) {
		return START_OF_DAY.plus(duration);
	}

	public static LocalDate toLocalDate(Date date) {
		return toLocalDate(date, ZoneId.systemDefault());
	}

	public static LocalDate toLocalDate(Instant instant) {
		return toLocalDate(instant, ZoneId.systemDefault());
	}

	public static LocalDate toLocalDate(Date date, ZoneId zone) {
		return toLocalDate(Instant.ofEpochMilli(date.getTime()), zone);
	}

	public static LocalDate toLocalDate(Instant instant, ZoneId zone) {
		return LocalDateTime.ofInstant(instant, zone).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return toLocalDateTime(date, ZoneId.systemDefault());
	}

	public static LocalDateTime toLocalDateTime(Date date, ZoneId zone) {
		return toLocalDateTime(date.toInstant(), zone);
	}

	public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zone) {
		return LocalDateTime.ofInstant(instant, zone);
	}

	public static LocalDateTime toLocalDateTime(Instant instant) {
		return toLocalDateTime(instant, ZoneId.systemDefault());
	}

	public static Date toDate(ChronoLocalDate date) {
		return toDate(date, ZoneId.systemDefault());
	}

	public static Date toDate(ChronoLocalDate date, ZoneId zone) {
		return toDate(date.atTime(LocalTime.ofSecondOfDay(0)).atZone(zone).toInstant());
	}

	public static Date toDate(@SuppressWarnings("rawtypes") ChronoLocalDateTime date) {
		return toDate(date, 0);
	}

	public static Date toDate(@SuppressWarnings("rawtypes") ChronoLocalDateTime date, int zoneOffset) {
		return toDate(date.toInstant(ZoneOffset.ofHours(zoneOffset)));
	}

	public static Date toDate(Instant instant) {
		return Date.from(instant);
	}

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static LocalDateTime toLocalDateTime(FileTime time) {
		return toLocalDateTime(time.toInstant());
	}

	public static LocalDateTime toLocalDateTime(FileTime time, ZoneId zone) {
		return toLocalDateTime(time.toInstant(), zone);
	}
}
