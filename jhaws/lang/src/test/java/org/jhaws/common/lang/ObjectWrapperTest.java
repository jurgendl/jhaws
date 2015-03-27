package org.jhaws.common.lang;

import org.junit.Assert;
import org.junit.Test;

public class ObjectWrapperTest {
	public static class ChildClass extends ParentClass {
		String normalFieldChild = "normalFieldChild";

		final String finalFieldChild = "finalFieldChild";

		static String staticFieldChild = "staticFieldChild";

		static final String finalStaticFieldChild = "finalStaticFieldChild";
	}

	public static class OtherChildClass extends ParentClass {
		//
	}

	public static class ParentClass {
		String normalField = "normalField";

		final String finalField = "finalField";

		static String staticField = "staticField";

		static final String finalStaticField = "finalStaticField";
	}

	@Test
	public void test1ParentClassGet() {
		ObjectWrapper ow = new ObjectWrapper(ParentClass.class);

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		try {
			Assert.assertEquals("normalField", ow.get("normalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}
		try {
			Assert.assertEquals("finalField", ow.get("finalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}

		ow = new ObjectWrapper(new ParentClass());

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		Assert.assertEquals("normalField", ow.get("normalField"));
		Assert.assertEquals("finalField", ow.get("finalField"));
	}

	@Test
	public void test2OtherChildClassGet() {
		ObjectWrapper ow = new ObjectWrapper(ChildClass.class);

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		try {
			Assert.assertEquals("normalField", ow.get("normalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}
		try {
			Assert.assertEquals("finalField", ow.get("finalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}

		ow = new ObjectWrapper(new ChildClass());

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		Assert.assertEquals("normalField", ow.get("normalField"));
		Assert.assertEquals("finalField", ow.get("finalField"));
	}

	@Test
	public void test3ChildClassGet() {
		ObjectWrapper ow = new ObjectWrapper(ChildClass.class);

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		try {
			Assert.assertEquals("normalField", ow.get("normalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}
		try {
			Assert.assertEquals("finalField", ow.get("finalField"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}

		Assert.assertEquals("staticFieldChild", ow.get("staticFieldChild"));
		Assert.assertEquals("finalStaticFieldChild", ow.get("finalStaticFieldChild"));
		try {
			Assert.assertEquals("normalFieldChild", ow.get("normalFieldChild"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}
		try {
			Assert.assertEquals("finalFieldChild", ow.get("finalFieldChild"));
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FieldNotFoundException);
		}

		ow = new ObjectWrapper(new ChildClass());

		Assert.assertEquals("staticField", ow.get("staticField"));
		Assert.assertEquals("finalStaticField", ow.get("finalStaticField"));
		Assert.assertEquals("normalField", ow.get("normalField"));
		Assert.assertEquals("finalField", ow.get("finalField"));

		Assert.assertEquals("staticFieldChild", ow.get("staticFieldChild"));
		Assert.assertEquals("finalStaticFieldChild", ow.get("finalStaticFieldChild"));
		Assert.assertEquals("normalFieldChild", ow.get("normalFieldChild"));
		Assert.assertEquals("finalFieldChild", ow.get("finalFieldChild"));
	}

	@Test
	public void test4ParentClassSet() {
		ObjectWrapper ow = new ObjectWrapper(ParentClass.class);

		ow.set((Object) "staticField*", "staticField");
		ow.set((Object) "finalStaticField*", "finalStaticField");
		Assert.assertEquals("staticField*", ow.get("staticField"));
		Assert.assertEquals("finalStaticField*", ow.get("finalStaticField"));

		ow = new ObjectWrapper(new ParentClass());

		Assert.assertEquals("staticField*", ow.get("staticField"));
		Assert.assertEquals("finalStaticField*", ow.get("finalStaticField"));

		ow.set((Object) "normalField*", "normalField");
		ow.set((Object) "finalField*", "finalField");

		Assert.assertEquals("normalField*", ow.get("normalField"));
		Assert.assertEquals("finalField*", ow.get("finalField"));
	}

	@Test
	public void test5ChildClassSet() {
		ObjectWrapper ow = new ObjectWrapper(ChildClass.class);

		ow.set((Object) "staticField*", "staticField");
		ow.set((Object) "finalStaticField*", "finalStaticField");
		Assert.assertEquals("staticField*", ow.get("staticField"));
		Assert.assertEquals("finalStaticField*", ow.get("finalStaticField"));

		ow.set((Object) "staticFieldChild*", "staticFieldChild");
		ow.set((Object) "finalStaticFieldChild*", "finalStaticFieldChild");
		Assert.assertEquals("staticFieldChild*", ow.get("staticFieldChild"));
		Assert.assertEquals("finalStaticFieldChild*", ow.get("finalStaticFieldChild"));

		ow = new ObjectWrapper(new ChildClass());

		Assert.assertEquals("staticField*", ow.get("staticField"));
		Assert.assertEquals("finalStaticField*", ow.get("finalStaticField"));

		ow.set((Object) "normalField*", "normalField");
		ow.set((Object) "finalField*", "finalField");

		Assert.assertEquals("normalField*", ow.get("normalField"));
		Assert.assertEquals("finalField*", ow.get("finalField"));

		Assert.assertEquals("staticFieldChild*", ow.get("staticFieldChild"));
		Assert.assertEquals("finalStaticFieldChild*", ow.get("finalStaticFieldChild"));

		ow.set((Object) "normalFieldChild*", "normalFieldChild");
		ow.set((Object) "finalFieldChild*", "finalFieldChild");

		Assert.assertEquals("normalFieldChild*", ow.get("normalFieldChild"));
		Assert.assertEquals("finalFieldChild*", ow.get("finalFieldChild"));
	}

}
