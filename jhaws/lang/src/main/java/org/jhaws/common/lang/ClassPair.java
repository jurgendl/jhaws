package org.jhaws.common.lang;

public class ClassPair<S, T> extends KeyValue<Class<S>, Class<T>> {
    private static final long serialVersionUID = -5930427872429137357L;

    public ClassPair(Class<S> key, Class<T> value) {
        super(key, value);
    }

    public Class<S> getSourceClass() {
        return getKey();
    }

    public Class<T> getTargetClass() {
        return getValue();
    }

    @Override
    public String toString() {
        return getSourceClass().getName() + ">" + getTargetClass().getName();
    }
}