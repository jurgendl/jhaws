package org.jhaws.common.lang;

@FunctionalInterface
public interface CollectionMapper<S, SC> {
    SC collect(S sourceProxy);
}
