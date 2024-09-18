package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface SerializableBooleanSupplier extends BooleanSupplier, Serializable {
    public static BooleanSupplier enhance(SerializableBooleanSupplier supplier) {
        return supplier::getAsBoolean;
    }
}
