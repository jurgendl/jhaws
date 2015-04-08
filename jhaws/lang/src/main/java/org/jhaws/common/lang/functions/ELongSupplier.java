package org.jhaws.common.lang.functions;

import java.util.function.LongSupplier;

@FunctionalInterface
public interface ELongSupplier extends SLongSupplier {
	public static LongSupplier enhance(ELongSupplier supplier) {
		return supplier::getAsLong;
	}

	@Override
	default long getAsLong() {
		try {
			return getAsLongEnhanced();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	long getAsLongEnhanced() throws Exception;
}
