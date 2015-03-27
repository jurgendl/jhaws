package org.jhaws.common.lang;

import java.util.Map;

@FunctionalInterface
public interface Mapper<S, T> {
	void apply(Map<Object, Object> context, S s, T t);
}
