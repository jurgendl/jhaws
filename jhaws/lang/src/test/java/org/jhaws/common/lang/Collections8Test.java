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
	public void test2() {
		String s = "test string, 123, abc, the end";
		String t = new RegexIterator("(1)(2)", s).stream(m -> "[" + m.group(1) + "/" + m.group(2) + "]");
		Assert.assertEquals("test string, [1/2]3, abc, the end", t);
	}
}
