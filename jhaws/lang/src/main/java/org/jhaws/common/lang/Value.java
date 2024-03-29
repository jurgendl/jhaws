package org.jhaws.common.lang;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Value<T> implements Serializable, Supplier<T> {
    private static final long serialVersionUID = -5341543889953418944L;

    public static <T> Value<T> value(T value) {
        return new Value<>(value);
    }

    protected T value;

    public Value() {
        super();
    }

    public Value(T value) {
        this.setValue(value);
    }

    public T getValue() {
        return this.value;
    }

    public T setValue(T value) {
        return this.value = value;
    }

    @Override
    public T get() {
        return getValue();
    }

    public Value<T> set(T value) {
        setValue(value);
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Value<?> other = (Value<?>) obj;
        if (this.value == null) {
            if (other.value != null) return false;
        } else if (!this.value.equals(other.value)) return false;
        return true;
    }

    public Value<T> setOr(T t, Consumer<T> f) {
        if (isSet()) {
            f.accept(get());
        } else {
            set(t);
        }
        return this;
    }

    /**
     * @deprecated {@link #isSet()}
     */
    @Deprecated
    public boolean set() {
        return isSet();
    }

    public Value<T> reset() {
        return setNull();
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isSet() {
        return value != null;
    }

    public boolean isNotSet() {
        return !isSet();
    }

    public boolean isNotNull() {
        return !isNull();
    }

    public Value<T> setNull() {
        value = null;
        return this;
    }

    public Value<T> operate(UnaryOperator<T> operation) {
        return operate(t -> true, operation);
    }

    public Value<T> operate(Predicate<T> when, UnaryOperator<T> operation) {
        if (when.test(value)) {
            value = operation.apply(value);
        }
        return this;
    }

    public Value<T> operate(Predicate<T> when, UnaryOperator<T> operation, Supplier<T> elseOperation) {
        if (when.test(value)) {
            value = operation.apply(value);
        } else {
            value = elseOperation.get();
        }
        return this;
    }

    public Value<T> consume(Consumer<T> consume) {
        return consume(t -> true, consume);
    }

    public Value<T> consume(Predicate<T> when, Consumer<T> consume) {
        if (when.test(value)) {
            consume.accept(value);
        }
        return this;
    }

    public Value<T> consume(Predicate<T> when, Consumer<T> consume, Supplier<T> elseOperation) {
        if (when.test(value)) {
            consume.accept(value);
        } else {
            value = elseOperation.get();
        }
        return this;
    }

    public boolean is(Predicate<T> voorwaarde) {
        return voorwaarde.test(get());
    }
}
