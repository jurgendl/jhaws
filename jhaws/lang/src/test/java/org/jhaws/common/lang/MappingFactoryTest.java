package org.jhaws.common.lang;

import org.junit.Test;

public class MappingFactoryTest {
	@Test
	public void test1() {
	}
//	private MappingFactory mappingFactory = new MappingFactory();
//
//	private Pojo getTestPojo(String suffix, int index) {
//		Pojo pojo = new Pojo();
//		pojo.setVeld1("veld1_" + suffix);
//		pojo.setVeld2(index);
//		pojo.setVeld3("veld3_" + suffix);
//		{
//			CommonNestedPojo commonNested = new CommonNestedPojo();
//			commonNested.setCommonNestedField("commonNestedField");
//			pojo.setCommonNested(commonNested);
//		}
//		{
//			CommonNestedPojo commonNested = new CommonNestedPojo();
//			commonNested.setCommonNestedField("commonNestedField");
//			pojo.getCollection().add(commonNested);
//		}
//		{
//			CommonNestedPojo commonNested = new CommonNestedPojo();
//			commonNested.setCommonNestedField("commonNestedField");
//			pojo.setArray(new CommonNestedPojo[] { commonNested });
//		}
//		return pojo;
//	}
//
//	protected Mapping<Pojo, DTO> init() {
//		Mapping<Pojo, DTO> mapping = this.mappingFactory.build(Pojo.class, DTO.class)//
//				.add((ctx, s, t) -> t.setNestedDTOVeld(s.getNestedPojo().getNestedVeld()))//
//				.add((ctx, s, t) -> t.getNestedDTO().setNestedVeld(s.getNestedPojoVeld()))//
//				.collect(this.mappingFactory, Pojo::getCollection, DTO::getCollection, CommonNestedDTO.class)//
//				.collectAA(this.mappingFactory, Pojo::getArray, DTO::getArray, CommonNestedDTO.class)//
//		;
//		this.mappingFactory.build(CommonNestedPojo.class, CommonNestedDTO.class);
//		this.mappingFactory.build(Retro.class, RetroB.class);
//		this.mappingFactory.build(SourceProb.class, TargetProb.class);
//		return mapping;
//	}
//
//	@Test
//	public void test1() {
//		this.init();
//
//		Pojo pojo = this.getTestPojo("test1", 1);
//		DTO dto = this.mappingFactory.map(pojo, DTO.class);
//		Assert.assertEquals(pojo.getVeld1(), dto.getVeld1());
//		Assert.assertEquals(pojo.getVeld2(), dto.getVeld2().intValue());
//		Assert.assertEquals(pojo.getVeld3(), dto.getVeld3());
//		Assert.assertEquals(pojo.getNestedPojo().getNestedVeld(), dto.getNestedDTOVeld());
//		Assert.assertEquals(pojo.getNestedPojoVeld(), dto.getNestedDTO().getNestedVeld());
//		Assert.assertEquals(pojo.getCommonNested().getCommonNestedField(),
//				dto.getCommonNested().getCommonNestedField());
//		Assert.assertEquals(pojo.getCollection().iterator().next().getCommonNestedField(),
//				dto.getCollection().iterator().next().getCommonNestedField());
//	}
//
//	@Test
//	public void test2() {
//		this.init();
//
//		Retro r1 = new Retro();
//		Retro r2 = new Retro();
//		r1.setRetro(r2);
//		r2.setRetro(r1);
//		this.mappingFactory.map(r1, RetroB.class);
//	}
//
//	// FIXME @Test
//	public void test3() {
//		this.init();
//
//		List<Pojo> pc = new ArrayList<>();
//		Pojo p1 = this.getTestPojo("test3a", 30);
//		pc.add(p1);
//		Pojo p2 = this.getTestPojo("test3b", 31);
//		pc.add(p2);
//		Pojo[] pa = { p1, p2 };
//		{
//			List<DTO> c = this.mappingFactory.map(pc, DTO.class, toCollector(newList()));
//			Assert.assertEquals(pc.size(), c.size());
//			Assert.assertEquals(c.get(0).getVeld1(), p1.getVeld1());
//			Assert.assertEquals(c.get(0).getVeld2().intValue(), p1.getVeld2());
//			Assert.assertEquals(c.get(0).getVeld3(), p1.getVeld3());
//			Assert.assertEquals(c.get(1).getVeld1(), p2.getVeld1());
//			Assert.assertEquals(c.get(1).getVeld2().intValue(), p2.getVeld2());
//			Assert.assertEquals(c.get(1).getVeld3(), p2.getVeld3());
//		}
//		{
//			DTO[] a = this.mappingFactory.map(pc, DTO.class);
//			Assert.assertEquals(pc.size(), a.length);
//			Assert.assertEquals(a[0].getVeld1(), p1.getVeld1());
//			Assert.assertEquals(a[0].getVeld2().intValue(), p1.getVeld2());
//			Assert.assertEquals(a[0].getVeld3(), p1.getVeld3());
//			Assert.assertEquals(a[1].getVeld1(), p2.getVeld1());
//			Assert.assertEquals(a[1].getVeld2().intValue(), p2.getVeld2());
//			Assert.assertEquals(a[1].getVeld3(), p2.getVeld3());
//		}
//		{
//			DTO[] a = this.mappingFactory.map(pa, DTO.class);
//			Assert.assertEquals(pc.size(), a.length);
//			Assert.assertEquals(a[0].getVeld1(), p1.getVeld1());
//			Assert.assertEquals(a[0].getVeld2().intValue(), p1.getVeld2());
//			Assert.assertEquals(a[0].getVeld3(), p1.getVeld3());
//			Assert.assertEquals(a[1].getVeld1(), p2.getVeld1());
//			Assert.assertEquals(a[1].getVeld2().intValue(), p2.getVeld2());
//			Assert.assertEquals(a[1].getVeld3(), p2.getVeld3());
//		}
//		{
//			List<DTO> c = this.mappingFactory.map(pa, DTO.class, toCollector(newList()));
//			Assert.assertEquals(pc.size(), c.size());
//			Assert.assertEquals(c.get(0).getVeld1(), p1.getVeld1());
//			Assert.assertEquals(c.get(0).getVeld2().intValue(), p1.getVeld2());
//			Assert.assertEquals(c.get(0).getVeld3(), p1.getVeld3());
//			Assert.assertEquals(c.get(1).getVeld1(), p2.getVeld1());
//			Assert.assertEquals(c.get(1).getVeld2().intValue(), p2.getVeld2());
//			Assert.assertEquals(c.get(1).getVeld3(), p2.getVeld3());
//		}
//	}
//
//	@Test
//	public void test4() {
//		this.init();
//
//		SourceProb s = new SourceProb();
//		s.setDate(new Date());
//		TargetProb t = new TargetProb();
//		this.mappingFactory.map(s, t);
//		Assert.assertEquals(s.getDate(), t.getDate());
//	}
//
//	@Test
//	public void test5() {
//		SourceProb s = new SourceProb();
//		s.test();
//		SourceProb p = Mapping.proxy(s, SourceProb.class, this.mappingFactory.info(SourceProb.class));
//		p.test();
//		Assert.assertNotNull(p.getDate());
//		Assert.assertEquals(s.getDate(), p.getDate());
//	}
}
