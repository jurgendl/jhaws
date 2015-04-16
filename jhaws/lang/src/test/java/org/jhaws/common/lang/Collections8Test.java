package org.jhaws.common.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jhaws.common.lang.functions.EConsumer;
import org.jhaws.common.lang.functions.EPredicate;
import org.junit.Assert;
import org.junit.Test;

public class Collections8Test {
	@Test
	public void test1() {
		Collection<String> c = Arrays.asList("v1", "v2", "v3");
		Collections8.array(c);
		Collections8.array(c.parallelStream());
	}

	@Test
	public void testRegexIterator() {
		RegexIterator it = new RegexIterator("(\\d)(\\d)", "test string, 123, abc, the end");
		Assert.assertEquals("test string, [1/2]3, abc, the end", it.streamFunction(match -> "[" + match.group(1) + "/" + match.group(2) + "]"));
		Assert.assertEquals("test string, 3, abc, the end", it.streamFunction(match -> ""));
		Assert.assertEquals("test string, 0.53, abc, the end", it.streamFunction(match -> Double.parseDouble(match.group(1)) / Double.parseDouble(match.group(2))));
		Assert.assertEquals(Arrays.asList("12"), it.simple());
		Assert.assertEquals(Arrays.asList(Arrays.asList("1", "2")), it.all());
	}

	@Test
	public void testException() {
		List<String> asList = Arrays.asList("1", "2", "3");
		try {
			asList.stream().parallel().filter(EPredicate.enhance(e -> throwEx(e))).forEach(EConsumer.enhance(e -> throwEx(e)));
			Assert.fail("must throw exception");
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof ChkException);
			Assert.assertTrue(asList.contains(e.getCause().getMessage()));
		}
		try {
			asList.stream().parallel().filter((EPredicate<String>) e -> throwEx(e)).forEach((EConsumer<String>) e -> throwEx(e));
			Assert.fail("must throw exception");
		} catch (Exception e) {
			Assert.assertTrue(e.getCause() instanceof ChkException);
			Assert.assertTrue(asList.contains(e.getCause().getMessage()));
		}
	}

	private static boolean throwEx(String key) throws ChkException {
		throw new ChkException(key);
	}

	private static class ChkException extends Exception {
		private static final long serialVersionUID = -8122612735696376609L;

		public ChkException(String message) {
			super(message);
		}
	}
}
