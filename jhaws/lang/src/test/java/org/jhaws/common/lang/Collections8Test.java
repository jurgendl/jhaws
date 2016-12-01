package org.jhaws.common.lang;

import static org.jhaws.common.lang.CollectionUtils8.array;
import static org.jhaws.common.lang.CollectionUtils8.split;
import static org.jhaws.common.lang.CollectionUtils8.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import org.jhaws.common.lang.CollectionUtils8.Opt;
import org.jhaws.common.lang.functions.EConsumer;
import org.jhaws.common.lang.functions.EPredicate;
import org.junit.Assert;
import org.junit.Test;

public class Collections8Test {
	private static final int warmup = 100;

	private static final int testcount = 10000;

	@Test
	public void testSpeed() {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		String[] array = new String[10000];
		for (int i = 0; i < array.length; i++) {
			array[i] = String.valueOf(System.currentTimeMillis());
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < warmup; i++) {
			List<String> l = toList(array);
		}
		start = System.currentTimeMillis();
		for (int i = 0; i < warmup; i++) {
			List<String> l = new ArrayList<>(Arrays.asList(array));
		}
		start = System.currentTimeMillis();
		for (int i = 0; i < testcount; i++) {
			List<String> l = toList(array);
		}
		System.out.println("new:" + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		for (int i = 0; i < testcount; i++) {
			List<String> l = new ArrayList<>(Arrays.asList(array));
		}
		System.out.println("old:" + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test1() {
		Collection<String> c = Arrays.asList("v1", "v2", "v3");
		array(c);
		array(c.parallelStream());
	}

	@Test
	public void testRegexIterator() {
		RegexIterator it = new RegexIterator("(\\d)(\\d)", "test string, 123, abc, the end");
		// FIXME
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
			asList.stream().filter(EPredicate.enhance(e -> throwEx(e))).forEach(EConsumer.enhance(e -> throwEx(e)));
			Assert.fail("must throw exception");
		} catch (Exception e) {
			Assert.assertTrue("" + e.getCause(), e.getCause() instanceof ChkException);
			Assert.assertTrue(e.getCause().getMessage(), asList.contains(e.getCause().getMessage()));
		}
		try {
			asList.stream().filter((EPredicate<String>) e -> throwEx(e)).forEach((EConsumer<String>) e -> throwEx(e));
			Assert.fail("must throw exception");
		} catch (Exception e) {
			Assert.assertTrue("" + e.getCause(), e.getCause() instanceof ChkException);
			Assert.assertTrue(e.getCause().getMessage(), asList.contains(e.getCause().getMessage()));
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

	@Test
	public void testSizePartitioning() {
		HashSet<String> all = new HashSet<>();
		for (int i = 0; i < 168000; i++) {
			all.add(java.util.UUID.randomUUID().toString());
		}
		int maxSize = 9000;
		HashSet<String> all2 = new HashSet<>();
		for (Collection<String> gg : split(all, maxSize)) {
			all2.addAll(gg);
		}
		Assert.assertEquals(all, all2);
	}

	public static class TPersoon {
		TPersoonNaam naam;

		public TPersoonNaam getNaam() {
			return this.naam;
		}

		public void setNaam(TPersoonNaam naam) {
			this.naam = naam;
		}
	}

	public static class TPersoonNaam {
		TNaam naam;

		public TNaam getNaam() {
			return this.naam;
		}

		public void setNaam(TNaam naam) {
			this.naam = naam;
		}
	}

	public static class TNaam {
		String naam;

		public String getNaam() {
			return this.naam;
		}

		public void setNaam(String naam) {
			this.naam = naam;
		}
	}

	@Test
	public void testEagerOpt1a() {
		TPersoon persoon = new TPersoon();
		Assert.assertNull(Opt.eager(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).opt(TNaam::getNaam).get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), Opt.eager(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).opt(TNaam::getNaam).get());
	}

	@Test
	public void testEagerOpt1b() {
		TPersoon persoon = new TPersoon();
		Assert.assertNull(Opt.eager(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).get(TNaam::getNaam));
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), Opt.eager(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).get(TNaam::getNaam));
	}

	@Test
	public void testEagerOpt2() {
		TPersoon persoon = new TPersoon();
		Opt<String> notReusable = Opt.eager(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).opt(TNaam::getNaam);
		Assert.assertNull(notReusable.get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertNull(notReusable.get());
	}

	@Test
	public void testReusableOpta() {
		TPersoon persoon = new TPersoon();
		Opt<String> opt = Opt.reusable(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam).opt(TNaam::getNaam);
		Assert.assertNull(opt.get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), opt.get());
	}

	@Test
	public void testReusableOptb() {
		TPersoon persoon = new TPersoon();
		Opt<TNaam> opt = Opt.reusable(persoon).opt(TPersoon::getNaam).opt(TPersoonNaam::getNaam);
		Assert.assertNull(opt.get(TNaam::getNaam));
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), opt.get(TNaam::getNaam));
	}

	@Test
	public void index() {
		CollectionUtils8.index(IntStream.range(0, 26).mapToObj(i -> (char) ('A' + i))).forEach(System.out::println);
	}
}
