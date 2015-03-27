package org.jhaws.common.lang;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassPair<S, T> {
    private final Class<S> sourceClass;

    private final Class<T> targetClass;

    public ClassPair(Class<S> sourceClass, Class<T> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    public Class<S> getSourceClass() {
        return this.sourceClass;
    }

    public Class<T> getTargetClass() {
        return this.targetClass;
    }

    @Override
    public String toString() {
        return sourceClass.getName() + ">" + targetClass.getName();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ClassPair)) {
            return false;
        }
        ClassPair<?, ?> castOther = (ClassPair<?, ?>) other;
        return new EqualsBuilder().append(sourceClass, castOther.sourceClass).append(targetClass, castOther.targetClass).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sourceClass).append(targetClass).toHashCode();
    }
}