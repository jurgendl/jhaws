package org.jhaws.common.lang;

import java.util.Arrays;
import java.util.Collection;

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
		Assert.assertEquals("test string, [1/2]3, abc, the end", it.stream(match -> "[" + match.group(1) + "/" + match.group(2) + "]"));
		Assert.assertEquals("test string, 3, abc, the end", it.stream(match -> ""));
		Assert.assertEquals("test string, 0.53, abc, the end", it.stream(match -> Double.parseDouble(match.group(1)) / Double.parseDouble(match.group(2))));
		Assert.assertEquals(Arrays.asList("12"), it.simple());
		Assert.assertEquals(Arrays.asList(Arrays.asList("1", "2")), it.all());
	}
}
