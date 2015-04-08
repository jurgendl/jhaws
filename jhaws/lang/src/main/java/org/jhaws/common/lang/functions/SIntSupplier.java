package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.IntSupplier;

@FunctionalInterface
public interface SIntSupplier extends IntSupplier, Serializable {
	public static IntSupplier enhance(SIntSupplier supplier) {
		return supplier::getAsInt;
	}
}
