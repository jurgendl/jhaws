package org.jhaws.common.lang;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.junit.Test;

public class BeanWrapperTest {
	public static class ChildClass extends ParentClass implements ChildInterface {
		private String childValue = "childValue";

		@Override
		public String getChildValue() {
			return this.childValue;
		}

		@Override
		public void setChildValue(String childValue) {
			this.childValue = childValue;
		}
	}

	public static interface ChildInterface extends ParentInterface {
		public String getChildValue();

		public void setChildValue(String parentValue);
	}

	public static class ParentClass implements ParentInterface {
		private String parentValue = "parentValue";

		@Override
		public String getParentValue() {
			return this.parentValue;
		}

		@Override
		public void setParentValue(String parentValue) {
			this.parentValue = parentValue;
		}
	}

	public static interface ParentInterface {
		public String getParentValue();

		public void setParentValue(String parentValue);
	}

	@Test
	public void testIntrospector() {
		try {
			BeanInfo bi;
			bi = Introspector.getBeanInfo(ParentClass.class);
			for (PropertyDescriptor o : bi.getPropertyDescriptors()) {
				System.out.println(o.getName() + ":" + o.getDisplayName() + ":" + o.getShortDescription() + ":" + o.getPropertyType() + ":" + o.getReadMethod() + ":"
						+ o.getWriteMethod() + ":" + o.isBound() + ":" + o.isConstrained() + ":" + o.isExpert() + ":" + o.isHidden() + ":" + o.isPreferred());
			}
			System.out.println("---------------------");
			bi = Introspector.getBeanInfo(ParentInterface.class);
			for (PropertyDescriptor o : bi.getPropertyDescriptors()) {
				System.out.println(o.getName() + ":" + o.getDisplayName() + ":" + o.getShortDescription() + ":" + o.getPropertyType() + ":" + o.getReadMethod() + ":"
						+ o.getWriteMethod() + ":" + o.isBound() + ":" + o.isConstrained() + ":" + o.isExpert() + ":" + o.isHidden() + ":" + o.isPreferred());
			}
			System.out.println("---------------------");
			bi = Introspector.getBeanInfo(ChildClass.class);
			for (PropertyDescriptor o : bi.getPropertyDescriptors()) {
				System.out.println(o.getName() + ":" + o.getDisplayName() + ":" + o.getShortDescription() + ":" + o.getPropertyType() + ":" + o.getReadMethod() + ":"
						+ o.getWriteMethod() + ":" + o.isBound() + ":" + o.isConstrained() + ":" + o.isExpert() + ":" + o.isHidden() + ":" + o.isPreferred());
			}
			System.out.println("---------------------");
			bi = Introspector.getBeanInfo(ChildInterface.class);
			for (PropertyDescriptor o : bi.getPropertyDescriptors()) {
				System.out.println(o.getName() + ":" + o.getDisplayName() + ":" + o.getShortDescription() + ":" + o.getPropertyType() + ":" + o.getReadMethod() + ":"
						+ o.getWriteMethod() + ":" + o.isBound() + ":" + o.isConstrained() + ":" + o.isExpert() + ":" + o.isHidden() + ":" + o.isPreferred());
			}
			System.out.println("---------------------");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testPropertyDescriptorsBean() {
		for (PropertyDescriptor pd : new PropertyDescriptorsBean(ChildTestBean.class).getPropertyDescriptors().values()) {
			System.out.println(pd.getName());
			System.out.println(pd.getReadMethod());
			System.out.println(pd.getWriteMethod());
			System.out.println();
		}
	}

	public static abstract class TestBean {
		private boolean property1;

		private Boolean property2;

		private String property3;

		public boolean isProperty1() {
			return property1;
		}

		public void setProperty1(boolean property1) {
			this.property1 = property1;
		}

		public Boolean getProperty2() {
			return property2;
		}

		public void setProperty2(Boolean property2) {
			this.property2 = property2;
		}

		public String getProperty3() {
			return property3;
		}

		public void setProperty3(String property3) {
			this.property3 = property3;
		}

		public abstract void setProperty4(Integer property4);
	}

	public static class ChildTestBean extends TestBean {
		private Integer property4;

		private final Float property5 = 0f;

		@Override
		public void setProperty1(boolean property1) {
			super.setProperty1(property1);
		}

		public Integer getProperty4() {
			return property4;
		}

		@Override
		public void setProperty4(Integer property4) {
			this.property4 = property4;
		}

		public Object setPropertyW1() {
			return null;
		}

		public void getPropertyW1(Object w1) {
			//
		}

		public Float getProperty5() {
			return property5;
		}
	}
}
