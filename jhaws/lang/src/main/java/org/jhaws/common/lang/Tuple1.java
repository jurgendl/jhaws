package org.jhaws.common.lang;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple1<T1> extends Value<T1> {
    public static <T1> Tuple1<T1> of(T1 t1) {
        return new Tuple1<>(t1);
    }

    public Tuple1() {
        super();
    }

    public Tuple1(T1 value) {
        super(value);
    }

    public T1 getT1() {
        return getValue();
    }

    public void setT1(T1 t1) {
        setValue(t1);
    }

    public Tuple1<T1> operateT1(UnaryOperator<T1> operation) {
        return operateT1(t -> true, operation);
    }

    public Tuple1<T1> operateT1(Predicate<T1> when, UnaryOperator<T1> operation) {
        if (when.test(value)) {
            value = operation.apply(value);
        }
        return this;
    }

    public Tuple1<T1> operateT1(Predicate<T1> when, UnaryOperator<T1> operation, Supplier<T1> elseOperation) {
        if (when.test(value)) {
            value = operation.apply(value);
        } else {
            value = elseOperation.get();
        }
        return this;
    }

    public Tuple1<T1> consumeT1(Consumer<T1> consumer) {
        return consumeT1(t -> true, consumer);
    }

    public Tuple1<T1> consumeT1(Predicate<T1> when, Consumer<T1> consumer) {
        if (when.test(value)) {
            consumer.accept(value);
        }
        return this;
    }

    public Tuple1<T1> consumeT1(Predicate<T1> when, Consumer<T1> consume, Supplier<T1> elseOperation) {
        if (when.test(value)) {
            consume.accept(value);
        } else {
            value = elseOperation.get();
        }
        return this;
    }
}
