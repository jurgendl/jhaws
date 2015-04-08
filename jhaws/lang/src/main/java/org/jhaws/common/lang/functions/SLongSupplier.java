package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.LongSupplier;

@FunctionalInterface
public interface SLongSupplier extends LongSupplier, Serializable {
	public static LongSupplier enhance(SLongSupplier supplier) {
		return supplier::getAsLong;
	}
}
