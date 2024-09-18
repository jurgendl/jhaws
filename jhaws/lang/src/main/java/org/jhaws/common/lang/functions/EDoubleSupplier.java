package org.jhaws.common.lang.functions;

import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface EDoubleSupplier extends SerializableDoubleSupplier {
    public static DoubleSupplier enhance(EDoubleSupplier supplier) {
        return supplier::getAsDouble;
    }

    @Override
    default double getAsDouble() {
        try {
            return getAsDoubleEnhanced();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    double getAsDoubleEnhanced() throws Exception;
}
