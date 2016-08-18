package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
	public static <T, R> Function<T, R> enhance(SFunction<T, R> function) {
		return function::apply;
	}
}
