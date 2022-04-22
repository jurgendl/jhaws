package org.jhaws.common.lang;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    protected T4 t4;

    public Tuple4() {
        super();
    }

    public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        super(t1, t2, t3);
        this.t4 = t4;
    }

    @Override
    public String toString() {
        return "( " + value + " , " + t2 + ", " + t3 + ", " + t4 + " )";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(t2, t3, value, t4);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        @SuppressWarnings("rawtypes")
        Tuple4 other = (Tuple4) obj;
        return Objects.equals(this.t4, other.t4) && Objects.equals(this.t2, other.t2) && Objects.equals(this.t3, other.t3) && Objects.equals(this.value, other.value);
    }

    public T4 getT4() {
        return this.t4;
    }

    public void setT4(T4 t4) {
        this.t4 = t4;
    }

    public Tuple4<T1, T2, T3, T4> operateT4(UnaryOperator<T4> operation) {
        return operateT4(t -> true, operation);
    }

    public Tuple4<T1, T2, T3, T4> operateT4(Predicate<T4> when, UnaryOperator<T4> operation) {
        if (when.test(t4)) {
            t4 = operation.apply(t4);
        }
        return this;
    }

    public Tuple4<T1, T2, T3, T4> operateT4(Predicate<T4> when, UnaryOperator<T4> operation, Supplier<T4> elseOperation) {
        if (when.test(t4)) {
            t4 = operation.apply(t4);
        } else {
            t4 = elseOperation.get();
        }
        return this;
    }

    public Tuple4<T1, T2, T3, T4> consumeT4(Consumer<T4> consumer) {
        return consumeT4(t -> true, consumer);
    }

    public Tuple4<T1, T2, T3, T4> consumeT4(Predicate<T4> when, Consumer<T4> consumer) {
        if (when.test(t4)) {
            consumer.accept(t4);
        }
        return this;
    }

    public Tuple4<T1, T2, T3, T4> consumeT4(Predicate<T4> when, Consumer<T4> consume, Supplier<T4> elseOperation) {
        if (when.test(t4)) {
            consume.accept(t4);
        } else {
            t4 = elseOperation.get();
        }
        return this;
    }

    public <X> Tuple4<T1, T2, T3, X> projectT4(Function<T4, X> operation) {
        return projectT4(t -> true, operation);
    }

    public <X> Tuple4<T1, T2, T3, X> projectT4(Predicate<T4> when, Function<T4, X> operation) {
        return projectT4(when, operation, () -> null);
    }

    public <X> Tuple4<T1, T2, T3, X> projectT4(Predicate<T4> when, Function<T4, X> operation, Supplier<X> elseOperation) {
        return new Tuple4<T1, T2, T3, X>(value, t2, t3, when.test(t4) ? operation.apply(t4) : elseOperation.get());
    }
}
