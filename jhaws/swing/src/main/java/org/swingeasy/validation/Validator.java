package org.swingeasy.validation;

/**
 * @author Jurgen
 */
public interface Validator<T> {
    public abstract Object[] getArguments(T value);

    public abstract String getMessageKey();

    public abstract boolean isValid(Object context, T value);
}