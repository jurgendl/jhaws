package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.LongSupplier;

@FunctionalInterface
public interface SerializableLongSupplier extends LongSupplier, Serializable {
    public static LongSupplier enhance(SerializableLongSupplier supplier) {
        return supplier::getAsLong;
    }
}
