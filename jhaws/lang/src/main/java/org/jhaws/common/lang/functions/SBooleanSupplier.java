package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface SBooleanSupplier extends BooleanSupplier, Serializable {
    public static BooleanSupplier enhance(SBooleanSupplier supplier) {
        return supplier::getAsBoolean;
    }
}
