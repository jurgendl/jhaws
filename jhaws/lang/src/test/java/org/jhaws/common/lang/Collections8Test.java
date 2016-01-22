package org.jhaws.common.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.jhaws.common.lang.Collections8.Opt;
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
		for (Collection<String> gg : Collections8.split(all, maxSize)) {
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
	public void testEagerOpt1() {
		TPersoon persoon = new TPersoon();
		Assert.assertNull(Opt.eager(persoon).nest(TPersoon::getNaam).nest(TPersoonNaam::getNaam).nest(TNaam::getNaam).get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), Opt.eager(persoon).nest(TPersoon::getNaam).nest(TPersoonNaam::getNaam).nest(TNaam::getNaam).get());
	}

	@Test
	public void testEagerOpt2() {
		TPersoon persoon = new TPersoon();
		Opt<String> notReusable = Opt.eager(persoon).nest(TPersoon::getNaam).nest(TPersoonNaam::getNaam).nest(TNaam::getNaam);
		Assert.assertNull(notReusable.get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertNull(notReusable.get());
	}

	@Test
	public void testReusableOpt() {
		TPersoon persoon = new TPersoon();
		Opt<String> opt = Opt.reusable(persoon).nest(TPersoon::getNaam).nest(TPersoonNaam::getNaam).nest(TNaam::getNaam);
		Assert.assertNull(opt.get());
		TPersoonNaam pn = new TPersoonNaam();
		persoon.setNaam(pn);
		TNaam n = new TNaam();
		pn.setNaam(n);
		n.setNaam("naam");
		Assert.assertEquals(n.getNaam(), opt.get());
	}
}
