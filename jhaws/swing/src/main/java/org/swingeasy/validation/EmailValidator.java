package org.swingeasy.validation;

/**
 * @author Jurgen
 */
public class EmailValidator extends RegexValidator {
    public EmailValidator() {
        super("(^([a-zA-Z0-9]+([\\.+_-][a-zA-Z0-9]+)*)@(([a-zA-Z0-9]+((\\.|[-]{1,2})[a-zA-Z0-9]+)*)\\.[a-zA-Z]{2,6}))?$");
    }

    /**
     * 
     * @see org.swingeasy.validation.Validator#getArguments(java.lang.Object)
     */
    @Override
    public Object[] getArguments(String value) {
        return new Object[] { value, "test@gmail.com" };
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        return "EmailValidator.invalid";
    }
}
