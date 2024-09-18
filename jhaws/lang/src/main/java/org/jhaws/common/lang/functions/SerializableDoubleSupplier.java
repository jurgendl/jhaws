package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface SerializableDoubleSupplier extends DoubleSupplier, Serializable {
    public static DoubleSupplier enhance(SerializableDoubleSupplier supplier) {
        return supplier::getAsDouble;
    }
}
