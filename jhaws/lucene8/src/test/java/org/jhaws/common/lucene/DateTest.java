package org.jhaws.common.lucene;

import java.util.Date;

import org.junit.Test;

public class DateTest {
	@Test
	public void test() {
		Date today = new Date(2010 - 1900, 1 - 1, 1, 0, 0, 0);// 1262300400000
		java.time.LocalDateTime date = java.time.LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0);
		org.joda.time.LocalDateTime joda = new org.joda.time.LocalDateTime(2010, 1, 1, 0, 0, 0, 0);// 1262304000000
		// System.out.println(today);
		// System.out.println(date);
		// System.out.println(joda);

		java.time.ZoneId zone = java.time.ZoneId.of("UTC");
		java.time.LocalDateTime ldt = java.time.LocalDateTime.ofInstant(today.toInstant(), zone);
		// System.out.println(ldt);
		// Assert.assertEquals(date, ldt);

		java.time.ZonedDateTime zdt = ldt.atZone(zone);
		Date output = Date.from(zdt.toInstant());
		// System.out.println(output);

		org.joda.time.LocalDateTime jo = org.joda.time.LocalDateTime.fromDateFields(today);
		// System.out.println(jo);

		java.util.TimeZone tz = java.util.TimeZone.getTimeZone("UTC");
		Date d = joda.toDate(tz);
		// System.out.println(d);
	}
}
