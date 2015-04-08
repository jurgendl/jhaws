package org.jhaws.common.lang.functions;

import java.util.function.Predicate;

@FunctionalInterface
public interface EPredicate<T> extends SPredicate<T> {
	public static <T> Predicate<T> enhance(EPredicate<T> predicate) {
		return predicate::test;
	}

	@Override
	default boolean test(T t) {
		try {
			return testEnhanced(t);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	boolean testEnhanced(T t) throws Exception;
}