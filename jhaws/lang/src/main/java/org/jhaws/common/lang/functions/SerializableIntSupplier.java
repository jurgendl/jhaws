package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.IntSupplier;

@FunctionalInterface
public interface SerializableIntSupplier extends IntSupplier, Serializable {
    public static IntSupplier enhance(SerializableIntSupplier supplier) {
        return supplier::getAsInt;
    }
}
