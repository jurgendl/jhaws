package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.Predicate;

@FunctionalInterface
public interface SerializablePredicate<T> extends Predicate<T>, Serializable {
    public static <T> Predicate<T> enhance(SerializablePredicate<T> predicate) {
        return predicate::test;
    }
}