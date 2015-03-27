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
	public void test() {
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
}
