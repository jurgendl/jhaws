package org.jhaws.common.lang.functions;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface EBooleanSupplier extends SBooleanSupplier {
	public static BooleanSupplier enhance(EBooleanSupplier supplier) {
		return supplier::getAsBoolean;
	}

	@Override
	default boolean getAsBoolean() {
		try {
			return getAsBooleanEnhanced();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	boolean getAsBooleanEnhanced() throws Exception;
}
