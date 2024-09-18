package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {
    public static <T> Consumer<T> enhance(SerializableConsumer<T> consumer) {
        return consumer::accept;
    }
}
