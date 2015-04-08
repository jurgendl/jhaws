package org.jhaws.common.lang.functions;

import java.util.function.IntSupplier;

@FunctionalInterface
public interface EIntSupplier extends SIntSupplier {
	public static IntSupplier enhance(EIntSupplier supplier) {
		return supplier::getAsInt;
	}

	@Override
	default int getAsInt() {
		try {
			return getAsIntEnhanced();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	int getAsIntEnhanced() throws Exception;
}
