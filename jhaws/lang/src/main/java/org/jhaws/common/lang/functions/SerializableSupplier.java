package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    public static <T> Supplier<T> enhance(SerializableSupplier<T> predicate) {
        return predicate::get;
    }
}
