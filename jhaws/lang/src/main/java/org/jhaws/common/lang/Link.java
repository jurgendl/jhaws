package org.jhaws.common.lang;

import ch.lambdaj.Lambda;

public interface Link {
	public static <T> T link(Class<T> type) {
		return Lambda.on(type);
	}

	public static <A> String name(A argument) {
		return Lambda.argument(argument).getInkvokedPropertyName();
	}

	public static <T extends Link> T relink(T link) {
		return Lambda.on(Lambda.argument(link).getReturnType());
	}
}
