package org.jhaws.common.lang;

import java.util.function.BooleanSupplier;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanValue extends Value<Boolean> implements BooleanSupplier {
    private static final long serialVersionUID = -1530814975734843133L;

    public BooleanValue() {
        super(Boolean.FALSE);
    }

    public BooleanValue(Boolean value) {
        super(value);
    }

    public BooleanValue not() {
        set(!isTrue());
        return this;
    }

    public boolean is() {
        return isTrue();
    }

    public boolean isTrue() {
        return Boolean.TRUE.equals(get());
    }

    /**
     * does not return true when equals to null !!!!!!!!!!!!!!!!!!!!!
     * 
     * @see #isNull()
     * @see #isNotTrue()
     */
    public boolean isFalse() {
        return Boolean.FALSE.equals(get());
    }

    public boolean isNotTrue() {
        return !isTrue();
    }

    public boolean isNot() {
        return isNotTrue();
    }

    public boolean isNotFalse() {
        return !isFalse();
    }

    public BooleanValue setTrue() {
        set(Boolean.TRUE);
        return this;
    }

    public BooleanValue setFalse() {
        set(Boolean.FALSE);
        return this;
    }

    @Override
    public boolean getAsBoolean() {
        return Boolean.TRUE.equals(getValue());
    }

    public BooleanValue and(BooleanValue b) {
        return and(b.getAsBoolean());
    }

    public BooleanValue and(Boolean b) {
        return and(Boolean.TRUE.equals(b));
    }

    public BooleanValue and(boolean b) {
        set(getAsBoolean() && b);
        return this;
    }

    public BooleanValue or(BooleanValue b) {
        return or(b.getAsBoolean());
    }

    public BooleanValue or(Boolean b) {
        return or(Boolean.TRUE.equals(b));
    }

    public BooleanValue or(boolean b) {
        set(getAsBoolean() || b);
        return this;
    }
}
