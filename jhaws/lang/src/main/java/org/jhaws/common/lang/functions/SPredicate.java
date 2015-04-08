package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Predicate;

@FunctionalInterface
public interface SPredicate<T> extends Predicate<T>, Serializable {
	public static <T> Predicate<T> enhance(SPredicate<T> predicate) {
		return predicate::test;
	}
}