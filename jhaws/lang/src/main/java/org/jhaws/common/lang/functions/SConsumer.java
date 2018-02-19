package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface SConsumer<T> extends Consumer<T>, Serializable {
    public static <T> Consumer<T> enhance(SConsumer<T> consumer) {
        return consumer::accept;
    }
}
