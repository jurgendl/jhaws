package org.jhaws.common.lang;

public class ParentClassIterator extends ParentIterator<Class<?>> {
	public ParentClassIterator(Class<?> clazz) {
		super(clazz, Class::getSuperclass, c -> !Object.class.equals(c));
	}
}
