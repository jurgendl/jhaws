package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface SDoubleSupplier extends DoubleSupplier, Serializable {
    public static DoubleSupplier enhance(SDoubleSupplier supplier) {
        return supplier::getAsDouble;
    }
}
