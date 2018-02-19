package org.jhaws.common.lang.functions;

import java.util.function.Supplier;

@FunctionalInterface
public interface ESupplier<T> extends SSupplier<T> {
    public static <T> Supplier<T> enhance(ESupplier<T> predicate) {
        return predicate::get;
    }

    @Override
    default T get() {
        try {
            return getEnhanced();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    T getEnhanced() throws Exception;
}
