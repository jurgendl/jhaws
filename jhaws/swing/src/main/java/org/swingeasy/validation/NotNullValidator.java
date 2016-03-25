package org.swingeasy.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jurgen
 */
public class NotNullValidator<T> implements Validator<T> {
    /**
     *
     * @see org.swingeasy.validation.Validator#getArguments(java.lang.Object)
     */
    @Override
    public Object[] getArguments(T value) {
        return new Object[] { value };
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        return "NotNullValidator.invalid";
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#isValid(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isValid(Object context, T value) {
        return (value != null) && (!(value instanceof String) || StringUtils.isNotBlank(String.class.cast(value)));
    }
}
